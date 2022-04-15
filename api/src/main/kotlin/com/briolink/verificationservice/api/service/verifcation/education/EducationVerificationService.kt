package com.briolink.verificationservice.api.service.verifcation.education

import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.verificationservice.api.service.verifcation.VerificationService
import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
@Transactional
class EducationVerificationService(
    override val entityManager: EntityManager,
    override val eventPublisher: EventPublisher,
    override val verificationWriteRepository: VerificationWriteRepository,
) : VerificationService() {

    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.Education
}