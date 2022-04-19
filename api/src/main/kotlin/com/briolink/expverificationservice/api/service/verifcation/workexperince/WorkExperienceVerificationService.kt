package com.briolink.expverificationservice.api.service.verifcation.workexperince

import com.briolink.expverificationservice.api.service.verifcation.VerificationService
import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.expverificationservice.common.jpa.write.repository.ObjectConfirmTypeWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationStatusWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.lib.event.publisher.EventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityManager

@Service
@Transactional
class WorkExperienceVerificationService(
    override val verificationWriteRepository: VerificationWriteRepository,
    override val eventPublisher: EventPublisher,
    override val entityManager: EntityManager,
    override val objectConfirmTypeWriteRepository: ObjectConfirmTypeWriteRepository,
    override val verificationStatusWriteRepository: VerificationStatusWriteRepository,
    private val userJobPositionReadRepository: UserJobPositionReadRepository
) : VerificationService() {
    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.WorkExperience

    override fun existObjectIdAndUserIdAndStatus(objectId: UUID, userId: UUID, status: VerificationStatusEnum): Boolean =
        userJobPositionReadRepository.existByIdAndUserAndStatus(objectId, userId, status.value)
}
