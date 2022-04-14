package com.briolink.verificationservice.api.service.verifcation.education

import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.verificationservice.api.service.verifcation.VerificationService
import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.verificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.verificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
@Transactional
class EducationVerificationService(
    private val userEducationReadRepository: UserEducationReadRepository,

    override val entityManager: EntityManager,
    override val eventPublisher: EventPublisher,
    override val verificationWriteRepository: VerificationWriteRepository,
) : VerificationService() {

    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.Education

    override fun checkExistNotConfirmedObjectIdAndUserId(objectId: UUID, userId: UUID): Boolean =
        userEducationReadRepository.existsByIdAndUserIdAndStatus(userId, objectId, VerificationStatusEnum.NotConfirmed.value)
}
