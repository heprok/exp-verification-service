package com.briolink.expverificationservice.updater.handler.usereducation

import com.briolink.expverificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.UniversityReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.expverificationservice.updater.handler.university.UniversityHandlerService
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
    fun createOrUpdate(domain: UserEducationEventData, status: VerificationStatus? = null): UserEducationReadEntity {
        userEducationReadRepository.findById(domain.id).orElse(
            UserEducationReadEntity(domain.id).apply {
                userId = domain.userId
                universityId = domain.universityId
                data = UserEducationReadEntity.UserEducationData(
                    id = domain.id,
                    university = universityHandlerService.getUniversityData(domain.universityId),
                    degree = "",
                    startDate = domain.startDate,
                    endDate = null
                )
            }
        ).apply {
            if (data.university.id != domain.universityId)
                data.university = universityHandlerService.getUniversityData(domain.universityId)

            status?.let { this.status = it }
            userId = domain.userId
            data.degree = domain.degree
            data.startDate = domain.startDate
            data.endDate = domain.endDate
            return userEducationReadRepository.save(this)
        }
    }

    fun getById(id: UUID): UserEducationReadEntity {
        return userEducationReadRepository.findById(id).orElseThrow { throw EntityNotFoundException("UserEducation with id $id not found") }
    }

    fun updateUniversity(university: UniversityReadEntity) {
        userEducationReadRepository.updateUniversity(
            universityId = university.id,
            name = university.data.name,
            logo = university.data.logo?.toString()
        )
    }

    fun updateStatus(id: UUID, status: VerificationStatusEnum): Int =
        userEducationReadRepository.updateStatus(id, status.value)
}
