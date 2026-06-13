package org.wikipedia.dataclient.page

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.Namespace
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class PageSummaryTest {

    private val enWiki = WikiSite.forLanguageCode("en")

    @Test
    fun testThumbnailUrlReturnsSource() {
        val summary = PageSummary(
            thumbnail = PageSummary.Thumbnail("https://upload.wikimedia.org/test.jpg", 100, 200)
        )
        assertEquals("https://upload.wikimedia.org/test.jpg", summary.thumbnailUrl)
    }

    @Test
    fun testThumbnailUrlReturnsNullWhenNoThumbnail() {
        val summary = PageSummary()
        assertNull(summary.thumbnailUrl)
    }

    @Test
    fun testApiTitleReturnsCanonicalTitle() {
        val summary = PageSummary(
            titles = PageSummary.Titles(canonical = "Albert_Einstein", display = "Albert Einstein")
        )
        assertEquals("Albert_Einstein", summary.apiTitle)
    }

    @Test
    fun testApiTitleReturnsEmptyWhenNoTitles() {
        val summary = PageSummary()
        assertEquals("", summary.apiTitle)
    }

    @Test
    fun testDisplayTitleReturnsDisplayValue() {
        val summary = PageSummary(
            titles = PageSummary.Titles(canonical = "Albert_Einstein", display = "Albert Einstein")
        )
        assertEquals("Albert Einstein", summary.displayTitle)
    }

    @Test
    fun testDisplayTitleReturnsEmptyWhenNull() {
        val summary = PageSummary()
        assertEquals("", summary.displayTitle)
    }

    @Test
    fun testLeadImageNameExtractsFromUrl() {
        val summary = PageSummary(
            thumbnail = PageSummary.Thumbnail("https://upload.wikimedia.org/wikipedia/commons/test.jpg", 100, 200)
        )
        assertNotNull(summary.leadImageName)
    }

    @Test
    fun testLeadImageNameReturnsNullWhenNoThumbnail() {
        val summary = PageSummary()
        assertNull(summary.leadImageName)
    }

    @Test
    fun testNsReturnsMainWhenNullNamespace() {
        val summary = PageSummary(namespace = null)
        assertEquals(Namespace.MAIN, summary.ns)
    }

    @Test
    fun testNsReturnsCorrectNamespace() {
        val summary = PageSummary(namespace = PageSummary.NamespaceContainer(1, "Talk"))
        assertEquals(Namespace.TALK, summary.ns)
    }

    @Test
    fun testNsReturnsMainForZeroId() {
        val summary = PageSummary(namespace = PageSummary.NamespaceContainer(0, ""))
        assertEquals(Namespace.MAIN, summary.ns)
    }

    @Test
    fun testTypeConstantsHaveExpectedValues() {
        assertEquals("standard", PageSummary.TYPE_STANDARD)
        assertEquals("disambiguation", PageSummary.TYPE_DISAMBIGUATION)
        assertEquals("mainpage", PageSummary.TYPE_MAIN_PAGE)
    }

    @Test
    fun testDefaultTypeIsStandard() {
        val summary = PageSummary()
        assertEquals(PageSummary.TYPE_STANDARD, summary.type)
    }

    @Test
    fun testSecondaryConstructorCreatesCorrectTitles() {
        val summary = PageSummary(
            displayTitle = "Display Title",
            prefixTitle = "Prefix_Title",
            description = "A description",
            extract = "An extract",
            thumbnail = "https://example.com/thumb.jpg",
            lang = "en"
        )
        assertEquals("Prefix_Title", summary.apiTitle)
        assertEquals("Display Title", summary.displayTitle)
        assertEquals("A description", summary.description)
        assertEquals("An extract", summary.extract)
        assertEquals("https://example.com/thumb.jpg", summary.thumbnailUrl)
        assertEquals("en", summary.lang)
    }

    @Test
    fun testToStringReturnsDisplayTitle() {
        val summary = PageSummary(
            titles = PageSummary.Titles(canonical = "Test_Page", display = "Test Page")
        )
        assertEquals("Test Page", summary.toString())
    }

    @Test
    fun testToStringReturnsEmptyWhenNoTitles() {
        val summary = PageSummary()
        assertEquals("", summary.toString())
    }

    @Test
    fun testGetPageTitleCreatesCorrectPageTitle() {
        val summary = PageSummary(
            titles = PageSummary.Titles(canonical = "Main_Page", display = "Main Page"),
            description = "Description text",
            extract = "Extract text",
            thumbnail = PageSummary.Thumbnail("https://upload.wikimedia.org/thumb.jpg", 100, 200)
        )
        val pageTitle = summary.getPageTitle(enWiki)

        assertEquals("Main_Page", pageTitle.text)
        assertEquals("Main Page", pageTitle.displayText)
        assertEquals("Description text", pageTitle.description)
        assertEquals("https://upload.wikimedia.org/thumb.jpg", pageTitle.thumbUrl)
    }

    @Test
    fun testToPageReturnsPageForValidTitle() {
        val summary = PageSummary(
            titles = PageSummary.Titles(canonical = "Test_Page", display = "Test Page")
        )
        val pageTitle = PageTitle("Test_Page", enWiki)
        val page = summary.toPage(pageTitle)

        assertNotNull(page)
        assertEquals("Test_Page", page!!.title.text)
    }

    @Test
    fun testToPageReturnsNullForNullTitle() {
        val summary = PageSummary()
        val page = summary.toPage(null)
        assertNull(page)
    }

    @Test
    fun testAdjustPageTitleUpdatesCanonicalName() {
        val summary = PageSummary(
            titles = PageSummary.Titles(canonical = "Updated_Name", display = "Updated Name"),
            description = "Updated description"
        )
        val originalTitle = PageTitle("Original_Name", enWiki, "https://example.com/thumb.jpg")
        originalTitle.fragment = "Section1"
        val page = summary.toPage(originalTitle)

        assertNotNull(page)
        assertEquals("Updated_Name", page!!.title.text)
        assertEquals("Section1", page.title.fragment)
        assertEquals("Updated description", page.title.description)
    }

    @Test
    fun testNamespaceContainerDefaults() {
        val container = PageSummary.NamespaceContainer()
        assertEquals(0, container.id)
        assertEquals("", container.text)
    }

    @Test
    fun testTitlesDefaults() {
        val titles = PageSummary.Titles(null, null)
        assertNull(titles.canonical)
        assertNull(titles.display)
    }

    @Test
    fun testThumbnailDefaults() {
        val thumbnail = PageSummary.Thumbnail("https://test.jpg", 800, 600)
        assertEquals("https://test.jpg", thumbnail.source)
        assertEquals(800, thumbnail.width)
        assertEquals(600, thumbnail.height)
    }

    @Test
    fun testDefaultValuesAreCorrect() {
        val summary = PageSummary()
        assertEquals("", summary.lang)
        assertEquals(PageSummary.TYPE_STANDARD, summary.type)
        assertEquals(0, summary.pageId)
        assertEquals(0L, summary.revision)
        assertEquals("", summary.timestamp)
        assertEquals(0, summary.views)
        assertNull(summary.extract)
        assertNull(summary.description)
        assertNull(summary.coordinates)
        assertEquals("", summary.descriptionSource)
    }
}
