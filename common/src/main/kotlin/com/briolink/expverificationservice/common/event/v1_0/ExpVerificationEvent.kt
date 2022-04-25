package com.briolink.expverificationservice.common.event.v1_0

import com.briolink.expverificationservice.common.domain.v1_0.ExpVerification
import com.briolink.expverificationservice.common.domain.v1_0.ExpVerificationChangeStatusEventData
import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent

data class ExpVerificationCreatedEvent(override val data: ExpVerification) : Event<ExpVerification>("1.0")
data class ExpVerificationUpdatedEvent(override val data: ExpVerification) : Event<ExpVerification>("1.0")
data class ExpVerificationChangedStatusEvent(override val data: ExpVerificationChangeStatusEventData) :
    Event<ExpVerificationChangeStatusEventData>("1.0")

data class ExpVerificationSyncEvent(override val data: SyncData<ExpVerification>) : SyncEvent<ExpVerification>("1.0")
