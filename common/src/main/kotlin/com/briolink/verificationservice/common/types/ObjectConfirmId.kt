package com.briolink.verificationservice.common.types

import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import java.util.UUID

data class ObjectConfirmId(
    val type: ObjectConfirmTypeEnum,
    val id: UUID
) {
    val typeKey: Int
        get() = type.value

    val dbField: String
        get() = type.name
}
