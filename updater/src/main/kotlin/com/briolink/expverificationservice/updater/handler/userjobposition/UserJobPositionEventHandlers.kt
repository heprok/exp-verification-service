package com.briolink.expverificationservice.updater.handler.userjobposition

import com.briolink.expverificationservice.updater.handler.verification.workexperience.WorkExperienceVerificationHandlerService
import com.briolink.expverificationservice.updater.service.SyncService
import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum

@EventHandlers(
    EventHandler("UserJobPositionCreatedEvent", "1.0"),
    EventHandler("UserJobPositionUpdatedEvent", "1.0")
)
class UserJobPositionCreatedEventHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
    private val workExperienceVerificationHandlerService: WorkExperienceVerificationHandlerService,
) : IEventHandler<UserJobPositionCreatedEvent> {
    override fun handle(event: UserJobPositionCreatedEvent) {
        if (event.data.startDate != null) {

            userJobPositionHandlerService.createOrUpdate(event.data).also {
                if (event.name == "UserJobPositionUpdatedEvent") {
                    workExperienceVerificationHandlerService.updateUserJobPosition(it)
                }
            }
        }
    }
}

@EventHandler("UserJobPositionSyncEvent", "1.0")
class UserJobPositionSyncEventHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
    private val workExperienceVerificationHandlerService: WorkExperienceVerificationHandlerService,
    syncService: SyncService,
) : SyncEventHandler<UserJobPositionSyncEvent>(ObjectSyncEnum.UserJobPosition, syncService) {
    override fun handle(event: UserJobPositionSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            if (objectSync.startDate != null) {
                userJobPositionHandlerService.createOrUpdate(objectSync).also {
                    workExperienceVerificationHandlerService.updateUserJobPosition(it)
                }
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
