package com.briolink.expverificationservice.common.types

import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId

data class YearRange(val start: Year? = null, val end: Year? = null) {
    val startDate: LocalDate?
        get() = start?.let { LocalDate.of(it.value, 1, 1) }

    val endDate: LocalDate?
        get() = end?.let { LocalDate.of(it.value, 12, 31) }

    val endDateInstant: Instant?
        get() = endDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()

    val startDateInstant: Instant?
        get() = startDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
}
