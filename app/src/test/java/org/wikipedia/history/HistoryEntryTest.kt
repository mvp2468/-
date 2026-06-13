package org.wikipedia.history

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.PageTitle
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class HistoryEntryTest {

    @Test
    fun testPageTitlePropertiesAreStored() {
        val pageTitle = getPageTitle()
        val historyEntry = HistoryEntry(pageTitle, 0)
        assertEquals(TITLE, historyEntry.apiTitle)
        assertEquals(DISPLAY_TEXT, historyEntry.displayTitle)
        assertEquals(NAMESPACE, historyEntry.namespace)
        assertEquals(WIKI_SITE.authority(), historyEntry.authority)
        assertEquals(WIKI_SITE.languageCode, historyEntry.lang)
        assertEquals(pageTitle, historyEntry.title)
    }

    @Test
    fun testConstructorWithTimestamp() {
        val pageTitle = getPageTitle()
        val timestamp = Date(1609459200000L)
        val historyEntry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_SEARCH, timestamp)
        assertEquals(timestamp, historyEntry.timestamp)
        assertEquals(HistoryEntry.SOURCE_SEARCH, historyEntry.source)
        assertEquals(DISPLAY_TEXT, historyEntry.displayTitle)
    }

    @Test
    fun testConstructorWithWikiSite() {
        val pageTitle = getPageTitle()
        val historyEntry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_INTERNAL_LINK)
        assertEquals(WIKI_SITE.authority(), historyEntry.authority)
        assertEquals(WIKI_SITE.languageCode, historyEntry.lang)
    }

    @Test
    fun testDefaultConstructor() {
        val entry = HistoryEntry(id = 100)
        assertEquals(100, entry.id)
        assertEquals("", entry.apiTitle)
        assertEquals("", entry.displayTitle)
        assertEquals("", entry.authority)
        assertEquals("", entry.lang)
        assertEquals("", entry.namespace)
        assertEquals(HistoryEntry.SOURCE_INTERNAL_LINK, entry.source)
    }

    @Test
    fun testTitleGetterReturnsSamePageTitle() {
        val pageTitle = getPageTitle()
        val historyEntry = HistoryEntry(pageTitle, 0)
        val title = historyEntry.title
        assertEquals(pageTitle.text, title.text)
        assertEquals(pageTitle.displayText, title.displayText)
    }

    @Test
    fun testTitleGetterFromFields() {
        val entry = HistoryEntry(
            authority = WIKI_SITE.authority(),
            lang = WIKI_SITE.languageCode,
            apiTitle = "Some_Title",
            displayTitle = "Some Title",
            namespace = ""
        )
        assertEquals("Some Title", entry.title.displayText)
        assertEquals("Some_Title", entry.title.text)
    }

    @Test
    fun testReferrerDefaultNull() {
        val pageTitle = getPageTitle()
        val historyEntry = HistoryEntry(pageTitle, 0)
        assertNull(historyEntry.referrer)
    }

    @Test
    fun testReferrerCanBeSet() {
        val pageTitle = getPageTitle()
        val historyEntry = HistoryEntry(pageTitle, 0)
        historyEntry.referrer = "test_referrer"
        assertEquals("test_referrer", historyEntry.referrer)
    }

    @Test
    fun testSourceConstants() {
        assertEquals(1, HistoryEntry.SOURCE_SEARCH)
        assertEquals(2, HistoryEntry.SOURCE_INTERNAL_LINK)
        assertEquals(3, HistoryEntry.SOURCE_EXTERNAL_LINK)
        assertEquals(4, HistoryEntry.SOURCE_HISTORY)
        assertEquals(6, HistoryEntry.SOURCE_LANGUAGE_LINK)
        assertEquals(7, HistoryEntry.SOURCE_RANDOM)
        assertEquals(8, HistoryEntry.SOURCE_MAIN_PAGE)
        assertEquals(9, HistoryEntry.SOURCE_PLACES)
        assertEquals(25, HistoryEntry.SOURCE_NOTIFICATION)
        assertEquals(29, HistoryEntry.SOURCE_WIDGET)
        assertEquals(30, HistoryEntry.SOURCE_SUGGESTED_EDITS)
        assertEquals(32, HistoryEntry.SOURCE_WATCHLIST)
        assertEquals(45, HistoryEntry.SOURCE_ACTIVITY_TAB)
    }

    @Test
    fun testConstructorWithDifferentSources() {
        val sources = listOf(
            HistoryEntry.SOURCE_SEARCH,
            HistoryEntry.SOURCE_INTERNAL_LINK,
            HistoryEntry.SOURCE_EXTERNAL_LINK,
            HistoryEntry.SOURCE_RANDOM,
            HistoryEntry.SOURCE_MAIN_PAGE,
            HistoryEntry.SOURCE_WIDGET
        )
        sources.forEach { source ->
            val pageTitle = getPageTitle()
            val entry = HistoryEntry(pageTitle, source)
            assertEquals(source, entry.source)
        }
    }

    @Test
    fun testTimestampIsSetByConstructor() {
        val pageTitle = getPageTitle()
        val before = Date()
        val historyEntry = HistoryEntry(pageTitle, 0)
        val after = Date()
        // Timestamp should be within the range of when we called the constructor
        org.junit.Assert.assertTrue(historyEntry.timestamp >= before || historyEntry.timestamp <= after)
    }

    companion object {
        private val WIKI_SITE = WikiSite.forLanguageCode("en")
        private const val TITLE = "TITLE"
        private const val DISPLAY_TEXT = "DISPLAY_TEXT"
        private const val NAMESPACE = "NAMESPACE"
        private const val DESCRIPTION = "DESCRIPTION"

        fun getPageTitle(): PageTitle {
            val pageTitle = PageTitle(
                NAMESPACE, TITLE, WIKI_SITE
            )
            pageTitle.displayText = DISPLAY_TEXT
            pageTitle.description = DESCRIPTION

            return pageTitle
        }
    }
}
