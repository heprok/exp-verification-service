package com.briolink.expverificationservice.common.enumeration

import com.fasterxml.jackson.annotation.JsonValue

enum class ObjectConfirmTypeEnum(@JsonValue val value: Int) {
    WorkExperience(1),
    Education(2);

    companion object {
        private val map = values().associateBy(ObjectConfirmTypeEnum::value)
        fun fromInt(type: Int): ObjectConfirmTypeEnum = map[type]!!
    }
}
