package com.briolink.verificationservice.updater.handler.project

import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.event.v1_0.CompanyRoleSyncEvent
import com.briolink.verificationservice.common.jpa.read.entity.CompanyRoleReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyRoleReadRepository
import com.briolink.verificationservice.common.types.CompanyRoleTypeEnum
import com.briolink.verificationservice.updater.service.SyncService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("CompanyRoleSyncEvent", "1.0")
class CompanyRoleSyncEventHandler(
    private val companyRoleReadRepository: CompanyRoleReadRepository,
    syncService: SyncService,
) : SyncEventHandler<CompanyRoleSyncEvent>(ObjectSyncEnum.ConnectionCompanyRole, syncService) {
    override fun handle(event: CompanyRoleSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            val companyRole = companyRoleReadRepository.findByIdOrNull(objectSync.id) ?: CompanyRoleReadEntity()
            companyRole.apply {
                id = objectSync.id
                name = objectSync.name
                type = CompanyRoleTypeEnum.valueOf(objectSync.type.name)
                companyRoleReadRepository.save(this)
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
