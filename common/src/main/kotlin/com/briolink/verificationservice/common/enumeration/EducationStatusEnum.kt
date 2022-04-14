package com.briolink.verificationservice.common.enumeration

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

enum class EducationStatusEnum(@JsonValue val value: Int) {
    @JsonProperty("1")
    NotConfirmed(1),

    @JsonProperty("2")
    Pending(2),

    @JsonProperty("3")
    Verified(3),

    @JsonProperty("4")
    Rejected(4);

    companion object {
        private val map = values().associateBy(EducationStatusEnum::value)
        fun ofValue(type: Int): EducationStatusEnum = map[type]!!
    }
}
