package com.briolink.expverificationservice.updater.handler.usereducation

import com.briolink.expverificationservice.updater.handler.verification.education.EducationVerificationHandlerService
import com.briolink.expverificationservice.updater.service.SyncService
import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum

@EventHandlers(
    EventHandler("UserEducationCreatedEvent", "1.0"),
    EventHandler("UserEducationUpdatedEvent", "1.0")
)
class UserEducationCreatedEventHandler(
    private val userEducationHandlerService: UserEducationHandlerService,
    private val educationVerificationEducationHandlerService: EducationVerificationHandlerService,
) : IEventHandler<UserEducationCreatedEvent> {
    override fun handle(event: UserEducationCreatedEvent) {
        userEducationHandlerService.createOrUpdate(event.data).also {
            if (event.name == "UserEducationUpdatedEvent") {
                educationVerificationEducationHandlerService.updateUserEducation(it)
            }
        }
    }
}

@EventHandler("UserEducationSyncEvent", "1.0")
class UserEducationSyncEventHandler(
    private val userEducationHandlerService: UserEducationHandlerService,
    private val educationVerificationEducationHandlerService: EducationVerificationHandlerService,
    syncService: SyncService,
) : SyncEventHandler<UserEducationSyncEvent>(ObjectSyncEnum.UserEducation, syncService) {
    override fun handle(event: UserEducationSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            userEducationHandlerService.createOrUpdate(objectSync).also {
                educationVerificationEducationHandlerService.updateUserEducation(it)
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
