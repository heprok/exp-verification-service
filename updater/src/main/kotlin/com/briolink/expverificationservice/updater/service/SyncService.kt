package com.briolink.expverificationservice.updater.service

import com.briolink.expverificationservice.common.jpa.read.repository.SyncLogReadRepository
import com.briolink.lib.sync.BaseSyncService
import com.briolink.lib.sync.SyncLogId
import com.briolink.lib.sync.SyncWebClient
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.briolink.lib.sync.enumeration.UpdaterEnum
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class SyncService(
    override val syncWebClient: SyncWebClient,
    override val syncLogRepository: SyncLogReadRepository
) : BaseSyncService() {
    override val CURRENT_UPDATER: UpdaterEnum = UpdaterEnum.ExpVerification
    override fun getListSyncLogIdAtCompany(syncId: Int): List<SyncLogId> {
        val listSyncLogId =
            listOf(
                SyncLogId().apply {
                    this.syncId = syncId
                    this._service = ServiceEnum.Company.id
                    this._objectSync = ObjectSyncEnum.Company.value
                },
            )
        return listSyncLogId
    }

    override fun getListSyncLogIdAtUser(syncId: Int): List<SyncLogId> {
        val listSyncLogId =
            listOf(
                SyncLogId().apply {
                    this.syncId = syncId
                    this._service = ServiceEnum.User.id
                    this._objectSync = ObjectSyncEnum.User.value
                },
                SyncLogId().apply {
                    this.syncId = syncId
                    this._service = ServiceEnum.User.id
                    this._objectSync = ObjectSyncEnum.UserJobPosition.value
                },
                SyncLogId().apply {
                    this.syncId = syncId
                    this._service = ServiceEnum.User.id
                    this._objectSync = ObjectSyncEnum.UserEducation.value
                },
            )
        return listSyncLogId
    }

    override fun getListSyncLogIdAtConnection(syncId: Int): List<SyncLogId> {
        return listOf()
    }

    override fun getListSyncLogIdAtCompanyService(syncId: Int): List<SyncLogId> {
        return listOf()
    }

    override fun getListSyncLogIdAtExpVerification(syncId: Int): List<SyncLogId> {
        val listSyncLogId =
            listOf(
                SyncLogId().apply {
                    this.syncId = syncId
                    this._service = ServiceEnum.ExpVerification.id
                    this._objectSync = ObjectSyncEnum.ExpVerification.value
                },
            )

        return listSyncLogId
    }
}
