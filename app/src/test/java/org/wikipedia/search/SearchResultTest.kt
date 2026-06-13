package org.wikipedia.search

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.dataclient.mwapi.MwQueryPage
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class SearchResultTest {

    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testPrimaryConstructorWithAllFields() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val result = SearchResult(
            pageTitle = pageTitle,
            redirectFrom = "Old_Page",
            type = SearchResult.SearchResultType.PREFIX,
            snippet = "test snippet",
            indexInApiCall = 5
        )
        assertEquals("Test Page", result.pageTitle.displayText)
        assertEquals("Old_Page", result.redirectFrom)
        assertEquals(SearchResult.SearchResultType.PREFIX, result.type)
        assertEquals("test snippet", result.snippet)
        assertEquals(5, result.indexInApiCall)
    }

    @Test
    fun testSecondaryConstructorWithPageTitleAndType() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val result = SearchResult(pageTitle, SearchResult.SearchResultType.FULL_TEXT, snippet = "snip", indexInApiCall = 3)
        assertNull(result.redirectFrom)
        assertNull(result.coordinates)
        assertEquals(SearchResult.SearchResultType.FULL_TEXT, result.type)
        assertEquals("snip", result.snippet)
        assertEquals(3, result.indexInApiCall)
    }

    @Test
    fun testSecondaryConstructorDefaults() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val result = SearchResult(pageTitle)
        assertNull(result.redirectFrom)
        assertEquals(SearchResult.SearchResultType.PREFIX, result.type)
        assertNull(result.coordinates)
        assertNull(result.snippet)
        assertEquals(0, result.indexInApiCall)
    }

    @Test
    fun testLocationNullWhenNoCoordinates() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val result = SearchResult(pageTitle)
        assertNull(result.location)
    }

    @Test
    fun testLocationNullWhenEmptyCoordinates() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val result = SearchResult(pageTitle, null, SearchResult.SearchResultType.PREFIX, emptyList())
        assertNull(result.location)
    }

    @Test
    fun testLocationWithCoordinates() {
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val coord = MwQueryPage.Coordinates(lat = 40.7128, lon = -74.0060)
        val result = SearchResult(
            pageTitle = pageTitle,
            redirectFrom = null,
            type = SearchResult.SearchResultType.PREFIX,
            coordinates = listOf(coord)
        )
        assertNotNull(result.location)
        assertEquals(40.7128, result.location!!.latitude, 0.001)
        assertEquals(-74.0060, result.location!!.longitude, 0.001)
    }

    @Test
    fun testConstructorFromMwQueryPage() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Test_Page"
        every { page.thumbUrl() } returns "https://example.com/thumb.jpg"
        every { page.description } returns "A description"
        every { page.displayTitle("en") } returns "Test Page"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns "Redirected_From"
        every { page.snippet } returns "snippet text"

        val result = SearchResult(page, wikiSite,
            type = SearchResult.SearchResultType.FULL_TEXT,
            indexInApiCall = 2)

        assertEquals("Test_Page", result.pageTitle.text)
        assertEquals("Test Page", result.pageTitle.displayText)
        assertEquals("A description", result.pageTitle.description)
        assertEquals("Redirected_From", result.redirectFrom)
        assertEquals(SearchResult.SearchResultType.FULL_TEXT, result.type)
        assertEquals(2, result.indexInApiCall)
        assertEquals("snippet text", result.snippet)
    }

    @Test
    fun testConstructorFromMwQueryPageWithSectionTitle() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Article_Title"
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle("en") } returns "Article Title"
        every { page.sectionTitle } returns "Section 1"
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val result = SearchResult(page, wikiSite)

        assertNotNull(result.pageTitle.fragment)
        assertEquals("Section_1", result.pageTitle.fragment)
        assertEquals(SearchResult.SearchResultType.PREFIX, result.type)
    }

    @Test
    fun testConstructorFromMwQueryPageWithCoordinates() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Place"
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle("en") } returns "Place"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val coord = MwQueryPage.Coordinates(lat = 51.5074, lon = -0.1278)
        val result = SearchResult(page, wikiSite, coordinates = listOf(coord))

        assertNotNull(result.coordinates)
        assertEquals(1, result.coordinates!!.size)
        assertEquals(51.5074, result.coordinates!![0].lat, 0.001)
        assertEquals(-0.1278, result.coordinates!![0].lon, 0.001)
        assertNotNull(result.location)
    }

    @Test
    fun testConstructorFromMwQueryPageWithDefaultType() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Test"
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle("en") } returns "Test"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val result = SearchResult(page, wikiSite)
        assertEquals(SearchResult.SearchResultType.PREFIX, result.type)
        assertEquals(0, result.indexInApiCall)
    }

    @Test
    fun testSearchResultTypeEnumValues() {
        val types = SearchResult.SearchResultType.entries
        assertEquals(6, types.size)
        assertTrue(types.contains(SearchResult.SearchResultType.PREFIX))
        assertTrue(types.contains(SearchResult.SearchResultType.FULL_TEXT))
        assertTrue(types.contains(SearchResult.SearchResultType.HISTORY))
        assertTrue(types.contains(SearchResult.SearchResultType.READING_LIST))
        assertTrue(types.contains(SearchResult.SearchResultType.TAB_LIST))
        assertTrue(types.contains(SearchResult.SearchResultType.SEMANTIC))
    }

    @Test
    fun testSearchResultTypeEnumValueOf() {
        assertEquals(SearchResult.SearchResultType.PREFIX, SearchResult.SearchResultType.valueOf("PREFIX"))
        assertEquals(SearchResult.SearchResultType.FULL_TEXT, SearchResult.SearchResultType.valueOf("FULL_TEXT"))
        assertEquals(SearchResult.SearchResultType.SEMANTIC, SearchResult.SearchResultType.valueOf("SEMANTIC"))
    }

    @Test
    fun testLocationTakesFirstCoordinate() {
        val pageTitle = PageTitle("Test", wikiSite)
        val coord1 = MwQueryPage.Coordinates(lat = 40.0, lon = -70.0)
        val coord2 = MwQueryPage.Coordinates(lat = 50.0, lon = -80.0)
        val result = SearchResult(
            pageTitle = pageTitle,
            redirectFrom = null,
            type = SearchResult.SearchResultType.PREFIX,
            coordinates = listOf(coord1, coord2)
        )
        assertEquals(40.0, result.location!!.latitude, 0.001)
        assertEquals(-70.0, result.location!!.longitude, 0.001)
    }
}
