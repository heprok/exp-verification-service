package com.briolink.verificationservice.updater.handler.user

import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.verificationservice.updater.service.SyncService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("UserSyncEvent", "1.0")
class UserSyncEventHandler(
    private val userReadRepository: UserReadRepository,
    private val locationService: LocationService,
    syncService: SyncService,
) : SyncEventHandler<UserSyncEvent>(ObjectSyncEnum.User, syncService) {
    override fun handle(event: UserSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            val user = userReadRepository.findByIdOrNull(objectSync.id) ?: UserReadEntity().apply {
                data = UserReadEntity.Data(
                    id = objectSync.id,
                    slug = "",
                    firstName = "",
                    lastName = ""
                )
            }
            userReadRepository.save(
                user.apply {
                    id = objectSync.id
                    name = "${objectSync.firstName} ${objectSync.lastName}"
                    personalEmail = objectSync.personalEmail
                    confirmed = objectSync.confirmed
                    data = UserReadEntity.Data(
                        id = objectSync.id,
                        slug = objectSync.slug,
                        image = objectSync.image,
                        firstName = objectSync.firstName,
                        lastName = objectSync.lastName,
                        location = objectSync.locationId?.let {
                            locationService.getLocationInfo(it, LocationMinInfo::class.java)
                        },
                        description = objectSync.description,
                        verifiedConnections = data.verifiedConnections,
                        verifiedProjects = data.verifiedProjects
                    )
                },
            )
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
