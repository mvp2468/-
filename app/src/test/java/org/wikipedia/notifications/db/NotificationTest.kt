package org.wikipedia.notifications.db

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationTest {

    @Test
    fun testIsUnreadWhenReadIsNull() {
        val notification = Notification(id = 1, read = null)
        assertTrue(notification.isUnread)
    }

    @Test
    fun testIsUnreadWhenReadIsEmpty() {
        val notification = Notification(id = 1, read = "")
        assertTrue(notification.isUnread)
    }

    @Test
    fun testIsUnreadWhenReadIsPresent() {
        val notification = Notification(id = 1, read = "2024-01-01")
        assertFalse(notification.isUnread)
    }

    @Test
    fun testKeyCombinesIdAndWiki() {
        val notification = Notification(id = 42, wiki = "enwiki")
        assertEquals(42L + "enwiki".hashCode().toLong(), notification.key())
    }

    @Test
    fun testIsFromWikidataWhenWikiIsWikidataDbName() {
        val notification = Notification(id = 1, wiki = "wikidatawiki")
        assertEquals(notification.wiki == "wikidatawiki", notification.isFromWikidata)
    }

    @Test
    fun testIsFromWikidataFalseForOtherWikis() {
        val notification = Notification(id = 1, wiki = "enwiki")
        assertFalse(notification.isFromWikidata)
    }

    @Test
    fun testToStringReturnsIdString() {
        val notification = Notification(id = 123)
        assertEquals("123", notification.toString())
    }

    @Test
    fun testUtcIso8601WhenTimestampIsNull() {
        val notification = Notification(id = 1)
        assertEquals("", notification.utcIso8601)
    }

    @Test
    fun testDateWithoutTimestampReturnsCurrentDate() {
        val notification = Notification(id = 1)
        assertNotNull(notification.date())
    }

    @Test
    fun testTitleIsMainNamespaceTrueForMainCode() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"full":"Page Title","text":"Page Title","namespace-key":0}"""
        val title = json.decodeFromString<Notification.Title>(jsonStr)
        assertTrue(title.isMainNamespace)
    }

    @Test
    fun testTitleIsMainNamespaceFalseForOtherCode() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"full":"Talk:Test","text":"Test","namespace-key":1}"""
        val title = json.decodeFromString<Notification.Title>(jsonStr)
        assertFalse(title.isMainNamespace)
    }

    @Test
    fun testAgentDeserialization() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"id":1,"name":"TestUser"}"""
        val agent = json.decodeFromString<Notification.Agent>(jsonStr)
        assertEquals("TestUser", agent.name)
    }

    @Test
    fun testLinkIconReturnsEmptyWhenNoIcon() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"description":"test","url":"https://example.com"}"""
        val link = json.decodeFromString<Notification.Link>(jsonStr)
        assertEquals("", link.icon())
    }

    @Test
    fun testLinkUrlProtocolResolution() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"description":"test","url":"//en.wikipedia.org/wiki/Test"}"""
        val link = json.decodeFromString<Notification.Link>(jsonStr)
        assertTrue(link.url.startsWith("https://"))
    }

    @Test
    fun testSourceUrlDecoded() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"title":"Test","url":"https://en.wikipedia.org/wiki/Special%3ASearch"}"""
        val source = json.decodeFromString<Notification.Source>(jsonStr)
        assertFalse(source.url.contains("%3A"))
    }
}
