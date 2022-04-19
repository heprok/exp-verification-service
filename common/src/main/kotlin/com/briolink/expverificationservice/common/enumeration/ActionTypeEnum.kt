package com.briolink.expverificationservice.common.enumeration

import com.fasterxml.jackson.annotation.JsonValue

enum class ActionTypeEnum(@JsonValue val value: Int) {
    Confirmed(1),
    Rejected(2);

    companion object {
        private val map = values().associateBy(ActionTypeEnum::value)
        fun fromInt(type: Int): ActionTypeEnum = map[type]!!
    }
}
