package com.briolink.verificationservice.updater.handler.user

import com.briolink.lib.event.Event
import com.briolink.lib.location.model.LocationId
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.time.LocalDate
import java.util.UUID

data class UserEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val slug: String,
    @JsonProperty
    val firstName: String,
    @JsonProperty
    val lastName: String,
    @JsonProperty
    val personalEmail: String,
    @JsonProperty
    val description: String? = null,
    @JsonProperty
    val locationId: LocationId? = null,
    @JsonProperty
    val image: URL? = null,
    @JsonProperty
    val twitter: String? = null,
    @JsonProperty
    val facebook: String? = null,
    @JsonProperty
    val confirmed: Boolean,
)

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
    val isCurrent: Boolean,
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

data class UserCreatedEvent(override val data: UserEventData) : Event<UserEventData>("1.0")
data class UserUpdatedEvent(override val data: UserEventData) : Event<UserEventData>("1.0")
data class UserSyncEvent(override val data: SyncData<UserEventData>) : SyncEvent<UserEventData>("1.0")
data class UserJobPositionEvent(override val data: UserJobPositionEventData) : Event<UserJobPositionEventData>("1.0")
data class UserJobPositionDeletedEvent(override val data: UserJobPositionDeletedEventData) : Event<UserJobPositionDeletedEventData>("1.0")
data class UserJobPositionSyncEvent(override val data: SyncData<UserJobPositionEventData>) : SyncEvent<UserJobPositionEventData>("1.0")
