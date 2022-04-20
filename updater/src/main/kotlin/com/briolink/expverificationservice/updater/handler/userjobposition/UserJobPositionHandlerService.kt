package com.briolink.expverificationservice.updater.handler.userjobposition

import com.briolink.expverificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.expverificationservice.updater.handler.company.CompanyHandlerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class UserJobPositionHandlerService(
    private val userJobPositionReadRepository: UserJobPositionReadRepository,
    private val companyHandlerService: CompanyHandlerService,
) {
    fun createOrUpdate(domain: UserJobPositionEventData, status: VerificationStatus? = null): UserJobPositionReadEntity {
        userJobPositionReadRepository.findById(domain.id).orElse(
            UserJobPositionReadEntity(domain.id).apply {
                companyId = domain.companyId
                userId = domain.userId
                data = UserJobPositionReadEntity.UserJobPositionData(
                    id = domain.id,
                    company = companyHandlerService.getCompanyData(domain.companyId),
                    title = "", startDate = domain.startDate!!, endDate = null
                )
            }
        ).apply {
            if (data.company.id != domain.companyId)
                data.company = companyHandlerService.getCompanyData(domain.companyId)

            status?.let { this.status = it }

            data.title = domain.title
            data.startDate = domain.startDate!!
            data.endDate = domain.endDate

            return userJobPositionReadRepository.save(this)
        }
    }

    fun getById(id: UUID): UserJobPositionReadEntity =
        userJobPositionReadRepository.findById(id).orElseThrow {
            EntityNotFoundException("UserJobPosition with id $id not found")
        }

    fun updateCompany(company: CompanyReadEntity) {
        userJobPositionReadRepository.updateCompany(
            companyId = company.id,
            slug = company.data.slug,
            name = company.data.name,
            logo = company.data.logo?.toString()
        )
    }

    fun updateStatus(id: UUID, status: VerificationStatusEnum): Int =
        userJobPositionReadRepository.updateStatus(id, status.value)
}
