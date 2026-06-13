package org.wikipedia.search

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.history.HistoryEntry
import org.wikipedia.page.Namespace
import org.wikipedia.page.PageTitle
import org.wikipedia.readinglist.database.ReadingListPage

@RunWith(RobolectricTestRunner::class)
class SearchResultLongPressHandlerTest {

    private val callback = mockk<SearchResultCallback>(relaxed = true)
    private lateinit var handler: SearchResultLongPressHandler
    private lateinit var pageTitle: PageTitle
    private lateinit var historyEntry: HistoryEntry

    @Before
    fun setUp() {
        handler = SearchResultLongPressHandler(callback, 42)
        val wikiSite = WikiSite.forLanguageCode("en")
        pageTitle = PageTitle("Test_Page", wikiSite)
        historyEntry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_SEARCH)
    }

    @Test
    fun testOnOpenLinkNavigatesToTitle() {
        handler.onOpenLink(historyEntry)
        verify { callback.navigateToTitle(pageTitle, false, 42) }
    }

    @Test
    fun testOnOpenInNewTabNavigatesToTitleInNewTab() {
        handler.onOpenInNewTab(historyEntry)
        verify { callback.navigateToTitle(pageTitle, true, 42) }
    }

    @Test
    fun testOnAddRequestAddsToDefaultList() {
        handler.onAddRequest(historyEntry, addToDefault = true)
        verify { callback.onSearchAddPageToList(historyEntry, true) }
    }

    @Test
    fun testOnAddRequestAddsToNonDefaultList() {
        handler.onAddRequest(historyEntry, addToDefault = false)
        verify { callback.onSearchAddPageToList(historyEntry, false) }
    }

    @Test
    fun testOnMoveRequestMovesPage() {
        val wikiSite = WikiSite.forLanguageCode("en")
        val pt = PageTitle("Test_Page", wikiSite)
        val readingListPage = ReadingListPage(pt).apply { listId = 100L }
        handler.onMoveRequest(readingListPage, historyEntry)
        verify { callback.onSearchMovePageToList(100L, historyEntry) }
    }

    @Test
    fun testOnOpenLinkWithNullCallback() {
        val nullHandler = SearchResultLongPressHandler(null, 42)
        nullHandler.onOpenLink(historyEntry)
    }

    @Test
    fun testOnOpenInNewTabWithNullCallback() {
        val nullHandler = SearchResultLongPressHandler(null, 42)
        nullHandler.onOpenInNewTab(historyEntry)
    }

    @Test
    fun testOnAddRequestWithNullCallback() {
        val nullHandler = SearchResultLongPressHandler(null, 42)
        nullHandler.onAddRequest(historyEntry, true)
    }
}
