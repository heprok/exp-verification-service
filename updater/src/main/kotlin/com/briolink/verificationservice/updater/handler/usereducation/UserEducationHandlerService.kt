package com.briolink.verificationservice.updater.handler.usereducation

import com.briolink.verificationservice.common.enumeration.EducationStatusEnum
import com.briolink.verificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.verificationservice.updater.handler.university.UniversityHandlerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class UserEducationHandlerService(
    private val userEducationReadRepository: UserEducationReadRepository,
    private val universityHandlerService: UniversityHandlerService,
) {
    fun createOrUpdate(domain: UserEducationEventData): UserEducationReadEntity {
        userEducationReadRepository.findById(domain.id).orElse(
            UserEducationReadEntity(domain.id).apply {
                userId = domain.userId
                data = UserEducationReadEntity.UserEducationData(
                    id = domain.id,
                    university = universityHandlerService.getUniversityData(id),
                    degree = "",
                    startDate = domain.startDate,
                    endDate = null
                )
            }
        ).apply {
            userId = domain.userId
            status = domain.status.let { EducationStatusEnum.ofValue(it.value) }
            data.degree = domain.degree
            data.startDate = domain.startDate
            data.endDate = domain.endDate
            return userEducationReadRepository.save(this)
        }
    }

    fun getById(id: UUID): UserEducationReadEntity {
        return userEducationReadRepository.findById(id).orElseThrow { throw EntityNotFoundException("UserEducation with id $id not found") }
    }

    fun updateCompany(university: CompanyReadRepository) {
        TODO("not implemented")
    }
}
