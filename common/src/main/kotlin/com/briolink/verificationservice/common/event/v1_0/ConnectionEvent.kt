package com.briolink.verificationservice.common.event.v1_0

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.briolink.verificationservice.common.domain.v1_0.Verification

data class VerificationCreatedEvent(override val data: Verification) : Event<Verification>("1.0")
data class VerificationUpdatedEvent(override val data: Verification) : Event<Verification>("1.0")
data class VerificationSyncEvent(override val data: SyncData<Verification>) : SyncEvent<Verification>("1.0")
