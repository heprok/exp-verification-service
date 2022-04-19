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

enum class VerificationStatus(@JsonValue val value: Int) {
    NotConfirmed(1),
    Pending(2),
    Confirmed(3),
    Rejected(4);

    companion object {
        private val map = values().associateBy(VerificationStatus::value)
        fun fromInt(type: Int): VerificationStatus = map[type]!!
    }
}

data class Verification(
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
    val status: VerificationStatus,
    @JsonProperty
    var actionAt: Instant?,
    @JsonProperty
    var created: Instant,
    @JsonProperty
    var changed: Instant?,
) : Domain
