package org.wikipedia.search

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class StandardSearchResultsTest {

    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testEmptyResults() {
        val results = StandardSearchResults(
            results = emptyList(),
            continuation = null
        )
        assertTrue(results.results.isEmpty())
        assertNull(results.continuation)
        assertNull(results.xSearchIdPrefix)
        assertNull(results.xSearchIdFullText)
    }

    @Test
    fun testWithResults() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val result = SearchResult(pageTitle, SearchResult.SearchResultType.PREFIX)
        val results = StandardSearchResults(
            results = listOf(result),
            continuation = 0
        )
        assertEquals(1, results.results.size)
        assertEquals(0, results.continuation)
        assertEquals("Test_Page", results.results[0].pageTitle.prefixedText)
    }

    @Test
    fun testWithAllFields() {
        val pageTitle = PageTitle("Page", wikiSite)
        val result = SearchResult(pageTitle, SearchResult.SearchResultType.FULL_TEXT)
        val results = StandardSearchResults(
            results = listOf(result),
            continuation = 10,
            xSearchIdPrefix = "prefix-id-123",
            xSearchIdFullText = "fulltext-id-456"
        )
        assertEquals(1, results.results.size)
        assertEquals(10, results.continuation)
        assertEquals("prefix-id-123", results.xSearchIdPrefix)
        assertEquals("fulltext-id-456", results.xSearchIdFullText)
        assertEquals(SearchResult.SearchResultType.FULL_TEXT, results.results[0].type)
    }

    @Test
    fun testContinuationNull() {
        val results = StandardSearchResults(
            results = emptyList(),
            continuation = null,
            xSearchIdPrefix = "id1"
        )
        assertNull(results.continuation)
        assertEquals("id1", results.xSearchIdPrefix)
        assertNull(results.xSearchIdFullText)
    }

    @Test
    fun testMultipleResults() {
        val result1 = SearchResult(PageTitle("First", wikiSite), SearchResult.SearchResultType.PREFIX)
        val result2 = SearchResult(PageTitle("Second", wikiSite), SearchResult.SearchResultType.FULL_TEXT)
        val result3 = SearchResult(PageTitle("Third", wikiSite), SearchResult.SearchResultType.PREFIX)

        val results = StandardSearchResults(
            results = listOf(result1, result2, result3),
            continuation = 30
        )
        assertEquals(3, results.results.size)
        assertEquals("First", results.results[0].pageTitle.prefixedText)
        assertEquals("Second", results.results[1].pageTitle.prefixedText)
        assertEquals("Third", results.results[2].pageTitle.prefixedText)
    }

    @Test
    fun testXSearchIdsAreNullable() {
        val results = StandardSearchResults(
            results = listOf(SearchResult(PageTitle("Test", wikiSite))),
            continuation = null
        )
        assertNull(results.xSearchIdPrefix)
        assertNull(results.xSearchIdFullText)
    }

    @Test
    fun testEquality() {
        val r1 = StandardSearchResults(
            results = emptyList(),
            continuation = 5,
            xSearchIdPrefix = "abc"
        )
        val r2 = StandardSearchResults(
            results = emptyList(),
            continuation = 5,
            xSearchIdPrefix = "abc"
        )
        assertEquals(r1, r2)
        assertEquals(r1.hashCode(), r2.hashCode())
    }

    @Test
    fun testNotEqualDifferentContinuation() {
        val r1 = StandardSearchResults(emptyList(), 5)
        val r2 = StandardSearchResults(emptyList(), 6)
        assertNotEquals(r1, r2)
    }
}
