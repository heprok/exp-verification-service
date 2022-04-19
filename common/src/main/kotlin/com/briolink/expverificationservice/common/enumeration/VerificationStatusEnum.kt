package com.briolink.expverificationservice.common.enumeration

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

enum class VerificationStatusEnum(@JsonValue val value: Int) {
    @JsonProperty("1")
    NotConfirmed(1),

    @JsonProperty("2")
    Pending(2),

    @JsonProperty("3")
    Confirmed(3),

    @JsonProperty("4")
    Rejected(4);

    companion object {
        private val map = values().associateBy(VerificationStatusEnum::value)
        fun ofValue(type: Int): VerificationStatusEnum = map[type]!!
    }
}
