package com.briolink.verificationservice.updater.handler.verification.education

import com.briolink.verificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.verificationservice.common.domain.v1_0.Verification
import com.briolink.verificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.verificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.verificationservice.updater.handler.user.UserHandlerService
import com.briolink.verificationservice.updater.handler.usereducation.UserEducationHandlerService
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
@Transactional
class EducationVerificationHandlerService(
    private val educationHandlerService: UserEducationHandlerService,
    private val userHandlerService: UserHandlerService,
    private val educationVerificationReadRepository: EducationVerificationReadRepository,
) {
    fun createOrUpdate(domain: Verification): EducationVerificationReadEntity {
        if (domain.objectConfirmType != ObjectConfirmType.Education) throw IllegalArgumentException("ObjectConfirmType is not Education")

        val userEducation = educationHandlerService.getById(domain.objectConfirmId)
        val user = userHandlerService.getById(domain.userId)

        educationVerificationReadRepository.findById(domain.id).orElse(EducationVerificationReadEntity()).apply {
            id = domain.id
            userId = domain.userId
            userFullName = user.data.firstName + " " + user.data.lastName
            userToConfirmIds = domain.userToConfirmIds.toArray(arrayOfNulls<UUID>(domain.userToConfirmIds.size))
            actionAt = domain.actionAt
            actionBy = domain.actionBy
            status = domain.status.let { VerificationStatusEnum.ofValue(it.value) }
            created = domain.created
            changed = domain.changed
            userData = user.data

            userEducationId = userEducation.id
            universityId = userEducation.universityId
            universityName = userEducation.data.university.name
            degree = userEducation.data.degree
            userEducationData = userEducation.data

            return educationVerificationReadRepository.save(this)
        }
    }
}
