package org.wikipedia.readinglist.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.Namespace

@RunWith(RobolectricTestRunner::class)
class ReadingListPageTest {

    private lateinit var enwiki: WikiSite

    @Before
    fun setUp() {
        enwiki = WikiSite.forLanguageCode("en")
    }

    // ---- Status constants ----

    @Test
    fun testStatusConstants() {
        assertEquals(0L, ReadingListPage.STATUS_QUEUE_FOR_SAVE)
        assertEquals(1L, ReadingListPage.STATUS_SAVED)
        assertEquals(2L, ReadingListPage.STATUS_QUEUE_FOR_DELETE)
        assertEquals(3L, ReadingListPage.STATUS_QUEUE_FOR_FORCED_SAVE)
    }

    // ---- Constructor ----

    @Test
    fun testBasicConstructor() {
        val page = ReadingListPage(
            enwiki, Namespace.MAIN, "Test Title", "Test_Title",
            description = "A test page", thumbUrl = "https://example.com/thumb.jpg"
        )
        assertEquals("Test Title", page.displayTitle)
        assertEquals("Test_Title", page.apiTitle)
        assertEquals("A test page", page.description)
        assertEquals("https://example.com/thumb.jpg", page.thumbUrl)
        assertEquals(Namespace.MAIN, page.namespace)
        assertEquals("en", page.lang)
    }

    @Test
    fun testDefaultConstructorValues() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        assertEquals(-1, page.listId)
        assertEquals(0, page.id)
        assertEquals(ReadingListPage.STATUS_QUEUE_FOR_SAVE, page.status)
        assertEquals(0, page.revId)
        assertEquals(0, page.remoteId)
    }

    @Test
    fun testConstructorDefaultsMtimeAndAtimeToZero() {
        // Primary constructor defaults mtime and atime to 0
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        assertEquals(0, page.mtime)
        assertEquals(0, page.atime)
    }

    // ---- Download progress defaults ----

    @Test
    fun testDownloadProgressDefaultZero() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        assertEquals(0, page.downloadProgress)
    }

    @Test
    fun testDownloadProgressCanBeSet() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        page.downloadProgress = 75
        assertEquals(75, page.downloadProgress)
    }

    // ---- Selected default ----

    @Test
    fun testSelectedDefaultFalse() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        assertFalse(page.selected)
    }

    @Test
    fun testSelectedCanBeSet() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        page.selected = true
        assertTrue(page.selected)
    }

    // ---- saving property ----

    @Test
    fun testSavingWhenQueueForSave() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title",
            offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_SAVE)
        assertTrue(page.saving)
    }

    @Test
    fun testSavingWhenQueueForForcedSave() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title",
            offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_FORCED_SAVE)
        assertTrue(page.saving)
    }

    @Test
    fun testNotSavingWhenSaved() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title",
            offline = true, status = ReadingListPage.STATUS_SAVED)
        assertFalse(page.saving)
    }

    @Test
    fun testNotSavingWhenQueueForDelete() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title",
            offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_DELETE)
        assertFalse(page.saving)
    }

    @Test
    fun testNotSavingWhenOfflineFalse() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title",
            offline = false, status = ReadingListPage.STATUS_QUEUE_FOR_SAVE)
        assertFalse(page.saving)
    }

    @Test
    fun testNotSavingWhenOfflineFalseAndForcedSave() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title",
            offline = false, status = ReadingListPage.STATUS_QUEUE_FOR_FORCED_SAVE)
        assertFalse(page.saving)
    }

    // ---- toPageSummary ----

    @Test
    fun testToPageSummary() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Display", "API_Title",
            description = "Desc", thumbUrl = "https://thumb.url", lang = "en")
        val summary = ReadingListPage.toPageSummary(page)
        assertNotNull(summary)
        assertEquals("Display", summary.displayTitle)
        assertEquals("API_Title", summary.apiTitle)
        assertEquals("Desc", summary.description)
        assertEquals("https://thumb.url", summary.thumbnailUrl)
        assertEquals("en", summary.lang)
    }

    @Test
    fun testToPageSummaryWithNullDescription() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Display", "API_Title", lang = "en")
        val summary = ReadingListPage.toPageSummary(page)
        assertEquals(null, summary.description)
    }

    // ---- toPageTitle ----

    @Test
    fun testToPageTitle() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Display", "API_Title",
            description = "Desc", thumbUrl = "https://thumb.url", lang = "en")
        val pageTitle = ReadingListPage.toPageTitle(page)
        assertEquals("API_Title", pageTitle.prefixedText)
        assertEquals("Display", pageTitle.displayText)
        assertEquals("Desc", pageTitle.description)
        assertEquals("https://thumb.url", pageTitle.thumbUrl)
    }

    // ---- lang default ----

    @Test
    fun testLangDefaultsToEn() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        assertEquals("en", page.lang)
    }

    // ---- sizeBytes default ----

    @Test
    fun testSizeBytesDefaultZero() {
        val page = ReadingListPage(enwiki, Namespace.MAIN, "Title", "Title")
        assertEquals(0, page.sizeBytes)
    }

    // ---- Multiple pages with different statuses ----

    @Test
    fun testAllStatusTransitions() {
        val queuedForSave = ReadingListPage(enwiki, Namespace.MAIN, "T", "T",
            offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_SAVE)
        val saved = ReadingListPage(enwiki, Namespace.MAIN, "T", "T",
            offline = true, status = ReadingListPage.STATUS_SAVED)
        val queuedForDelete = ReadingListPage(enwiki, Namespace.MAIN, "T", "T",
            offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_DELETE)
        val forcedSave = ReadingListPage(enwiki, Namespace.MAIN, "T", "T",
            offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_FORCED_SAVE)

        assertTrue(queuedForSave.saving)
        assertFalse(saved.saving)
        assertFalse(queuedForDelete.saving)
        assertTrue(forcedSave.saving)
    }
}
