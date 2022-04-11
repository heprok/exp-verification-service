package com.briolink.verificationservice.updater.userjobposition

import com.briolink.verificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.verificationservice.updater.handler.company.CompanyHandlerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserEducationHandlerService(
    private val userEducationReadRepository: UserEducationReadRepository,
    private val universityHandlerService: CompanyHandlerService,
) {
    fun createOrUpdate(domain: UserEducationEventData): UserEducationReadEntity {
        userEducationReadRepository.findById(domain.id).orElse(
            UserEducationReadEntity(domain.id).apply {
                universityId = domain.universityId
                data = UserEducationReadEntity.UserEducationData(
                    id = domain.id,
                    university = universityHandlerService.getCompanyData(id),
                    title = "", startDate = domain.startDate!!, endDate = null
                )
            }
        ).apply {
            data.id = domain.id
            data.title = domain.title
            data.startDate = domain.startDate!!
            data.endDate = domain.endDate
            return userEducationReadRepository.save(this)
        }
    }

    fun updateCompany(university: CompanyReadRepository) {
        TODO("not implemented")
    }
}
