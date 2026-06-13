package org.wikipedia.search

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.Constants
import org.wikipedia.WikipediaApp
import org.wikipedia.database.AppDatabase
import org.wikipedia.dataclient.Service
import org.wikipedia.dataclient.ServiceFactory
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.dataclient.mwapi.MwQueryPage
import org.wikipedia.dataclient.mwapi.MwQueryResponse
import org.wikipedia.dataclient.mwapi.MwQueryResult
import org.wikipedia.history.db.HistoryEntryWithImageDao
import org.wikipedia.readinglist.db.ReadingListPageDao
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class StandardSearchRepositoryTest {

    private val service = mockk<Service>(relaxed = true)
    private val historyDao = mockk<HistoryEntryWithImageDao>(relaxed = true)
    private val readingListPageDao = mockk<ReadingListPageDao>(relaxed = true)
    private val wikipediaApp = mockk<WikipediaApp>(relaxed = true)

    @Before
    fun setUp() {
        mockkObject(AppDatabase)
        every { AppDatabase.instance } returns mockk(relaxed = true)
        every { AppDatabase.instance.historyEntryWithImageDao() } returns historyDao
        every { AppDatabase.instance.readingListPageDao() } returns readingListPageDao
        coEvery { historyDao.findHistoryItem(any(), any()) } returns SearchResults()
        coEvery { readingListPageDao.findPageForSearchQueryInAnyList(any(), any()) } returns SearchResults()

        mockkObject(WikipediaApp)
        every { WikipediaApp.instance } returns wikipediaApp
        every { wikipediaApp.tabList } returns mutableListOf()
        every { wikipediaApp.languageState } returns mockk(relaxed = true)
        every { wikipediaApp.languageState.appLanguageCodes } returns listOf("en")

        mockkObject(ServiceFactory)
        every { ServiceFactory.get(any<WikiSite>()) } returns service
        every { ServiceFactory.get(any<WikiSite>(), any(), any<Class<*>>()) } returns service
    }

    @After
    fun tearDown() {
        unmockkObject(AppDatabase)
        unmockkObject(WikipediaApp)
        unmockkObject(ServiceFactory)
    }

    // region Helper methods

    private fun createMockResponse(
        pages: List<MwQueryPage>? = null,
        xSearchId: String? = null,
        continuation: MwQueryResponse.Continuation? = null
    ): retrofit2.Response<MwQueryResponse> {
        val queryResult = mockk<MwQueryResult>(relaxed = true)
        every { queryResult.pages } returns pages

        val mwResponse = mockk<MwQueryResponse>(relaxed = true)
        every { mwResponse.query } returns queryResult
        every { mwResponse.continuation } returns continuation

        val mockResponse = mockk<Response<MwQueryResponse>>(relaxed = true)
        val headers = mockk<okhttp3.Headers>(relaxed = true)
        every { headers["x-search-id"] } returns xSearchId
        every { mockResponse.headers() } returns headers
        every { mockResponse.body() } returns mwResponse
        return mockResponse
    }

    private fun createMockPage(
        title: String,
        index: Int = 0,
        coordinates: List<MwQueryPage.Coordinates>? = null
    ): MwQueryPage {
        val page = mockk<MwQueryPage>()
        every { page.title } returns title
        every { page.index } returns index
        every { page.coordinates } returns coordinates
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle(any()) } returns title
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null
        return page
    }

    // endregion

    @Test
    fun testPrefixSearchReturnsResults() = runBlocking {
        val pages = (0 until 10).map { createMockPage("Page_$it", index = it) }
        val mockResponse = createMockResponse(pages = pages, xSearchId = "search-id-1")

        coEvery { service.prefixSearchResponse("test", 10, 0) } returns mockResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "test",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(10, result.results.size)
        assertEquals("search-id-1", result.xSearchIdPrefix)
        assertEquals(0, result.continuation)
    }

    @Test
    fun testPrefixSearchWithShortTermSkipsTabHistory() = runBlocking {
        val pages = (0 until 10).map { createMockPage("Page_$it", index = it) }
        val mockResponse = createMockResponse(pages = pages)

        coEvery { service.prefixSearchResponse("xy", 10, 0) } returns mockResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "xy",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(10, result.results.size)
    }

    @Test
    fun testPrefixSearchWithPlacesSourceSkipsTabHistory() = runBlocking {
        // With PLACES, only pages with coordinates are included
        val pages = (0 until 10).map {
            createMockPage("Place_$it", index = it, coordinates = listOf(MwQueryPage.Coordinates(it.toDouble(), it.toDouble())))
        }
        val mockResponse = createMockResponse(pages = pages)

        coEvery { service.prefixSearchResponse("place", 10, 0) } returns mockResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "place",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.PLACES,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(10, result.results.size)
    }

    @Test
    fun testFullTextSearchWhenPrefixInsufficient() = runBlocking {
        val prefixPage = createMockPage("Only_One")
        val mockPrefixResponse = createMockResponse(pages = listOf(prefixPage))

        val fullPage = createMockPage("FullText_Result", index = 1)
        val fullTextContinuation = mockk<MwQueryResponse.Continuation>(relaxed = true)
        every { fullTextContinuation.gsroffset } returns 10
        val mockFullTextResponse = createMockResponse(
            pages = listOf(fullPage),
            xSearchId = "fulltext-id",
            continuation = fullTextContinuation
        )

        coEvery { service.prefixSearchResponse("query", 10, 0) } returns mockPrefixResponse
        coEvery { service.fullTextSearchResponse("query", 10, 0) } returns mockFullTextResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "query",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(2, result.results.size)
        assertEquals("fulltext-id", result.xSearchIdFullText)
        assertEquals(10, result.continuation)
    }

    @Test
    fun testNotPrefixSearchSkipsToFullText() = runBlocking {
        val pages = (0 until 10).map { createMockPage("FullText_$it", index = it) }
        val fullTextContinuation = mockk<MwQueryResponse.Continuation>(relaxed = true)
        every { fullTextContinuation.gsroffset } returns 5
        val mockResponse = createMockResponse(
            pages = pages,
            xSearchId = "fulltext-only",
            continuation = fullTextContinuation
        )

        coEvery { service.fullTextSearchResponse("search", 10, null) } returns mockResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "search",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = false,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(10, result.results.size)
        assertNull(result.xSearchIdPrefix)
        assertEquals("fulltext-only", result.xSearchIdFullText)
    }

    @Test
    fun testEmptyResultsPopulatesCountsPerLanguage() = runBlocking {
        val emptyResponse = createMockResponse(pages = emptyList())
        coEvery { service.prefixSearchResponse("noresults", 10, 0) } returns emptyResponse
        // Full text also returns empty so we don't get ClassCastException
        coEvery { service.fullTextSearchResponse("noresults", 10, 0) } returns emptyResponse

        coEvery { service.prefixSearch("noresults", 10, 0) } returns mockk(relaxed = true)
        coEvery { service.fullTextSearch("noresults", 10, null) } returns mockk(relaxed = true)

        val repository = StandardSearchRepository()
        val countsPerLanguage = mutableListOf<Pair<String, Int>>()
        val result = repository.search(
            searchTerm = "noresults",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = countsPerLanguage
        )

        assertTrue(result.results.isEmpty())
        assertTrue(countsPerLanguage.isNotEmpty())
        assertEquals("en", countsPerLanguage[0].first)
        assertEquals(0, countsPerLanguage[0].second)
    }

    @Test
    fun testEmptyResultsClearsCountsPerLanguage() = runBlocking {
        val countList = mutableListOf(Pair("es", 5), Pair("fr", 3))
        val emptyResponse = createMockResponse(pages = emptyList())
        coEvery { service.prefixSearchResponse("nothing", 10, 0) } returns emptyResponse
        coEvery { service.fullTextSearchResponse("nothing", 10, 0) } returns emptyResponse

        val repository = StandardSearchRepository()
        repository.search(
            searchTerm = "nothing",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = countList
        )

        assertFalse(countList.any { it.first == "es" })
        assertTrue(countList.any { it.first == "en" })
    }

    @Test
    fun testResultsAreDistinctByPrefixedText() = runBlocking {
        val pages = (0 until 10).map { createMockPage("Page_$it", index = it) }
        val mockResponse = createMockResponse(pages = pages)

        coEvery { service.prefixSearchResponse("term", 10, 0) } returns mockResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "term",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(10, result.results.size)
    }

    @Test
    fun testNonEmptyResultsSkipCountsPerLanguage() = runBlocking {
        val pages = (0 until 10).map { createMockPage("Result_$it", index = it) }
        val mockResponse = createMockResponse(pages = pages)

        coEvery { service.prefixSearchResponse("found", 10, 0) } returns mockResponse

        val repository = StandardSearchRepository()
        val countsPerLanguage = mutableListOf<Pair<String, Int>>()
        repository.search(
            searchTerm = "found",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = null,
            batchSize = 10,
            isPrefixSearch = true,
            countsPerLanguageCode = countsPerLanguage
        )

        assertTrue(countsPerLanguage.isEmpty())
    }

    @Test
    fun testWithContinuationParameter() = runBlocking {
        val pages = (0 until 10).map { createMockPage("Next_$it", index = it) }
        val fullTextContinuation = mockk<MwQueryResponse.Continuation>(relaxed = true)
        every { fullTextContinuation.gsroffset } returns 30
        val mockResponse = createMockResponse(
            pages = pages,
            xSearchId = "continued",
            continuation = fullTextContinuation
        )

        coEvery { service.fullTextSearchResponse("seek", 10, 10) } returns mockResponse

        val repository = StandardSearchRepository()
        val result = repository.search(
            searchTerm = "seek",
            languageCode = "en",
            invokeSource = Constants.InvokeSource.SEARCH,
            continuation = 10,
            batchSize = 10,
            isPrefixSearch = false,
            countsPerLanguageCode = mutableListOf()
        )

        assertEquals(10, result.results.size)
        assertEquals(30, result.continuation)
    }
}
