package org.wikipedia.search.db

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class RecentSearchTest {

    @Test
    fun testConstructorWithTextOnly() {
        val recentSearch = RecentSearch("test query")
        assertEquals("test query", recentSearch.text)
        assertNotNull(recentSearch.timestamp)
    }

    @Test
    fun testConstructorWithTextAndTimestamp() {
        val timestamp = Date(1000000L)
        val recentSearch = RecentSearch("search term", timestamp)
        assertEquals("search term", recentSearch.text)
        assertEquals(timestamp, recentSearch.timestamp)
    }

    @Test
    fun testTextIsPrimaryKey() {
        val search1 = RecentSearch("unique text")
        val search2 = RecentSearch("unique text")
        assertEquals(search1.text, search2.text)
    }

    @Test
    fun testDifferentTextsAreDifferent() {
        val search1 = RecentSearch("query one")
        val search2 = RecentSearch("query two")
        assertNotEquals(search1.text, search2.text)
    }

    @Test
    fun testTimestampIsCurrentByDefault() {
        val before = System.currentTimeMillis()
        val recentSearch = RecentSearch("query")
        val after = System.currentTimeMillis()
        assertTrue(recentSearch.timestamp.time in before..after)
    }

    @Test
    fun testEmptyText() {
        val recentSearch = RecentSearch("")
        assertEquals("", recentSearch.text)
    }

    @Test
    fun testSpecialCharactersInText() {
        val recentSearch = RecentSearch("caf\u00e9 & r\u00e9sum\u00e9")
        assertEquals("caf\u00e9 & r\u00e9sum\u00e9", recentSearch.text)
    }
}
