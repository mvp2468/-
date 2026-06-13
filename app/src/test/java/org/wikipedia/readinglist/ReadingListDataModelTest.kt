package org.wikipedia.readinglist

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.PageTitle
import org.wikipedia.readinglist.database.ReadingList
import org.wikipedia.readinglist.database.ReadingListPage

@RunWith(RobolectricTestRunner::class)
class ReadingListDataModelTest {

    @Test
    fun testReadingListPageDefaultValues() {
        val wikiSite = WikiSite.forLanguageCode("en")
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val page = ReadingListPage(pageTitle)
        assertNotNull(page)
        assertEquals("Test_Page", page.apiTitle)
    }

    @Test
    fun testReadingListDefaultValues() {
        val list = ReadingList("My List", "A test list")
        assertNotNull(list)
        assertEquals("My List", list.title)
        assertEquals("A test list", list.description)
    }

    @Test
    fun testReadingListEmptyTitleUsesDefault() {
        val list = ReadingList("", null)
        assertNotNull(list.title)
    }

    @Test
    fun testReadingListPageWithAllFields() {
        val wikiSite = WikiSite.forLanguageCode("en")
        val pageTitle = PageTitle("Test_Page", wikiSite)
        val page = ReadingListPage(pageTitle)
        assertEquals(wikiSite, page.wiki)
        assertNotNull(page.displayTitle)
    }
}
