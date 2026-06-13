package org.wikipedia.extensions

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class DateExtensionsTest {

    @Test
    fun testToLocalDate() {
        val date = Date.from(
            LocalDate.of(2024, 6, 15)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val localDate = date.toLocalDate()
        assertEquals(2024, localDate.year)
        assertEquals(6, localDate.monthValue)
        assertEquals(15, localDate.dayOfMonth)
    }

    @Test
    fun testIsToday() {
        val today = Date()
        assertTrue(today.isToday())
    }

    @Test
    fun testIsNotToday() {
        val yesterday = Date.from(
            LocalDate.now().minusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        assertFalse(yesterday.isToday())
    }

    @Test
    fun testIsYesterdayWhenActuallyYesterday() {
        val yesterday = Date.from(
            LocalDate.now().minusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        assertTrue(yesterday.isYesterday())
    }

    @Test
    fun testIsYesterdayWhenToday() {
        val today = Date()
        assertFalse(today.isYesterday())
    }

    @Test
    fun testIsYesterdayWhenTwoDaysAgo() {
        val twoDaysAgo = Date.from(
            LocalDate.now().minusDays(2)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        assertFalse(twoDaysAgo.isYesterday())
    }
}