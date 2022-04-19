package com.briolink.expverificationservice.common.types

import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
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
