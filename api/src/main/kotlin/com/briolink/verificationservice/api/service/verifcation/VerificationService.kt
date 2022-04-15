package com.briolink.verificationservice.api.service.verifcation

import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.verificationservice.api.exception.UserErrorGraphQlException
import com.briolink.verificationservice.common.enumeration.ActionTypeEnum
import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.verificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.verificationservice.common.event.v1_0.VerificationCreatedEvent
import com.briolink.verificationservice.common.event.v1_0.VerificationUpdatedEvent
import com.briolink.verificationservice.common.jpa.write.entity.ObjectConfirmTypeWriteEntity
import com.briolink.verificationservice.common.jpa.write.entity.VerificationStatusWriteEntity
import com.briolink.verificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.verificationservice.common.mapper.toDomain
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import java.time.Instant
import java.util.UUID
import javax.persistence.EntityManager

abstract class VerificationService() {
    protected abstract val entityManager: EntityManager
    protected abstract val eventPublisher: EventPublisher
    protected abstract val verificationWriteRepository: VerificationWriteRepository
    abstract val objectTypeVerification: ObjectConfirmTypeEnum

    private fun confirmTypeReference(): ObjectConfirmTypeWriteEntity =
        entityManager.getReference(ObjectConfirmTypeWriteEntity::class.java, objectTypeVerification.value)

    private fun statusReference(status: VerificationStatusEnum): VerificationStatusWriteEntity =
        entityManager.getReference(VerificationStatusWriteEntity::class.java, status.value)

    private fun publishCreatedEvent(entity: VerificationWriteEntity) {
        eventPublisher.publishAsync(VerificationCreatedEvent(entity.toDomain()))
    }

    private fun publishUpdatedEvent(entity: VerificationWriteEntity) {
        eventPublisher.publishAsync(VerificationUpdatedEvent(entity.toDomain()))
    }

    private fun getById(id: UUID): VerificationWriteEntity =
        verificationWriteRepository.findById(id).orElseThrow { throw DgsEntityNotFoundException("Verification $id not found") }

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
            verificationWriteRepository.save(it)
        }.also {
            publishUpdatedEvent(it)
        }
    }

    private fun checkObjectIdWithUserIdNotConfirmed(objectId: UUID, userId: UUID): Boolean =
        verificationWriteRepository.existsByUserIdAndObjectConfirmIdAndObjectConfirmTypeIdAndStatusId(
            userId = userId,
            objectConfirmId = objectId,
            typeId = objectTypeVerification.value,
            statusId = VerificationStatusEnum.NotConfirmed.value
        )

    fun addVerification(userId: UUID, objectId: UUID, userConfirmIds: List<UUID>): VerificationWriteEntity {

        if (userConfirmIds.contains(userId)) throw UserErrorGraphQlException("User can't confirm himself")

        if (!checkObjectIdWithUserIdNotConfirmed(objectId, userId))
            throw UserErrorGraphQlException("${objectTypeVerification.name} $objectId and user $userId not found")

        return VerificationWriteEntity().apply {
            this.userId = userId
            this.status = statusReference(VerificationStatusEnum.NotConfirmed)
            this.objectConfirmId = objectId
            this.objectConfirmType = confirmTypeReference()
            this.userToConfirmIds = userConfirmIds.toTypedArray()
        }.let {
            verificationWriteRepository.save(it)
        }.also {
            publishCreatedEvent(it)
        }
    }
}
