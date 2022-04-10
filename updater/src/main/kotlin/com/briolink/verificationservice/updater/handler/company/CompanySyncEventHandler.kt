package com.briolink.verificationservice.updater.handler.company

import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ConnectionCompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.updater.handler.userservice.UserServiceHandlerService
import com.briolink.verificationservice.updater.service.SyncService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@EventHandler("CompanySyncEvent", "1.0")
@Transactional
class CompanySyncEventHandler(
    private val projectReadRepository: ProjectReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val connectionCompanyReadRepository: ConnectionCompanyReadRepository,
    private val userServiceHandlerService: UserServiceHandlerService,
    private val locationService: LocationService,
    syncService: SyncService,
) : SyncEventHandler<CompanySyncEvent>(ObjectSyncEnum.Company, syncService) {
    override fun handle(event: CompanySyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            val company = companyReadRepository.findByIdOrNull(objectSync.id) ?: CompanyReadEntity().apply {
                data = CompanyReadEntity.Data(
                    id = objectSync.id,
                    slug = "",
                    name = ""
                )
            }
            val location =
                objectSync.locationId?.let { locationService.getLocationInfo(it, LocationMinInfo::class.java) }

            companyReadRepository.save(
                company.apply {
                    id = objectSync.id
                    name = objectSync.name
                    data = CompanyReadEntity.Data(
                        id = objectSync.id,
                        slug = objectSync.slug,
                        logo = objectSync.logo,
                        name = objectSync.name,
                        industryId = objectSync.industry?.id,
                        industryName = objectSync.industry?.name,
                        website = objectSync.website,
                        marketSegmentId = objectSync.occupation?.id,
                        marketSegmentName = objectSync.occupation?.name,
                        location = location,
                        description = objectSync.description,
                        verifiedProjects = company.data.verifiedProjects,
                        verifiedConnections = this.data.verifiedConnections,
                    )
                },
            ).also {
                userServiceHandlerService.updateCompany(it)
            }

            projectReadRepository.updateCompany(
                companyId = objectSync.id,
                name = objectSync.name,
                slug = objectSync.slug,
                logo = objectSync.logo?.toString(),
                occupation = objectSync.occupation?.name,
            )
            connectionCompanyReadRepository.updateCompany(
                id = objectSync.id,
                name = objectSync.name,
                description = objectSync.description,
                slug = objectSync.slug,
                logo = objectSync.logo?.toString(),
                marketSegmentId = objectSync.occupation?.id,
                marketSegmentName = objectSync.occupation?.name,
                industryId = objectSync.industry?.id,
                industryName = objectSync.industry?.name,
                website = objectSync.website?.toString(),
                countryId = location?.country?.id,
                stateId = location?.state?.id,
                cityId = location?.city?.id,
            )
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
