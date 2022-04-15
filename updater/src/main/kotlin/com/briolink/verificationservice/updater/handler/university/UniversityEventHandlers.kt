package com.briolink.verificationservice.updater.handler.university

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.updater.service.SyncService
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandlers(
    EventHandler("UniversityCreatedEvent", "1.0"),
    EventHandler("UniversityUpdatedEvent", "1.0")
)
class UniversityEventHandler(
    private val universityHandlerService: UniversityHandlerService,
) : IEventHandler<UniversityCreatedEvent> {
    override fun handle(event: UniversityCreatedEvent) {
        universityHandlerService.createOrUpdate(event.data).also {
            if (event.name == "UniversityUpdatedEvent") {
                // universityHandlerService.updateLocation(it)
            }
        }
    }
}

@Transactional
@EventHandler("UniversitySyncEvent", "1.0")
class UniversitySyncEventHandler(
    private val universityHandlerService: UniversityHandlerService,
    syncService: SyncService,
) : SyncEventHandler<UniversitySyncEvent>(ObjectSyncEnum.University, syncService) {
    override fun handle(event: UniversitySyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            universityHandlerService.createOrUpdate(objectSync)
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
