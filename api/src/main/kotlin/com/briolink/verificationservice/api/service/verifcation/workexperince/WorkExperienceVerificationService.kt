package com.briolink.verificationservice.api.service.verifcation.workexperince

import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.verificationservice.api.service.verifcation.VerificationService
import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional
class WorkExperienceVerificationService(
    override val verificationWriteRepository: VerificationWriteRepository,
    override val eventPublisher: EventPublisher,
    override val entityManager: EntityManager,
) : VerificationService() {
    override val objectTypeVerification: ObjectConfirmTypeEnum = ObjectConfirmTypeEnum.WorkExperience
}
