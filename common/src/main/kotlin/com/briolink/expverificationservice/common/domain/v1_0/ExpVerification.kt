package com.briolink.expverificationservice.common.domain.v1_0

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.time.Instant
import java.util.UUID

enum class ObjectConfirmType(@JsonValue val value: Int) {
    WorkExperience(1),
    Education(2);

    companion object {
        private val map = values().associateBy(ObjectConfirmType::value)
        fun fromInt(type: Int): ObjectConfirmType = map[type]!!
    }
}

enum class ExpVerificationStatus(@JsonValue val value: Int) {
    NotConfirmed(1),
    Pending(2),
    Confirmed(3),
    Rejected(4);

    companion object {
        private val map = values().associateBy(ExpVerificationStatus::value)
        fun fromInt(type: Int): ExpVerificationStatus = map[type]!!
    }
}

data class ExpVerification(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val userId: UUID,
    @JsonProperty
    val objectConfirmId: UUID,
    @JsonProperty
    val objectConfirmType: ObjectConfirmType,
    @JsonProperty
    val userToConfirmIds: ArrayList<UUID>,
    @JsonProperty
    val actionBy: UUID?,
    @JsonProperty
    val status: ExpVerificationStatus,
    @JsonProperty
    var actionAt: Instant?,
    @JsonProperty
    var created: Instant,
    @JsonProperty
    var changed: Instant?,
) : Domain

data class ExpVerificationChangeStatusEventData(
    @JsonProperty
    val objectConfirmId: UUID,
    @JsonProperty
    val objectConfirmType: ObjectConfirmType,
    @JsonProperty
    val status: ExpVerificationStatus,
) : Domain
