package com.briolink.verificationservice.updater.handler.companyservice

import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.updater.handler.userservice.UserServiceHandlerService
import com.briolink.verificationservice.updater.service.SyncService
import org.springframework.transaction.annotation.Transactional

@EventHandler("CompanyServiceSyncEvent", "1.0")
@Transactional
class CompanyServiceSyncEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    private val userServiceHandlerService: UserServiceHandlerService,
    private val projectReadRepository: ProjectReadRepository,
    syncService: SyncService,
) : SyncEventHandler<CompanyServiceSyncEvent>(ObjectSyncEnum.CompanyService, syncService) {
    override fun handle(event: CompanyServiceSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            companyServiceHandlerService.createOrUpdate(objectSync).also {
                projectReadRepository.updateService(it.id!!, it.name, it.slug)
                userServiceHandlerService.updateCompanyService(it)
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
