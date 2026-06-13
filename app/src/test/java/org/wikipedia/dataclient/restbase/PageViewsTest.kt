package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class PageViewsTest {

    @Test
    fun testDefaultValues() {
        val views = PageViews()
        assertTrue(views.items.isEmpty())
    }

    @Test
    fun testItemDefaults() {
        val item = PageViews.Item()
        assertNull(item.timestamp)
        assertEquals(0L, item.viewCount)
        assertTrue(item.rankItems.isEmpty())
    }

    @Test
    fun testPageItemDefaults() {
        val item = PageViews.PageItem()
        assertEquals(0, item.rank)
        assertEquals("", item.wikiId)
        assertEquals(0L, item.pageId)
        assertEquals(0L, item.viewCount)
    }
}
