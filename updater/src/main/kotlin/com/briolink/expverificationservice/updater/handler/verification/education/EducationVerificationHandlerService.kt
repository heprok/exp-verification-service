package com.briolink.expverificationservice.updater.handler.verification.education

import com.briolink.expverificationservice.common.domain.v1_0.ExpVerification
import com.briolink.expverificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.UniversityReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.expverificationservice.updater.handler.user.UserHandlerService
import com.briolink.expverificationservice.updater.handler.usereducation.UserEducationHandlerService
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
    fun createOrUpdate(domain: ExpVerification): EducationVerificationReadEntity {
        if (domain.objectConfirmType != ObjectConfirmType.Education) throw IllegalArgumentException("ObjectConfirmType is not Education")

        val userEducation = educationHandlerService.getById(domain.objectConfirmId)
        val user = userHandlerService.getById(domain.userId)

        educationVerificationReadRepository.findById(domain.id).orElse(EducationVerificationReadEntity()).apply {
            id = domain.id
            userId = domain.userId
            userFullName = user.fullName
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

            return educationVerificationReadRepository.save(this).also {
                educationHandlerService.updateStatus(userEducation.id, it.status)
            }
        }
    }

    fun setStatus(userEducationId: UUID, status: VerificationStatusEnum) {
        educationHandlerService.updateStatus(userEducationId, status)
    }

    fun updateUser(user: UserReadEntity) {
        educationVerificationReadRepository.updateUser(
            userId = user.id,
            slug = user.data.slug,
            fullName = user.fullName,
            firstName = user.data.firstName,
            lastName = user.data.lastName,
            image = user.data.image?.toString(),
        )
    }

    fun updateUniversity(university: UniversityReadEntity) {
        educationVerificationReadRepository.updateUniversity(
            universityId = university.id,
            name = university.data.name,
            logo = university.data.logo?.toString()
        )
    }

    fun updateUserEducation(education: UserEducationReadEntity) {
        educationVerificationReadRepository.updateEducation(
            userEducationId = education.id,
            startDate = education.data.startDate,
            endDate = education.data.endDate,
            degree = education.data.degree
        )
    }
}
