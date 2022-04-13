package com.briolink.verificationservice.updater.handler.usereducation

import com.briolink.verificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.verificationservice.updater.handler.university.UniversityHandlerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserEducationHandlerService(
    private val userEducationReadRepository: UserEducationReadRepository,
    private val universityHandlerService: UniversityHandlerService,
) {
    fun createOrUpdate(domain: UserEducationEventData): UserEducationReadEntity {
        userEducationReadRepository.findById(domain.id).orElse(
            UserEducationReadEntity(domain.id).apply {
                data = UserEducationReadEntity.UserEducationData(
                    id = domain.id,
                    university = universityHandlerService.getUniversityData(id),
                    degree = "",
                    startDate = domain.startDate,
                    endDate = null
                )
            }
        ).apply {
            data.degree = domain.degree
            data.startDate = domain.startDate
            data.endDate = domain.endDate
            return userEducationReadRepository.save(this)
        }
    }

    fun updateCompany(university: CompanyReadRepository) {
        TODO("not implemented")
    }
}
