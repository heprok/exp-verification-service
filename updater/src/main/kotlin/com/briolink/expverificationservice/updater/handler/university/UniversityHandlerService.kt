package com.briolink.expverificationservice.updater.handler.university

import com.briolink.expverificationservice.common.jpa.read.entity.UniversityReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.UniversityReadRepository
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class UniversityHandlerService(
    private val universityReadRepository: UniversityReadRepository
) {
    fun createOrUpdate(domain: UniversityEventData): UniversityReadEntity {
        universityReadRepository.findById(domain.id).orElse(UniversityReadEntity(domain.id)).apply {
            data = UniversityReadEntity.UniversityData(
                id = domain.id,
                name = domain.name,
                logo = domain.logo,
            )
            return universityReadRepository.save(this)
        }
    }

    fun getUniversityData(id: UUID): UniversityReadEntity.UniversityData =
        universityReadRepository.findById(id).orElseThrow { throw EntityNotFoundException("University $id not found") }.data
}
