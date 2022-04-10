package com.briolink.verificationservice.updater.handler.companyservice

import com.briolink.verificationservice.common.jpa.read.entity.CompanyServiceReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.briolink.verificationservice.updater.handler.userservice.UserServiceHandlerService
import java.time.Year
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class CompanyServiceHandlerService(
    private val companyServiceReadRepository: CompanyServiceReadRepository,
    private val userServiceHandlerService: UserServiceHandlerService,
) {
    fun createOrUpdate(eventData: CompanyServiceEventData): CompanyServiceReadEntity {
        val service = companyServiceReadRepository.findByIdOrNull(eventData.id) ?: CompanyServiceReadEntity()
        service.apply {
            id = eventData.id
            name = eventData.name
            slug = eventData.slug
            companyId = eventData.companyId
            data = CompanyServiceReadEntity.Data(
                id = eventData.id,
                slug = eventData.slug,
                image = eventData.logo,
                name = eventData.name,
                description = eventData.description,
                price = eventData.price?.toFloat()
            )
            return companyServiceReadRepository.save(this)
        }
    }

    fun updateStats(serviceId: UUID, verifiedUsed: Int, lastUsedYear: Year?) {
        val companyService = companyServiceReadRepository.findByIdOrNull(serviceId)
            ?: throw EntityNotFoundException("Company service not found with id $serviceId")
        companyService.lastUsedYear = lastUsedYear
        companyService.verifiedUsed = verifiedUsed
        companyServiceReadRepository.save(companyService)
        userServiceHandlerService.updateStatisticUserService(serviceId, verifiedUsed, companyService.lastUsed)
    }
}
