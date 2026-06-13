package org.wikipedia.search

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SemanticSearchServiceTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun testSemanticSearchResultsDeserialization() {
        val jsonString = """
            {
                "language_code": "el",
                "query": "test query",
                "semantic_search_results": [
                    {
                        "page_title": "Test Page",
                        "section_header": "Introduction",
                        "section_index": 0,
                        "section_text": "This is the introduction '''text'''.",
                        "url": "https://el.wikipedia.org/wiki/Test_Page"
                    }
                ]
            }
        """.trimIndent()
        
        val results = json.decodeFromString<SemanticSearchResults>(jsonString)
        
        assertEquals("el", results.languageCode)
        assertEquals("test query", results.query)
        assertEquals(1, results.results.size)
        assertEquals("Test Page", results.results[0].title)
        assertEquals("Introduction", results.results[0].sectionHeader)
        assertEquals(0, results.results[0].sectionIndex)
    }

    @Test
    fun testSemanticSearchResultsWithMultipleResults() {
        val jsonString = """
            {
                "language_code": "en",
                "query": "science",
                "semantic_search_results": [
                    {
                        "page_title": "Physics",
                        "section_header": "History",
                        "section_index": 1,
                        "section_text": "Physics has many ''historical'' developments.",
                        "url": "https://en.wikipedia.org/wiki/Physics"
                    },
                    {
                        "page_title": "Chemistry",
                        "section_header": "Overview",
                        "section_index": 0,
                        "section_text": "Chemistry is the study of matter.",
                        "url": "https://en.wikipedia.org/wiki/Chemistry"
                    }
                ]
            }
        """.trimIndent()
        
        val results = json.decodeFromString<SemanticSearchResults>(jsonString)
        
        assertEquals(2, results.results.size)
        assertEquals("Physics", results.results[0].title)
        assertEquals("Chemistry", results.results[1].title)
    }

    @Test
    fun testSemanticSearchResultsEmptyResults() {
        val jsonString = """
            {
                "language_code": "de",
                "query": "nonexistentterm12345",
                "semantic_search_results": []
            }
        """.trimIndent()
        
        val results = json.decodeFromString<SemanticSearchResults>(jsonString)
        
        assertTrue(results.results.isEmpty())
    }

    @Test
    fun testSemanticSearchResultDefaults() {
        val jsonString = """
            {
                "page_title": "Test",
                "url": "https://test.com"
            }
        """.trimIndent()
        
        val result = json.decodeFromString<SemanticSearchResult>(jsonString)
        
        assertEquals("Test", result.title)
        assertEquals("https://test.com", result.url)
        assertEquals("", result.sectionHeader)
        assertEquals(0, result.sectionIndex)
        assertEquals("", result.sectionText)
    }

    @Test
    fun testSemanticSearchResultWithSpecialCharacters() {
        val jsonString = """
            {
                "page_title": "Test <Page>",
                "section_header": "Section with 'quotes'",
                "section_index": 5,
                "section_text": "Text with '''bold''' and ''italic''",
                "url": "https://example.com/Test_Page"
            }
        """.trimIndent()
        
        val result = json.decodeFromString<SemanticSearchResult>(jsonString)
        
        assertEquals("Test <Page>", result.title)
        assertEquals("Section with 'quotes'", result.sectionHeader)
        assertEquals(5, result.sectionIndex)
    }

    @Test
    fun testSemanticSearchServiceBaseUrl() {
        assertEquals("https://semantic-search.wmcloud.org/", SemanticSearchService.BASE_URL)
    }
}
