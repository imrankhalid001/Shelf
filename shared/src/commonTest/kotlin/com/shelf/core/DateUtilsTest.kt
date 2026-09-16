package com.shelf.core

import com.shelf.core.utils.DateUtils
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateUtilsTest {

    @Test
    fun isConsecutiveDay_consecutiveDates_returnsTrue() {
        val day1 = LocalDate(2026, 3, 29)
        val day2 = LocalDate(2026, 3, 30)
        assertTrue(DateUtils.isConsecutiveDay(day1, day2))
    }

    @Test
    fun isConsecutiveDay_nonConsecutiveDates_returnsFalse() {
        val day1 = LocalDate(2026, 3, 28)
        val day2 = LocalDate(2026, 3, 30)
        assertFalse(DateUtils.isConsecutiveDay(day1, day2))
    }

    @Test
    fun isSameDay_identicalDates_returnsTrue() {
        val day1 = LocalDate(2026, 3, 30)
        val day2 = LocalDate(2026, 3, 30)
        assertTrue(DateUtils.isSameDay(day1, day2))
    }
}
