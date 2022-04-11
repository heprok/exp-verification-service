package com.briolink.verificationservice.updater.handler.university

import com.briolink.verificationservice.common.jpa.read.entity.UniversityReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.UniversityReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL

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
                logo = domain.logo?.let { URL(it) },
            )
            return universityReadRepository.save(this)
        }
    }
}
