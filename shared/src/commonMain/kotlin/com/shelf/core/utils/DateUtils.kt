package com.shelf.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtils {
    fun nowEpochMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun currentYear(): Int {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    fun epochMillisToLocalDate(epochMillis: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
        return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(timeZone).date
    }

    fun isConsecutiveDay(previous: LocalDate, current: LocalDate): Boolean {
        return previous.toEpochDays() + 1 == current.toEpochDays()
    }

    fun isSameDay(date1: LocalDate, date2: LocalDate): Boolean {
        return date1 == date2
    }
}
