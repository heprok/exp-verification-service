package com.briolink.expverificationservice.common.types

enum class SortDirectionEnum {
    ASC,
    DESC
}

data class SortParameter<T>(
    val key: T,
    val direction: SortDirectionEnum,
)
