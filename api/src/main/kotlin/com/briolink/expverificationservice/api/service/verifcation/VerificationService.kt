package com.briolink.expverificationservice.api.service.verifcation

import com.briolink.expverificationservice.common.enumeration.ActionTypeEnum
import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationCreatedEvent
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationSyncEvent
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationUpdatedEvent
import com.briolink.expverificationservice.common.jpa.write.entity.ObjectConfirmTypeWriteEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationStatusWriteEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.expverificationservice.common.jpa.write.repository.ObjectConfirmTypeWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationStatusWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.expverificationservice.common.mapper.toDomain
import com.briolink.expverificationservice.common.mapper.toEnum
import com.briolink.lib.common.exception.ValidationException
import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncUtil
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.briolink.lib.sync.model.PeriodDateTime
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import mu.KLogging
import java.time.Instant
import java.util.Optional
import java.util.UUID
import javax.persistence.EntityManager

abstract class VerificationService() {
    protected abstract val entityManager: EntityManager
    protected abstract val eventPublisher: EventPublisher
    protected abstract val verificationWriteRepository: VerificationWriteRepository
    protected abstract val objectConfirmTypeWriteRepository: ObjectConfirmTypeWriteRepository
    protected abstract val verificationStatusWriteRepository: VerificationStatusWriteRepository

    abstract val objectTypeVerification: ObjectConfirmTypeEnum

    abstract fun existObjectIdAndUserIdAndStatus(objectId: UUID, userId: UUID, status: VerificationStatusEnum): Boolean
    abstract fun setVerificationStatusInReadRepository(verificationId: UUID, status: VerificationStatusEnum): Boolean
    abstract fun getUserIdByObjectId(objectId: UUID): UUID

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

    // private fun publishChangedStatusEvent(objectConfirmId: UUID, status: VerificationStatusEnum) {
    //     eventPublisher.publish(
    //         ExpVerificationChangedStatusEvent(
    //             ExpVerificationChangeStatusEventData(
    //                 objectConfirmId = objectConfirmId,
    //                 objectConfirmType = ObjectConfirmType.fromInt(objectTypeVerification.value),
    //                 status = ExpVerificationStatus.fromInt(status.value)
    //             )
    //         )
    //     )
    // }

    /**
     * Get by id verification write entity
     *
     * @throws DgsEntityNotFoundException if entity not found
     *
     * @return VerificationWriteEntity
     */
    private fun getById(id: UUID): VerificationWriteEntity =
        verificationWriteRepository.findById(id).orElseThrow { throw DgsEntityNotFoundException("Verification $id not found") }

    private fun getLastByObjectConfirmOrCreate(objectId: UUID): Optional<VerificationWriteEntity> =
        verificationWriteRepository.findFirstByObjectConfirmIdAndObjectConfirmTypeOrderByChangedDesc(objectId, confirmTypeReference())

    /**
     * Confirm verification
     *
     * @throws ValidationException
     * @return VerificationWriteEntity
     */
    @Throws(ValidationException::class)
    fun confirmVerification(
        id: UUID,
        byUserId: UUID,
        actionType: ActionTypeEnum,
        overrideAction: Boolean = false
    ): VerificationWriteEntity {
        return getById(id).apply {
            if (!overrideAction && !this.userToConfirmIds.contains(byUserId))
                throw ValidationException("User is not in the list of users to confirm")

            if (this.actionAt != null) throw ValidationException("Verification is already done")

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
        val optVerification = getLastByObjectConfirmOrCreate(objectId)
        val isNew = optVerification.isEmpty

        val verification = optVerification.orElse(
            VerificationWriteEntity().apply {
                this.objectConfirmId = objectId
                this.objectConfirmType = confirmTypeReference()
                this.status = statusReference(VerificationStatusEnum.NotConfirmed)
            }
        )

        verification.apply {
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
            if (isNew)
                publishCreatedEvent(it)
            else {
                logger.warn { "Verification not found for object $objectId : $objectTypeVerification" }
                publishUpdatedEvent(it)
            }

            return it.status.toEnum()
        }
    }

    /**
     * Create verification
     *
     * @throws ValidationException
     * @throws DgsEntityNotFoundException
     *
     * @return VerificationWriteEntity
     */
    @Throws(ValidationException::class, DgsEntityNotFoundException::class)
    fun addVerification(userId: UUID, objectId: UUID, userConfirmIds: List<UUID>): VerificationWriteEntity {

        if (userConfirmIds.contains(userId)) throw ValidationException("User can't confirm himself")

        if (!existObjectIdAndUserIdAndStatus(objectId, userId, VerificationStatusEnum.NotConfirmed))
            throw DgsEntityNotFoundException("Not confirmed ${objectTypeVerification.name} $objectId and user $userId not found")

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

    private fun publishExpVerificationSyncEvent(
        syncId: Int,
        objectIndex: Long,
        totalObjects: Long,
        entity: VerificationWriteEntity?
    ) {
        eventPublisher.publishAsync(
            ExpVerificationSyncEvent(
                SyncData(
                    service = ServiceEnum.ExpVerification,
                    syncId = syncId,
                    objectIndex = objectIndex,
                    totalObjects = totalObjects,
                    objectSync = entity?.toDomain()
                )
            )
        )
    }

    fun publishSyncEvent(syncId: Int, period: PeriodDateTime? = null) {
        SyncUtil.publishSyncEvent(period, verificationWriteRepository) { indexElement, totalElements, entity ->
            publishExpVerificationSyncEvent(
                syncId, indexElement, totalElements,
                entity as VerificationWriteEntity?,
            )
        }
    }
}
