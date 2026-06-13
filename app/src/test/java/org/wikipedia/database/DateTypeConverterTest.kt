package org.wikipedia.database

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class DateTypeConverterTest {
    private val converter = DateTypeConverter()

    @Test
    fun testFromTimestampReturnsDate() {
        val epoch = 1718236800000L  // 2024-06-13T00:00:00Z
        val result = converter.fromTimestamp(epoch)
        assertNotNull(result)
        assertEquals(epoch, result!!.time)
    }

    @Test
    fun testFromTimestampReturnsNullForNull() {
        val result = converter.fromTimestamp(null)
        assertNull(result)
    }

    @Test
    fun testDateToTimestamp() {
        val date = Date(1718236800000L)
        val result = converter.dateToTimestamp(date)
        assertEquals(1718236800000L, result)
    }

    @Test
    fun testDateToTimestampReturnsNullForNull() {
        val result = converter.dateToTimestamp(null)
        assertNull(result)
    }

    @Test
    fun testRoundTrip() {
        val original = Date(1718236800000L)
        val timestamp = converter.dateToTimestamp(original)
        val restored = converter.fromTimestamp(timestamp)
        assertEquals(original.time, restored!!.time)
    }

    @Test
    fun testFromTimestampWithZero() {
        val result = converter.fromTimestamp(0L)
        assertNotNull(result)
        assertEquals(0L, result!!.time)
        assertEquals(Date(0L), result)
    }
}
