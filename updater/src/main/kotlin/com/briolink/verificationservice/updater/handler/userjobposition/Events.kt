package com.briolink.verificationservice.updater.handler.userjobposition

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.time.LocalDate
import java.util.UUID

enum class JobPositionStatus(@JsonValue val value: Int) {
    @JsonProperty("1")
    NotConfirmed(1),

    @JsonProperty("2")
    Pending(2),

    @JsonProperty("3")
    Verified(3),

    @JsonProperty("4")
    Rejected(4);
}

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

data class UserJobPositionCreatedEvent(override val data: UserJobPositionEventData) : Event<UserJobPositionEventData>("1.0")
data class UserJobPositionUpdatedEvent(override val data: UserJobPositionEventData) : Event<UserJobPositionEventData>("1.0")
data class UserJobPositionDeletedEvent(override val data: UserJobPositionDeletedEventData) : Event<UserJobPositionDeletedEventData>("1.0")
data class UserJobPositionSyncEvent(override val data: SyncData<UserJobPositionEventData>) : SyncEvent<UserJobPositionEventData>("1.0")
