package com.briolink.expverificationservice.updater.handler.company

import com.briolink.expverificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.CompanyReadRepository
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class CompanyHandlerService(
    private val companyReadRepository: CompanyReadRepository
) {
    fun createOrUpdate(domain: CompanyEventData): CompanyReadEntity {
        companyReadRepository.findById(domain.id).orElse(CompanyReadEntity(domain.id)).apply {
            data = CompanyReadEntity.CompanyData(
                id = domain.id,
                name = domain.name,
                slug = domain.slug,
                logo = domain.logo,
            )
            return companyReadRepository.save(this)
        }
    }

    fun getCompanyData(id: UUID): CompanyReadEntity.CompanyData =
        companyReadRepository.findById(id).orElseThrow { throw EntityNotFoundException("Company $id not found") }.data
}
