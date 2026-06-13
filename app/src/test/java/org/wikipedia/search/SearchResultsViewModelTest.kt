package org.wikipedia.search

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.Constants
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.dataclient.mwapi.MwQueryPage
import org.wikipedia.dataclient.mwapi.MwQueryResponse
import org.wikipedia.dataclient.mwapi.MwQueryResult
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class SearchResultsViewModelTest {

    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testPostProcessSectionTextBold() {
        val input = "This is '''bold''' text"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("This is <b>bold</b> text", result)
    }

    @Test
    fun testPostProcessSectionTextItalic() {
        val input = "This is ''italic'' text"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("This is <i>italic</i> text", result)
    }

    @Test
    fun testPostProcessSectionTextBoldAndItalic() {
        val input = "'''bold''' and ''italic''"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("<b>bold</b> and <i>italic</i>", result)
    }

    @Test
    fun testPostProcessSectionTextEmptyParens() {
        val input = "text () more"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("text  more", result)
    }

    @Test
    fun testPostProcessSectionTextParensWithComma() {
        val input = "text (,) more"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("text  more", result)
    }

    @Test
    fun testPostProcessSectionTextParensWithSemicolon() {
        val input = "text (;) more"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("text  more", result)
    }

    @Test
    fun testPostProcessSectionTextNormalParens() {
        val input = "text (hello) more"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("text (hello) more", result)
    }

    @Test
    fun testPostProcessSectionTextEmpty() {
        val result = SearchResultsViewModel.postProcessSectionText("")
        assertEquals("", result)
    }

    @Test
    fun testBuildListWithNullResponse() {
        val result = SearchResultsViewModel.buildList(
            response = null,
            invokeSource = Constants.InvokeSource.SEARCH,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.PREFIX
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun testBuildListWithNullPages() {
        val queryResult = mockk<MwQueryResult>()
        every { queryResult.pages } returns null
        val response = MwQueryResponse()
        response.query = queryResult

        val result = SearchResultsViewModel.buildList(
            response = response,
            invokeSource = Constants.InvokeSource.SEARCH,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.PREFIX
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun testBuildListWithPagesForPlacesSource() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Test_Page"
        every { page.index } returns 0
        every { page.coordinates } returns listOf(MwQueryPage.Coordinates(1.0, 2.0))
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle(any()) } returns "Test_Page"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val queryResult = mockk<MwQueryResult>()
        every { queryResult.pages } returns listOf(page)
        val response = MwQueryResponse()
        response.query = queryResult

        val result = SearchResultsViewModel.buildList(
            response = response,
            invokeSource = Constants.InvokeSource.PLACES,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.PREFIX
        )
        assertEquals(1, result.size)
    }

    @Test
    fun testBuildListWithPlacesSourceFiltersNoCoordinates() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Test_Page"
        every { page.index } returns 0
        every { page.coordinates } returns null

        val queryResult = mockk<MwQueryResult>()
        every { queryResult.pages } returns listOf(page)
        val response = MwQueryResponse()
        response.query = queryResult

        val result = SearchResultsViewModel.buildList(
            response = response,
            invokeSource = Constants.InvokeSource.PLACES,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.PREFIX
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun testBuildListWithSearchSourceIncludesNoCoordinates() {
        val page = mockk<MwQueryPage>()
        every { page.title } returns "Test_Page"
        every { page.index } returns 0
        every { page.coordinates } returns null
        every { page.thumbUrl() } returns null
        every { page.description } returns null
        every { page.displayTitle(any()) } returns "Test_Page"
        every { page.sectionTitle } returns null
        every { page.redirectFrom } returns null
        every { page.snippet } returns null

        val queryResult = mockk<MwQueryResult>()
        every { queryResult.pages } returns listOf(page)
        val response = MwQueryResponse()
        response.query = queryResult

        val result = SearchResultsViewModel.buildList(
            response = response,
            invokeSource = Constants.InvokeSource.SEARCH,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.FULL_TEXT
        )
        assertEquals(1, result.size)
        assertEquals(SearchResult.SearchResultType.FULL_TEXT, result[0].type)
    }

    @Test
    fun testBuildListWithSemanticSearchResults() {
        val semanticResult = SemanticSearchResult().apply {
            setProperty(SemanticSearchResult::title.name, "Physics")
            setProperty(SemanticSearchResult::sectionHeader.name, "History")
            setProperty(SemanticSearchResult::sectionIndex.name, 1)
            setProperty(SemanticSearchResult::sectionText.name, "'''bold''' text")
            setProperty(SemanticSearchResult::url.name, "https://en.wikipedia.org/wiki/Physics#History")
        }
        val semanticResults = SemanticSearchResults().apply {
            setProperty(SemanticSearchResults::results.name, listOf(semanticResult))
        }

        val result = SearchResultsViewModel.buildList(
            response = semanticResults,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.SEMANTIC
        )
        assertEquals(1, result.size)
        assertEquals(SearchResult.SearchResultType.SEMANTIC, result[0].type)
        assertEquals("<b>bold</b> text", result[0].snippet)
        assertEquals(1, result[0].indexInApiCall)
    }

    @Test
    fun testBuildListWithSemanticSearchResultsMultiple() {
        val semanticResult1 = SemanticSearchResult().apply {
            setProperty(SemanticSearchResult::title.name, "Page1")
            setProperty(SemanticSearchResult::url.name, "https://en.wikipedia.org/wiki/Page1")
        }
        val semanticResult2 = SemanticSearchResult().apply {
            setProperty(SemanticSearchResult::title.name, "Page2")
            setProperty(SemanticSearchResult::url.name, "https://en.wikipedia.org/wiki/Page2")
        }
        val semanticResults = SemanticSearchResults().apply {
            setProperty(SemanticSearchResults::results.name, listOf(semanticResult1, semanticResult2))
        }

        val result = SearchResultsViewModel.buildList(
            response = semanticResults,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.SEMANTIC
        )
        assertEquals(2, result.size)
        assertEquals(1, result[0].indexInApiCall)
        assertEquals(2, result[1].indexInApiCall)
    }

    @Test
    fun testBuildListSortedByIndex() {
        val page1 = mockk<MwQueryPage>()
        every { page1.title } returns "Z_Page"
        every { page1.index } returns 2
        every { page1.coordinates } returns null
        every { page1.thumbUrl() } returns null
        every { page1.description } returns null
        every { page1.displayTitle(any()) } returns "Z_Page"
        every { page1.sectionTitle } returns null
        every { page1.redirectFrom } returns null
        every { page1.snippet } returns null

        val page2 = mockk<MwQueryPage>()
        every { page2.title } returns "A_Page"
        every { page2.index } returns 0
        every { page2.coordinates } returns null
        every { page2.thumbUrl() } returns null
        every { page2.description } returns null
        every { page2.displayTitle(any()) } returns "A_Page"
        every { page2.sectionTitle } returns null
        every { page2.redirectFrom } returns null
        every { page2.snippet } returns null

        val page3 = mockk<MwQueryPage>()
        every { page3.title } returns "M_Page"
        every { page3.index } returns 1
        every { page3.coordinates } returns null
        every { page3.thumbUrl() } returns null
        every { page3.description } returns null
        every { page3.displayTitle(any()) } returns "M_Page"
        every { page3.sectionTitle } returns null
        every { page3.redirectFrom } returns null
        every { page3.snippet } returns null

        val queryResult = mockk<MwQueryResult>()
        every { queryResult.pages } returns listOf(page1, page2, page3)
        val response = MwQueryResponse()
        response.query = queryResult

        val result = SearchResultsViewModel.buildList(
            response = response,
            invokeSource = Constants.InvokeSource.SEARCH,
            wikiSite = wikiSite,
            type = SearchResult.SearchResultType.PREFIX
        )
        assertEquals(3, result.size)
        assertEquals(0, result[0].indexInApiCall)
        assertEquals(1, result[1].indexInApiCall)
        assertEquals(2, result[2].indexInApiCall)
    }

    @Test
    fun testGetStandardEventActionContextWithoutResult() {
        val vm = SearchResultsViewModel()
        val context = vm.getStandardEventActionContext()
        assertTrue(context.containsKey("search_id_pre"))
        assertTrue(context.containsKey("search_id_ful"))
        assertFalse(context.containsKey("position"))
        assertFalse(context.containsKey("type"))
    }

    @Test
    fun testGetStandardEventActionContextWithResult() {
        val vm = SearchResultsViewModel()
        val pageTitle = PageTitle("Test", wikiSite)
        val result = SearchResult(pageTitle, SearchResult.SearchResultType.PREFIX, indexInApiCall = 3)
        val context = vm.getStandardEventActionContext(result)
        assertTrue(context.containsKey("search_id_pre"))
        assertTrue(context.containsKey("search_id_ful"))
        assertEquals(3, context["position"])
        assertEquals(SearchResult.SearchResultType.PREFIX, context["type"])
    }

    @Test
    fun testGetHybridEventActionContextWithoutResult() {
        val vm = SearchResultsViewModel()
        val context = vm.getHybridEventActionContext()
        assertTrue(context.containsKey("search_id_pre"))
        assertTrue(context.containsKey("search_id_ful"))
        assertTrue(context.containsKey("search_id_sem"))
        assertFalse(context.containsKey("position"))
    }

    @Test
    fun testGetHybridEventActionContextWithResult() {
        val vm = SearchResultsViewModel()
        val pageTitle = PageTitle("Test", wikiSite)
        val result = SearchResult(pageTitle, SearchResult.SearchResultType.SEMANTIC, indexInApiCall = 5)
        val context = vm.getHybridEventActionContext(result)
        assertTrue(context.containsKey("search_id_sem"))
        assertEquals(5, context["position"])
        assertEquals(SearchResult.SearchResultType.SEMANTIC, context["type"])
    }

    @Test
    fun testGetBreadcrumbActionContext() {
        val vm = SearchResultsViewModel()
        vm.updateSearchTerm("test query")
        val context = vm.getBreadcrumbActionContext()
        assertTrue(context.containsKey("search_id_sem"))
        assertTrue(context.containsKey("lexical"))
        assertTrue(context.containsKey("semantic"))
        assertEquals("test query", context["query"])
    }

    @Test
    fun testUpdateSearchTerm() {
        val vm = SearchResultsViewModel()
        vm.updateSearchTerm("hello world")
        // The searchTerm StateFlow should reflect the update
        assertEquals("hello world", vm.searchTerm.value)
    }

    @Test
    fun testUpdateSearchTermWithNull() {
        val vm = SearchResultsViewModel()
        vm.updateSearchTerm("first")
        vm.updateSearchTerm(null)
        assertNull(vm.searchTerm.value)
    }

    @Test
    fun testUpdateSearchTermWithEmpty() {
        val vm = SearchResultsViewModel()
        vm.updateSearchTerm("")
        assertEquals("", vm.searchTerm.value)
    }

    @Test
    fun testUpdateLanguageCode() {
        val vm = SearchResultsViewModel()
        vm.updateLanguageCode("fr")
        assertEquals("fr", vm.languageCode.value)
    }

    @Test
    fun testResetHybridSearchState() {
        val vm = SearchResultsViewModel()
        vm.resetHybridSearchState()
        // After reset, state should be Loading
        assertTrue(vm.hybridSearchResultState.value is org.wikipedia.util.UiState.Loading)
    }

    @Test
    fun testPostProcessSectionTextBoldMultiline() {
        val input = "'''First'''\n'''Second'''"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("<b>First</b>\n<b>Second</b>", result)
    }

    @Test
    fun testPostProcessSectionTextMultipleBoldInLine() {
        val input = "a'''b'''c'''d'''e"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("a<b>b</b>c<b>d</b>e", result)
    }

    @Test
    fun testPostProcessSectionTextItalicEmpty() {
        // '''''' is matched by bold regex first, producing <b></b>
        val input = "'''''' text"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("<b></b> text", result)
    }

    @Test
    fun testPostProcessSectionTextComplexParens() {
        val input = "text ( , ) middle (;) end"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("text  middle  end", result)
    }

    @Test
    fun testPostProcessSectionTextParensWithDot() {
        val input = "text (.) more"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("text  more", result)
    }

    @Test
    fun testPostProcessSectionTextNoWikiMarkup() {
        val input = "Plain text without any wiki markup"
        val result = SearchResultsViewModel.postProcessSectionText(input)
        assertEquals("Plain text without any wiki markup", result)
    }

    @Test
    fun testGetStandardEventActionContextKeys() {
        val vm = SearchResultsViewModel()
        val context = vm.getStandardEventActionContext()
        assertEquals(2, context.size)
        assertEquals("", context["search_id_pre"])
        assertEquals("", context["search_id_ful"])
    }

    @Test
    fun testGetHybridEventActionContextKeys() {
        val vm = SearchResultsViewModel()
        val context = vm.getHybridEventActionContext()
        assertEquals(3, context.size)
        assertEquals("", context["search_id_pre"])
        assertEquals("", context["search_id_ful"])
        assertEquals("", context["search_id_sem"])
    }

    private fun Any.setProperty(propertyName: String, value: Any?) {
        val field = javaClass.getDeclaredField(propertyName)
        field.isAccessible = true
        field.set(this, value)
    }
}
