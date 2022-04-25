package com.briolink.expverificationservice.api.service.verifcation

import com.briolink.expverificationservice.api.exception.UserErrorGraphQlException
import com.briolink.expverificationservice.common.domain.v1_0.ExpVerificationChangeStatusEventData
import com.briolink.expverificationservice.common.domain.v1_0.ExpVerificationStatus
import com.briolink.expverificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.expverificationservice.common.enumeration.ActionTypeEnum
import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationChangedStatusEvent
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationCreatedEvent
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationUpdatedEvent
import com.briolink.expverificationservice.common.jpa.write.entity.ObjectConfirmTypeWriteEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationStatusWriteEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.expverificationservice.common.jpa.write.repository.ObjectConfirmTypeWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationStatusWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.expverificationservice.common.mapper.toDomain
import com.briolink.expverificationservice.common.mapper.toEnum
import com.briolink.lib.event.publisher.EventPublisher
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import mu.KLogging
import java.time.Instant
import java.util.UUID
import javax.persistence.EntityManager

abstract class VerificationService() {
    protected abstract val entityManager: EntityManager
    protected abstract val eventPublisher: EventPublisher
    protected abstract val verificationWriteRepository: VerificationWriteRepository
    protected abstract val objectConfirmTypeWriteRepository: ObjectConfirmTypeWriteRepository
    protected abstract val verificationStatusWriteRepository: VerificationStatusWriteRepository
    abstract val objectTypeVerification: ObjectConfirmTypeEnum

    companion object : KLogging()

    private fun confirmTypeReference(): ObjectConfirmTypeWriteEntity =
        entityManager.getReference(ObjectConfirmTypeWriteEntity::class.java, objectTypeVerification.value)
    // objectConfirmTypeWriteRepository.findByIdOrNull(objectTypeVerification.value)!!

    private fun statusReference(status: VerificationStatusEnum): VerificationStatusWriteEntity =
        // verificationStatusWriteRepository.findByIdOrNull(status.value)!!
        entityManager.getReference(VerificationStatusWriteEntity::class.java, status.value)

    private fun publishCreatedEvent(entity: VerificationWriteEntity) {
        eventPublisher.publish(ExpVerificationCreatedEvent(entity.toDomain()))
    }

    private fun publishUpdatedEvent(entity: VerificationWriteEntity) {
        eventPublisher.publish(ExpVerificationUpdatedEvent(entity.toDomain()))
    }

    private fun publishChangedStatusEvent(objectConfirmId: UUID, status: VerificationStatusEnum) {
        eventPublisher.publish(
            ExpVerificationChangedStatusEvent(
                ExpVerificationChangeStatusEventData(
                    objectConfirmId = objectConfirmId,
                    objectConfirmType = ObjectConfirmType.fromInt(objectTypeVerification.value),
                    status = ExpVerificationStatus.fromInt(status.value)
                )
            )
        )
    }

    private fun getById(id: UUID): VerificationWriteEntity =
        verificationWriteRepository.findById(id).orElseThrow { throw DgsEntityNotFoundException("Verification $id not found") }

    private fun getLastByObjectConfirm(objectId: UUID): VerificationWriteEntity =
        verificationWriteRepository.findFirstByObjectConfirmIdAndObjectConfirmTypeOrderByChangedDesc(objectId, confirmTypeReference())
            ?: throw DgsEntityNotFoundException("Verification not found")

    fun confirmVerification(id: UUID, byUserId: UUID, actionType: ActionTypeEnum): VerificationWriteEntity {
        return getById(id).apply {
            if (!this.userToConfirmIds.contains(byUserId)) throw UserErrorGraphQlException("User is not in the list of users to confirm")
            if (this.actionAt != null) throw UserErrorGraphQlException("Verification is already done")

            this.status = when (actionType) {
                ActionTypeEnum.Confirmed -> statusReference(VerificationStatusEnum.Confirmed)
                ActionTypeEnum.Rejected -> statusReference(VerificationStatusEnum.Rejected)
            }

            this.actionAt = Instant.now()
            this.actionBy = byUserId
        }.let {
            setVerificationStatusInReadRepository(it.id!!, it.status.toEnum())
            verificationWriteRepository.save(it)
        }.also {
            publishUpdatedEvent(it)
        }
    }

    fun resetObjectVerification(objectId: UUID, overrideStatus: VerificationStatusEnum? = null): VerificationStatusEnum {
        try {
            getLastByObjectConfirm(objectId).apply {
                this.status = overrideStatus?.let { statusReference(it) } ?: statusReference(VerificationStatusEnum.NotConfirmed)

                if (overrideStatus == VerificationStatusEnum.Rejected || overrideStatus == VerificationStatusEnum.Confirmed) {
                    this.actionAt = Instant.now()
                    this.actionBy = this.userId
                } else {
                    this.actionAt = null
                    this.actionBy = null
                }

                this.userToConfirmIds = emptyArray()
            }.let {
                setVerificationStatusInReadRepository(it.id!!, it.status.toEnum())
                verificationWriteRepository.save(it)
            }.also {
                publishUpdatedEvent(it)
                return it.status.toEnum()
            }
        } catch (ex: DgsEntityNotFoundException) {
            logger.warn { "Verification not found for object $objectId : $objectTypeVerification" }
            publishChangedStatusEvent(objectId, overrideStatus ?: VerificationStatusEnum.NotConfirmed)
            return overrideStatus ?: VerificationStatusEnum.NotConfirmed
        }
    }

    abstract fun existObjectIdAndUserIdAndStatus(objectId: UUID, userId: UUID, status: VerificationStatusEnum): Boolean
    abstract fun setVerificationStatusInReadRepository(verificationId: UUID, status: VerificationStatusEnum): Boolean

    fun addVerification(userId: UUID, objectId: UUID, userConfirmIds: List<UUID>): VerificationWriteEntity {

        if (userConfirmIds.contains(userId)) throw UserErrorGraphQlException("User can't confirm himself")

        if (!existObjectIdAndUserIdAndStatus(objectId, userId, VerificationStatusEnum.NotConfirmed))
            throw UserErrorGraphQlException("Not confirmed ${objectTypeVerification.name} $objectId and user $userId not found")

        val verification = VerificationWriteEntity().apply {
            this.userId = userId
            this.objectConfirmId = objectId
            this.objectConfirmType = confirmTypeReference()
            this.userToConfirmIds = userConfirmIds.toTypedArray()
            this.status = statusReference(VerificationStatusEnum.Pending)
        }.let {
            verificationWriteRepository.save(it)
        }.also {
            publishCreatedEvent(it)
        }
        return verification
    }
}
