package com.briolink.expverificationservice.api.service.verifcation.education

import com.briolink.expverificationservice.api.service.verifcation.VerificationService
import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.expverificationservice.common.jpa.write.repository.ObjectConfirmTypeWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationStatusWriteRepository
import com.briolink.expverificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.lib.event.publisher.EventPublisher
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
@Transactional
class EducationVerificationService(
    override val entityManager: EntityManager,
    override val eventPublisher: EventPublisher,
    override val verificationWriteRepository: VerificationWriteRepository,
    override val objectConfirmTypeWriteRepository: ObjectConfirmTypeWriteRepository,
    override val verificationStatusWriteRepository: VerificationStatusWriteRepository,

    private val userEducationReadRepository: UserEducationReadRepository,
    private val educationVerificationReadRepository: EducationVerificationReadRepository
) : VerificationService() {

    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.Education

    override fun existObjectIdAndUserIdAndStatus(objectId: UUID, userId: UUID, status: VerificationStatusEnum): Boolean =
        userEducationReadRepository.existByIdAndUserAndStatus(objectId, userId, status.value)

    override fun setVerificationStatusInReadRepository(verificationId: UUID, status: VerificationStatusEnum): Boolean =
        educationVerificationReadRepository.updateStatusById(verificationId, status.value) > 0

    override fun getUserIdByObjectId(objectId: UUID): UUID =
        userEducationReadRepository.getUserIdById(objectId) ?: throw DgsEntityNotFoundException("UserEducation with id $objectId not found")
}
