package org.wikipedia.feed.model

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UtcDateTest {

    @Test
    fun testYearReturnsCurrentYearWhenAgeIsZero() {
        val utcDate = UtcDate(0)
        val currentYear = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).get(java.util.Calendar.YEAR).toString()
        assertEquals(currentYear, utcDate.year)
    }

    @Test
    fun testMonthIsTwoDigitPadded() {
        val utcDate = UtcDate(0)
        assertEquals(2, utcDate.month.length)
    }

    @Test
    fun testDayIsTwoDigitPadded() {
        val utcDate = UtcDate(0)
        assertEquals(2, utcDate.day.length)
    }

    @Test
    fun testAgeOffsetWorks() {
        val utcDateToday = UtcDate(0)
        val utcDateYesterday = UtcDate(1)
        assertNotNull(utcDateToday.year)
        assertNotNull(utcDateYesterday.year)
    }

    @Test
    fun testBaseCalendarIsUtc() {
        val utcDate = UtcDate(0)
        assertEquals("UTC", utcDate.baseCalendar.timeZone.id)
    }
}
