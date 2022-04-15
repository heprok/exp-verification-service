package com.briolink.verificationservice.updater.handler.usereducation

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.time.LocalDate
import java.util.UUID

enum class EducationStatus(@JsonValue val value: Int) {
    @JsonProperty("1")
    NotConfirmed(1),

    @JsonProperty("2")
    Pending(2),

    @JsonProperty("3")
    Verified(3),

    @JsonProperty("4")
    Rejected(4);
}

data class UserEducationEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
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
