package org.wikipedia.dataclient.mwapi

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MwQueryPageTest {

    @Test
    fun testDisplayTitleReturnsFallback() {
        val page = mockk<MwQueryPage>()
        every { page.displayTitle("en") } answers { callOriginal() }
        every { page.title } returns "Test_Page"
    }

    @Test
    fun testNamespaceDefault() {
        val page = mockk<MwQueryPage>()
        every { page.namespace() } answers { callOriginal() }
        every { page.ns } returns 0
        val ns = page.namespace()
        assertEquals(org.wikipedia.page.Namespace.MAIN, ns)
    }

    @Test
    fun testThumbUrlNullByDefault() {
        val page = mockk<MwQueryPage>()
        every { page.thumbUrl() } answers { callOriginal() }
        assertNull(page.thumbUrl())
    }

    @Test
    fun testImageInfoNullByDefault() {
        val page = mockk<MwQueryPage>()
        every { page.imageInfo() } answers { callOriginal() }
        assertNull(page.imageInfo())
    }

    @Test
    fun testHasWatchlistExpiryFalseByDefault() {
        val page = mockk<MwQueryPage>()
        every { page.hasWatchlistExpiry() } answers { callOriginal() }
        assertFalse(page.hasWatchlistExpiry())
    }

    @Test
    fun testIsImageSharedFalseByDefault() {
        val page = mockk<MwQueryPage>()
        every { page.isImageShared } answers { callOriginal() }
        assertFalse(page.isImageShared)
    }

    @Test
    fun testGetErrorForActionEmptyByDefault() {
        val page = mockk<MwQueryPage>()
        every { page.getErrorForAction("edit") } answers { callOriginal() }
        assertTrue(page.getErrorForAction("edit").isEmpty())
    }

    @Test
    fun testCoordinatesDefault() {
        val coord = MwQueryPage.Coordinates()
        assertEquals(0.0, coord.lat, 0.001)
        assertEquals(0.0, coord.lon, 0.001)
    }

    @Test
    fun testCoordinatesWithValues() {
        val coord = MwQueryPage.Coordinates(lat = 51.5074, lon = -0.1278)
        assertEquals(51.5074, coord.lat, 0.001)
        assertEquals(-0.1278, coord.lon, 0.001)
    }

    @Test
    fun testLangLinkDefaults() {
        val langLink = MwQueryPage.LangLink()
        assertEquals("", langLink.lang)
        assertEquals("", langLink.title)
    }

    @Test
    fun testLangLinkWithValues() {
        val langLink = MwQueryPage.LangLink(lang = "fr", title = "Article_fr")
        assertEquals("fr", langLink.lang)
        assertEquals("Article_fr", langLink.title)
    }

    @Test
    fun testRevisionDefaults() {
        val rev = MwQueryPage.Revision()
        assertEquals("", rev.user)
        assertEquals(0, rev.revId)
    }

    @Test
    fun testRevisionGetContentFromSlot() {
        val rev = MwQueryPage.Revision()
        assertEquals("", rev.getContentFromSlot("main"))
    }
}