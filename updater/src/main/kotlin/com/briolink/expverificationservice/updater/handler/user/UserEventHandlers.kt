package com.briolink.expverificationservice.updater.handler.user

import com.briolink.expverificationservice.updater.service.SyncService
import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandlers(
    EventHandler("UserCreatedEvent", "1.0"),
    EventHandler("UserUpdatedEvent", "1.0")
)
class UserCreatedEventHandler(
    private val userHandlerService: UserHandlerService,
) : IEventHandler<UserCreatedEvent> {
    override fun handle(event: UserCreatedEvent) {
        userHandlerService.createOrUpdate(event.data).also {
            if (event.name == "UserUpdatedEvent") {
                // userHandlerService.updateLocation(it)
            }
        }
    }
}

@Transactional
@EventHandler("UserSyncEvent", "1.0")
class UserSyncEventHandler(
    private val userHandlerService: UserHandlerService,
    syncService: SyncService,
) : SyncEventHandler<UserSyncEvent>(ObjectSyncEnum.User, syncService) {
    override fun handle(event: UserSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            userHandlerService.createOrUpdate(objectSync)
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
