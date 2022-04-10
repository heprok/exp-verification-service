package com.briolink.verificationservice.updater.handler.company

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ConnectionCompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.updater.handler.userservice.UserServiceHandlerService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("CompanyUpdatedEvent", "1.0")
class CompanyUpdatedEventHandler(
    private val projectReadRepository: ProjectReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val connectionCompanyReadRepository: ConnectionCompanyReadRepository,
    private val userServiceHandlerService: UserServiceHandlerService,
    private val locationService: LocationService
) : IEventHandler<CompanyCreatedEvent> {
    override fun handle(event: CompanyCreatedEvent) {
        val company = companyReadRepository.findByIdOrNull(event.data.id) ?: CompanyReadEntity().apply {
            data = CompanyReadEntity.Data(
                id = event.data.id,
                slug = "",
                name = ""
            )
        }
        val location = event.data.locationId?.let { locationService.getLocationInfo(it, LocationMinInfo::class.java) }

        companyReadRepository.save(
            company.apply {
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
                    location = location,
                    description = event.data.description,
                    verifiedConnections = company.data.verifiedConnections,
                    verifiedProjects = company.data.verifiedProjects
                )
            }
        ).also {
            userServiceHandlerService.updateCompany(it)
        }

        projectReadRepository.updateCompany(
            companyId = event.data.id,
            name = event.data.name,
            slug = event.data.slug,
            logo = event.data.logo?.toString(),
            occupation = event.data.occupation?.name,
        )
        connectionCompanyReadRepository.updateCompany(
            id = event.data.id,
            name = event.data.name,
            description = event.data.description,
            slug = event.data.slug,
            logo = event.data.logo?.toString(),
            marketSegmentId = event.data.occupation?.id,
            marketSegmentName = event.data.occupation?.name,
            industryId = event.data.industry?.id,
            industryName = event.data.industry?.name,
            website = event.data.website?.toString(),
            countryId = location?.country?.id,
            stateId = location?.state?.id,
            cityId = location?.city?.id,
        )
    }
}
