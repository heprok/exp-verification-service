package com.briolink.verificationservice.updater.handler.userjobposition

import com.briolink.verificationservice.common.enumeration.JobPositionStatusEnum
import com.briolink.verificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.verificationservice.updater.handler.company.CompanyHandlerService
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
    fun createOrUpdate(domain: UserJobPositionEventData): UserJobPositionReadEntity {
        userJobPositionReadRepository.findById(domain.id).orElse(
            UserJobPositionReadEntity(domain.id).apply {
                companyId = domain.companyId
                data = UserJobPositionReadEntity.UserJobPositionData(
                    id = domain.id,
                    company = companyHandlerService.getCompanyData(id),
                    title = "", startDate = domain.startDate!!, endDate = null
                )
            }
        ).apply {
            status = JobPositionStatusEnum.valueOf(domain.status.name)

            if (data.company.id != domain.companyId)
                data.company = companyHandlerService.getCompanyData(domain.companyId)

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

    fun updateCompany(company: CompanyReadRepository) {
        TODO("not implemented")
    }
}
