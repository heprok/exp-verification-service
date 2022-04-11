package com.briolink.verificationservice.updater.userjobposition

import com.briolink.verificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.verificationservice.updater.handler.company.CompanyHandlerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
            data.id = domain.id
            data.title = domain.title
            data.startDate = domain.startDate!!
            data.endDate = domain.endDate
            return userJobPositionReadRepository.save(this)
        }
    }

    fun updateCompany(company: CompanyReadRepository) {
        TODO("not implemented")
    }
}
