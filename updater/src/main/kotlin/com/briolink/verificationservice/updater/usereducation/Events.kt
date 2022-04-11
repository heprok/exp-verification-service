package com.briolink.verificationservice.updater.userjobposition

import com.briolink.lib.event.Event
import com.briolink.lib.location.model.LocationId
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.briolink.verificationservice.updater.handler.user.UserEventData
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.time.LocalDate
import java.util.UUID

data class UserJobPositionEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val title: String,
    @JsonProperty
    val startDate: LocalDate? = null,
    @JsonProperty
    val endDate: LocalDate? = null,
    @JsonProperty
    val companyId: UUID,
    @JsonProperty
    val userId: UUID,
)

data class UserJobPositionDeletedEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val userId: UUID,
    @JsonProperty
    val isCurrent: Boolean = false
)

data class UserJobPositionEvent(override val data: UserJobPositionEventData) : Event<UserJobPositionEventData>("1.0")
data class UserJobPositionDeletedEvent(override val data: UserJobPositionDeletedEventData) : Event<UserJobPositionDeletedEventData>("1.0")
data class UserJobPositionSyncEvent(override val data: SyncData<UserJobPositionEventData>) : SyncEvent<UserJobPositionEventData>("1.0")
