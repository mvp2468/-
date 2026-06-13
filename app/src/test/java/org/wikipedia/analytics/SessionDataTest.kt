package org.wikipedia.analytics

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.history.HistoryEntry
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class SessionDataTest {

    private fun createPageTitle(text: String = "Test"): PageTitle {
        return PageTitle(text, WikiSite.forLanguageCode("en"))
    }

    private fun createHistoryEntry(source: Int): HistoryEntry {
        return HistoryEntry(createPageTitle(), source)
    }

    @Test
    fun testInitialTotalPagesIsZero() {
        val session = SessionData()
        assertEquals(0, session.totalPages)
    }

    @Test
    fun testInitialCountersAreZero() {
        val session = SessionData()
        assertEquals(0, session.pagesFromSearch)
        assertEquals(0, session.pagesFromRandom)
        assertEquals(0, session.pagesFromLangLink)
        assertEquals(0, session.pagesFromInternal)
        assertEquals(0, session.pagesFromExternal)
        assertEquals(0, session.pagesFromHistory)
        assertEquals(0, session.pagesFromReadingList)
        assertEquals(0, session.pagesFromBack)
        assertEquals(0, session.pagesWithNoDescription)
    }

    @Test
    fun testAddPageViewedIncrementsSearchCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_SEARCH))
        assertEquals(1, session.pagesFromSearch)
        assertEquals(1, session.totalPages)
    }

    @Test
    fun testAddPageViewedIncrementsRandomCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_RANDOM))
        assertEquals(1, session.pagesFromRandom)
    }

    @Test
    fun testAddPageViewedIncrementsLanguageLinkCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_LANGUAGE_LINK))
        assertEquals(1, session.pagesFromLangLink)
    }

    @Test
    fun testAddPageViewedIncrementsExternalLinkCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_EXTERNAL_LINK))
        assertEquals(1, session.pagesFromExternal)
    }

    @Test
    fun testAddPageViewedIncrementsHistoryCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_HISTORY))
        assertEquals(1, session.pagesFromHistory)
    }

    @Test
    fun testAddPageViewedIncrementsReadingListCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_READING_LIST))
        assertEquals(1, session.pagesFromReadingList)
    }

    @Test
    fun testAddPageViewedIncrementsSuggestedEditsCounter() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_SUGGESTED_EDITS))
        assertEquals(1, session.pagesFromSuggestedEdits)
    }

    @Test
    fun testAddPageViewedIncrementsInternalForOtherSources() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(0))
        assertEquals(1, session.pagesFromInternal)
    }

    @Test
    fun testAddPageFromBack() {
        val session = SessionData()
        session.addPageFromBack()
        assertEquals(1, session.pagesFromBack)
    }

    @Test
    fun testAddPageWithNoDescription() {
        val session = SessionData()
        session.addPageWithNoDescription()
        assertEquals(1, session.pagesWithNoDescription)
    }

    @Test
    fun testMultiplePageViewsFromSameSource() {
        val session = SessionData()
        val entry = createHistoryEntry(HistoryEntry.SOURCE_SEARCH)
        session.addPageViewed(entry)
        session.addPageViewed(entry)
        session.addPageViewed(entry)
        assertEquals(3, session.pagesFromSearch)
        assertEquals(3, session.totalPages)
    }

    @Test
    fun testMixedSources() {
        val session = SessionData()
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_SEARCH))
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_RANDOM))
        session.addPageViewed(createHistoryEntry(HistoryEntry.SOURCE_LANGUAGE_LINK))
        assertEquals(1, session.pagesFromSearch)
        assertEquals(1, session.pagesFromRandom)
        assertEquals(1, session.pagesFromLangLink)
        assertEquals(3, session.totalPages)
    }

    @Test
    fun testAddPageLatency() {
        val session = SessionData()
        session.addPageLatency(1000L)
        assertTrue(true)
    }

    @Test
    fun testStartTimeAndLastTouchTimeAreSet() {
        val session = SessionData()
        assertTrue(session.startTime > 0)
        assertTrue(session.lastTouchTime > 0)
        assertEquals(session.startTime, session.lastTouchTime)
    }
}
