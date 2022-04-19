package com.briolink.expverificationservice.updater.handler.company

import com.briolink.expverificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.expverificationservice.updater.service.SyncService
import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandlers(
    EventHandler("CompanyCreatedEvent", "1.0"),
    EventHandler("CompanyUpdatedEvent", "1.0")
)
class CompanyEventHandler(
    private val companyHandlerService: CompanyHandlerService,
) : IEventHandler<CompanyCreatedEvent> {
    override fun handle(event: CompanyCreatedEvent) {
        companyHandlerService.createOrUpdate(event.data).also {
            if (event.name == "CompanyUpdatedEvent") {
                // companyHandlerService.updateLocation(it)
            }
        }
    }
}

@EventHandler("CompanySyncEvent", "1.0")
@Transactional
class CompanySyncEventHandler(
    private val companyReadRepository: CompanyReadRepository,
    private val companyHandlerService: CompanyHandlerService,
    syncService: SyncService,
) : SyncEventHandler<CompanySyncEvent>(ObjectSyncEnum.Company, syncService) {
    override fun handle(event: CompanySyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            companyHandlerService.createOrUpdate(objectSync)
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
