package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.jpa.read.entity.SyncLogReadEntity
import com.briolink.lib.sync.ISyncLogRepository
import com.briolink.lib.sync.SyncLogId
import org.springframework.data.jpa.repository.JpaRepository

interface SyncLogReadRepository : JpaRepository<SyncLogReadEntity, SyncLogId>, ISyncLogRepository<SyncLogReadEntity>
