package org.wikipedia.database

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class LocalDateTimeTypeConverterTest {
    private val converter = LocalDateTimeTypeConverter()

    @Test
    fun testFromTimestampReturnsNullForNull() {
        val result = converter.fromTimestamp(null)
        assertNull(result)
    }

    @Test
    fun testDateToTimestampReturnsNullForNull() {
        val result = converter.dateToTimestamp(null)
        assertNull(result)
    }

    @Test
    fun testRoundTrip() {
        val original = LocalDateTime.of(2024, 6, 13, 12, 0, 0)
        val timestamp = converter.dateToTimestamp(original)
        val restored = converter.fromTimestamp(timestamp)
        assertEquals(original, restored)
    }

    @Test
    fun testRoundTripWithMilliseconds() {
        val original = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 123000000)
        val timestamp = converter.dateToTimestamp(original)
        val restored = converter.fromTimestamp(timestamp)
        assertEquals(original, restored)
    }

    @Test
    fun testEpochRoundTrip() {
        val epoch = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
        val timestamp = converter.dateToTimestamp(epoch)
        assertEquals(0L, timestamp)
        val restored = converter.fromTimestamp(timestamp)
        assertEquals(epoch, restored)
    }
}
