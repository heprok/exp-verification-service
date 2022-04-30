package com.briolink.expverificationservice.api.service.verifcation.workexperince

import com.briolink.expverificationservice.api.service.verifcation.VerificationService
import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.WorkExperienceVerificationReadRepository
import com.briolink.expverificationservice.common.jpa.write.repository.ObjectConfirmTypeWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationStatusWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.lib.event.publisher.EventPublisher
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
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
    private val userJobPositionReadRepository: UserJobPositionReadRepository,
    private val workExperienceVerificationReadRepository: WorkExperienceVerificationReadRepository
) : VerificationService() {
    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.WorkExperience

    override fun existObjectIdAndUserIdAndStatus(objectId: UUID, userId: UUID, status: VerificationStatusEnum): Boolean =
        userJobPositionReadRepository.existByIdAndUserAndStatus(objectId, userId, status.value)

    override fun setVerificationStatusInReadRepository(verificationId: UUID, status: VerificationStatusEnum): Boolean =
        workExperienceVerificationReadRepository.updateStatusById(verificationId, status.value) > 0

    override fun getUserIdByObjectId(objectId: UUID): UUID =
        userJobPositionReadRepository.getUserIdById(objectId)
            ?: throw DgsEntityNotFoundException("UserJobPosition with id $objectId not found")
}
