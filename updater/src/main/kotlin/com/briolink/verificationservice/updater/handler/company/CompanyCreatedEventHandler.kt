package com.briolink.verificationservice.updater.handler.company

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("CompanyCreatedEvent", "1.0")
class CompanyCreatedEventHandler(
    private val companyReadRepository: CompanyReadRepository,
    private val locationService: LocationService,
) : IEventHandler<CompanyCreatedEvent> {
    override fun handle(event: CompanyCreatedEvent) {
        companyReadRepository.save(
            CompanyReadEntity().apply {
                id = event.data.id
                name = event.data.name
                data = CompanyReadEntity.Data(
                    id = event.data.id,
                    slug = event.data.slug,
                    logo = event.data.logo,
                    name = event.data.name,
                    industryId = event.data.industry?.id,
                    industryName = event.data.industry?.name,
                    website = event.data.website,
                    marketSegmentId = event.data.occupation?.id,
                    marketSegmentName = event.data.occupation?.name,
                    location = event.data.locationId?.let { locationService.getLocationInfo(it, LocationMinInfo::class.java) },
                    description = event.data.description,
                    verifiedProjects = 0,
                    verifiedConnections = 0
                )
            }
        )
    }
}
