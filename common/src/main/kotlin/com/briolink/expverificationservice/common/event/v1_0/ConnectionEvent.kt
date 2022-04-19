package com.briolink.expverificationservice.common.event.v1_0

import com.briolink.expverificationservice.common.domain.v1_0.Verification
import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent

data class VerificationCreatedEvent(override val data: Verification) : Event<Verification>("1.0")
data class VerificationUpdatedEvent(override val data: Verification) : Event<Verification>("1.0")
data class VerificationSyncEvent(override val data: SyncData<Verification>) : SyncEvent<Verification>("1.0")
