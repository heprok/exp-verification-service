package com.briolink.expverificationservice.updater.handler.verification.workexperience

import com.briolink.expverificationservice.common.domain.v1_0.ExpVerification
import com.briolink.expverificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.WorkExperienceVerificationReadRepository
import com.briolink.expverificationservice.updater.handler.user.UserHandlerService
import com.briolink.expverificationservice.updater.handler.userjobposition.UserJobPositionHandlerService
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
@Transactional
class WorkExperienceVerificationHandlerService(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
    private val userHandlerService: UserHandlerService,
    private val workExperienceVerificationReadRepository: WorkExperienceVerificationReadRepository,

) {
    fun createOrUpdate(domain: ExpVerification): WorkExperienceVerificationReadEntity {
        if (domain.objectConfirmType != ObjectConfirmType.WorkExperience)
            throw IllegalArgumentException("ObjectConfirmType is not WorkExperience")

        val userJobPosition = userJobPositionHandlerService.getById(domain.objectConfirmId)
        val user = userHandlerService.getById(domain.userId)

        workExperienceVerificationReadRepository.findById(domain.id).orElse(WorkExperienceVerificationReadEntity()).apply {
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

            userJobPositionId = userJobPosition.id
            companyId = userJobPosition.companyId
            companyName = userJobPosition.data.company.name
            jobPositionTitle = userJobPosition.data.title
            userJobPositionData = userJobPosition.data

            return workExperienceVerificationReadRepository.save(this).also {
                userJobPositionHandlerService.updateStatus(it.userJobPositionId, it.status)
            }
        }
    }

    fun updateCompany(company: CompanyReadEntity) {
        workExperienceVerificationReadRepository.updateCompany(
            companyId = company.id,
            slug = company.data.slug,
            name = company.data.name,
            logo = company.data.logo?.toString()
        )
    }

    fun updateUser(user: UserReadEntity) {
        workExperienceVerificationReadRepository.updateUser(
            userId = user.id,
            slug = user.data.slug,
            fullName = user.fullName,
            firstName = user.data.firstName,
            lastName = user.data.lastName,
            image = user.data.image?.toString(),
        )
    }

    fun updateUserJobPosition(jobPosition: UserJobPositionReadEntity) {
        workExperienceVerificationReadRepository.updateJobPosition(
            userJobPositionId = jobPosition.id,
            title = jobPosition.data.title,
            startDate = jobPosition.data.startDate.toString(),
            endDate = jobPosition.data.endDate?.toString(),
        )
    }
}
