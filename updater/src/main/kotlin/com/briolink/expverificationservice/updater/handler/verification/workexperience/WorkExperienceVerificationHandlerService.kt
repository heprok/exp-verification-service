package com.briolink.expverificationservice.updater.handler.verification.workexperience

import com.briolink.expverificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.expverificationservice.common.domain.v1_0.Verification
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.WorkExperienceVerificationReadRepository
import com.briolink.expverificationservice.updater.handler.user.UserHandlerService
import com.briolink.expverificationservice.updater.handler.userjobposition.UserJobPositionHandlerService
import java.util.UUID
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class WorkExperienceVerificationHandlerService(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
    private val userHandlerService: UserHandlerService,
    private val workExperienceVerificationReadRepository: WorkExperienceVerificationReadRepository,
) {
    fun createOrUpdate(domain: Verification): WorkExperienceVerificationReadEntity {
        if (domain.objectConfirmType != ObjectConfirmType.WorkExperience)
            throw IllegalArgumentException("ObjectConfirmType is not Education")

        val userJobPosition = userJobPositionHandlerService.getById(domain.objectConfirmId)
        val user = userHandlerService.getById(domain.userId)

        workExperienceVerificationReadRepository.findById(domain.id).orElse(WorkExperienceVerificationReadEntity()).apply {
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

            userJobPositionId = userJobPosition.id
            companyId = userJobPosition.companyId
            companyName = userJobPosition.data.company.name
            jobPositionTitle = userJobPosition.data.title
            userJobPositionData = userJobPosition.data

            return workExperienceVerificationReadRepository.save(this)
        }
    }
}
