package com.briolink.verificationservice.api.service.verifcation.workexperince

import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.verificationservice.api.exception.UserErrorGraphQlException
import com.briolink.verificationservice.api.service.verifcation.VerificationService
import com.briolink.verificationservice.common.enumeration.ActionTypeEnum
import com.briolink.verificationservice.common.enumeration.JobPositionStatusEnum
import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.verificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.verificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID
import javax.persistence.EntityManager

@Service
@Transactional
class VerificationWorkExperienceService(
    override val verificationWriteRepository: VerificationWriteRepository,
    override val eventPublisher: EventPublisher,
    override val entityManager: EntityManager,

    private val userJobPositionReadRepository: UserJobPositionReadRepository,
) : VerificationService() {
    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.WorkExperience

    fun confirmVerification(id: UUID, byUserId: UUID, actionType: ActionTypeEnum): Boolean {
        getById(id).apply {
            if (!this.userToConfirmIds.contains(byUserId)) throw UserErrorGraphQlException("User is not in the list of users to confirm")
            if (this.actionAt != null) throw UserErrorGraphQlException("Verification is already done")

            this.actionType = actionType
            this.actionAt = Instant.now()
            this.actionBy = byUserId
        }.let {
            verificationWriteRepository.save(it)
        }.also {
            publishUpdatedEvent(it)
        }

        return true
    }

    fun checkExistNotConfirmedObjectIdAndUserId(objectId: UUID, userId: UUID): Boolean =
        userJobPositionReadRepository.existsByIdAndUserIdAndStatus(objectId, userId, JobPositionStatusEnum.NotConfirmed.value)

    fun addVerification(userId: UUID, objectId: UUID, userConfirmIds: List<UUID>): VerificationWriteEntity {

        if (userConfirmIds.contains(userId)) throw UserErrorGraphQlException("User can't confirm himself")

        if (!checkExistNotConfirmedObjectIdAndUserId(objectId, userId))
            throw UserErrorGraphQlException("User job position $objectId and user $userId not found")

        return VerificationWriteEntity().apply {
            this.userId = userId
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
