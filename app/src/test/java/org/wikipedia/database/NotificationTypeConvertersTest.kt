package org.wikipedia.database

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationTypeConvertersTest {

    private val converter = NotificationTypeConverters()

    @Test
    fun testStringToContentsNull() {
        assertNull(converter.stringToContents(null))
    }

    @Test
    fun testStringToContentsEmpty() {
        assertNull(converter.stringToContents(""))
    }

    @Test
    fun testStringToContentsValid() {
        val json = """{"header":"Test Header","compactHeader":"Compact","body":"Test body"}"""
        val result = converter.stringToContents(json)
        assertNotNull(result)
        assertEquals("Test Header", result!!.header)
        assertEquals("Compact", result.compactHeader)
        assertEquals("Test body", result.body)
    }

    @Test
    fun testContentsToStringNull() {
        assertNull(converter.contentsToString(null))
    }

    @Test
    fun testContentsToStringRoundTrip() {
        val json = """{"header":"A","compactHeader":"B","body":"C"}"""
        val parsed = converter.stringToContents(json)
        val serialized = converter.contentsToString(parsed)
        assertNotNull(serialized)
        // Verify round trip works by parsing again
        val reparsed = converter.stringToContents(serialized)
        assertNotNull(reparsed)
        assertEquals(parsed?.header, reparsed?.header)
        assertEquals(parsed?.compactHeader, reparsed?.compactHeader)
        assertEquals(parsed?.body, reparsed?.body)
    }

    @Test
    fun testStringToTimestampNull() {
        assertNull(converter.stringToTimestamp(null))
    }

    @Test
    fun testStringToTimestampEmpty() {
        assertNull(converter.stringToTimestamp(""))
    }

    @Test
    fun testStringToTimestampValid() {
        val json = """{"utciso8601":"2024-01-15T10:30:00Z"}"""
        val result = converter.stringToTimestamp(json)
        assertNotNull(result)
        assertEquals("2024-01-15T10:30:00Z", result!!.utciso8601)
    }

    @Test
    fun testTimestampToStringNull() {
        assertNull(converter.timestampToString(null))
    }

    @Test
    fun testTimestampToStringRoundTrip() {
        val json = """{"utciso8601":"2024-01-15T10:30:00Z"}"""
        val parsed = converter.stringToTimestamp(json)
        val serialized = converter.timestampToString(parsed)
        assertNotNull(serialized)
        val reparsed = converter.stringToTimestamp(serialized)
        assertEquals(parsed?.utciso8601, reparsed?.utciso8601)
    }

    @Test
    fun testStringToTitleNull() {
        assertNull(converter.stringToTitle(null))
    }

    @Test
    fun testStringToTitleEmpty() {
        assertNull(converter.stringToTitle(""))
    }

    @Test
    fun testStringToTitleValid() {
        val json = """{"full":"Article Title","text":"Article_Title"}"""
        val result = converter.stringToTitle(json)
        assertNotNull(result)
        assertEquals("Article Title", result!!.full)
        assertEquals("Article_Title", result.text)
    }

    @Test
    fun testTitleToStringNull() {
        assertNull(converter.titleToString(null))
    }

    @Test
    fun testTitleToStringRoundTrip() {
        val json = """{"full":"Some Title","text":"Some_Title"}"""
        val parsed = converter.stringToTitle(json)
        val serialized = converter.titleToString(parsed)
        assertNotNull(serialized)
        val reparsed = converter.stringToTitle(serialized)
        assertEquals(parsed?.full, reparsed?.full)
        assertEquals(parsed?.text, reparsed?.text)
    }

    @Test
    fun testStringToAgentNull() {
        assertNull(converter.stringToAgent(null))
    }

    @Test
    fun testStringToAgentEmpty() {
        assertNull(converter.stringToAgent(""))
    }

    @Test
    fun testStringToAgentValid() {
        val json = """{"name":"TestUser"}"""
        val result = converter.stringToAgent(json)
        assertNotNull(result)
        assertEquals("TestUser", result!!.name)
    }

    @Test
    fun testAgentToStringNull() {
        assertNull(converter.agentToString(null))
    }

    @Test
    fun testAgentToStringRoundTrip() {
        val json = """{"name":"Editor42"}"""
        val parsed = converter.stringToAgent(json)
        val serialized = converter.agentToString(parsed)
        assertNotNull(serialized)
        val reparsed = converter.stringToAgent(serialized)
        assertEquals(parsed?.name, reparsed?.name)
    }

    @Test
    fun testStringToContentsInvalidJson() {
        val result = converter.stringToContents("{invalid json}")
        assertNull(result)
    }

    @Test
    fun testStringToTimestampInvalidJson() {
        val result = converter.stringToTimestamp("not-json")
        assertNull(result)
    }

    @Test
    fun testStringToTitleInvalidJson() {
        val result = converter.stringToTitle("{broken")
        assertNull(result)
    }

    @Test
    fun testStringToAgentInvalidJson() {
        val result = converter.stringToAgent("[bad]")
        assertNull(result)
    }
}
