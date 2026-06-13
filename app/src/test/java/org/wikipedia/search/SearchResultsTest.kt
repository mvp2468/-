package org.wikipedia.search

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.dataclient.mwapi.MwQueryPage
import org.wikipedia.dataclient.mwapi.MwQueryResponse
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class SearchResultsTest {

    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testEmptySearchResults() {
        val searchResults = SearchResults()

        assertTrue(searchResults.results.isEmpty())
        assertNull(searchResults.continuation)
    }

    @Test
    fun testSearchResultsWithSearchResults() {
        val result1 = SearchResult(PageTitle("Page One", wikiSite), SearchResult.SearchResultType.PREFIX)
        val result2 = SearchResult(PageTitle("Page Two", wikiSite), SearchResult.SearchResultType.FULL_TEXT)

        val searchResults = SearchResults(mutableListOf(result1, result2))

        assertEquals(2, searchResults.results.size)
        assertEquals("Page One", searchResults.results[0].pageTitle.displayText)
        assertEquals("Page Two", searchResults.results[1].pageTitle.displayText)
    }

    @Test
    fun testSearchResultsMutableList() {
        val searchResults = SearchResults()

        searchResults.results.add(
            SearchResult(
                PageTitle("Added Title", wikiSite),
                SearchResult.SearchResultType.HISTORY
            )
        )

        assertEquals(1, searchResults.results.size)
        assertEquals("Added Title", searchResults.results[0].pageTitle.displayText)
    }

    @Test
    fun testSearchResultsOrderPreserved() {
        val result1 = SearchResult(PageTitle("First", wikiSite))
        val result2 = SearchResult(PageTitle("Second", wikiSite))
        val result3 = SearchResult(PageTitle("Third", wikiSite))

        val searchResults = SearchResults(mutableListOf(result1, result2, result3))

        assertEquals(3, searchResults.results.size)
        assertEquals("First", searchResults.results[0].pageTitle.displayText)
        assertEquals("Second", searchResults.results[1].pageTitle.displayText)
        assertEquals("Third", searchResults.results[2].pageTitle.displayText)
    }

    @Test
    fun testSearchResultsRemove() {
        val result1 = SearchResult(PageTitle("Keep", wikiSite))
        val result2 = SearchResult(PageTitle("Remove", wikiSite))

        val searchResults = SearchResults(mutableListOf(result1, result2))
        searchResults.results.removeAt(1)

        assertEquals(1, searchResults.results.size)
        assertEquals("Keep", searchResults.results[0].pageTitle.displayText)
    }

    @Test
    fun testSearchResultsClear() {
        val result1 = SearchResult(PageTitle("Page 1", wikiSite))
        val result2 = SearchResult(PageTitle("Page 2", wikiSite))

        val searchResults = SearchResults(mutableListOf(result1, result2))
        searchResults.results.clear()

        assertTrue(searchResults.results.isEmpty())
    }

    @Test
    fun testSearchResultsToString() {
        val result1 = SearchResult(PageTitle("Test Page", wikiSite))
        val searchResults = SearchResults(mutableListOf(result1))

        val str = searchResults.toString()
        assertTrue(str.contains("Test_Page"))
    }

    @Test
    fun testConstructorFromMwQueryPagesEmpty() {
        val searchResults = SearchResults(emptyList(), wikiSite, null)
        assertTrue(searchResults.results.isEmpty())
        assertNull(searchResults.continuation)
    }

    @Test
    fun testConstructorFromMwQueryPagesSortedByIndex() {
        val page1 = mockk<MwQueryPage>()
        every { page1.title } returns "Z_Page"
        every { page1.index } returns 3
        every { page1.coordinates } returns null
        every { page1.thumbUrl() } returns null
        every { page1.description } returns null
        every { page1.displayTitle(any()) } returns "Z_Page"
        every { page1.sectionTitle } returns null
        every { page1.redirectFrom } returns null
        every { page1.snippet } returns null

        val page2 = mockk<MwQueryPage>()
        every { page2.title } returns "A_Page"
        every { page2.index } returns 1
        every { page2.coordinates } returns null
        every { page2.thumbUrl() } returns null
        every { page2.description } returns null
        every { page2.displayTitle(any()) } returns "A_Page"
        every { page2.sectionTitle } returns null
        every { page2.redirectFrom } returns null
        every { page2.snippet } returns null

        val page3 = mockk<MwQueryPage>()
        every { page3.title } returns "M_Page"
        every { page3.index } returns 2
        every { page3.coordinates } returns null
        every { page3.thumbUrl() } returns null
        every { page3.description } returns null
        every { page3.displayTitle(any()) } returns "M_Page"
        every { page3.sectionTitle } returns null
        every { page3.redirectFrom } returns null
        every { page3.snippet } returns null

        val searchResults = SearchResults(listOf(page1, page2, page3), wikiSite, null)

        assertEquals(3, searchResults.results.size)
        // Should be sorted by index: 1, 2, 3
        assertEquals("A_Page", searchResults.results[0].pageTitle.prefixedText)
        assertEquals("M_Page", searchResults.results[1].pageTitle.prefixedText)
        assertEquals("Z_Page", searchResults.results[2].pageTitle.prefixedText)
    }

    @Test
    fun testConstructorFromMwQueryPagesWithContinuation() {
        val continuation = mockk<MwQueryResponse.Continuation>(relaxed = true)
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Page"
        every { page.index } returns 0
        every { page.coordinates } returns null
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle(any()) } returns "Page"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val searchResults = SearchResults(listOf(page), wikiSite, continuation)

        assertEquals(1, searchResults.results.size)
        assertNotNull(searchResults.continuation)
        assertSame(continuation, searchResults.continuation)
    }

    @Test
    fun testConstructorFromMwQueryPagesWithCoordinates() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Place_Page"
        every { page.index } returns 0
        every { page.coordinates } returns listOf(MwQueryPage.Coordinates(1.0, 2.0))
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle(any()) } returns "Place_Page"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val searchResults = SearchResults(listOf(page), wikiSite, null)

        assertEquals(1, searchResults.results.size)
        assertNotNull(searchResults.results[0].coordinates)
        assertEquals(1.0, searchResults.results[0].coordinates!![0].lat, 0.001)
    }

    @Test
    fun testConstructorFromMwQueryPagesIndexInApiCall() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Page_1"
        every { page.index } returns 5
        every { page.coordinates } returns null
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle(any()) } returns "Page_1"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val searchResults = SearchResults(listOf(page), wikiSite, null)

        assertEquals(1, searchResults.results.size)
        assertEquals(5, searchResults.results[0].indexInApiCall)
    }
}
