package com.briolink.expverificationservice.updater.handler.usereducation

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.UUID

data class UserEducationEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty("university")
    val universityId: UUID,
    @JsonProperty
    val startDate: LocalDate,
    @JsonProperty
    val endDate: LocalDate? = null,
    @JsonProperty
    val degree: String,
    @JsonProperty
    val userId: UUID
)

data class UserEducationDeletedEventData(
    @JsonProperty("id")
    val id: UUID
)

data class UserEducationCreatedEvent(override val data: UserEducationEventData) : Event<UserEducationEventData>("1.0")
data class UserEducationUpdatedEvent(override val data: UserEducationEventData) : Event<UserEducationEventData>("1.0")
data class UserEducationDeletedEvent(override val data: UserEducationDeletedEventData) : Event<UserEducationDeletedEventData>("1.0")
data class UserEducationSyncEvent(override val data: SyncData<UserEducationEventData>) : SyncEvent<UserEducationEventData>("1.0")
