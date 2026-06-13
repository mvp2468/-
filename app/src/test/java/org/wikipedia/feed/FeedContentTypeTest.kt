package org.wikipedia.feed

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FeedContentTypeTest {

    @Before
    fun setUp() {
        FeedContentType.entries.forEach {
            it.isEnabled = true
            it.langCodesSupported.clear()
            it.langCodesDisabled.clear()
            it.order = it.code()
        }
    }

    @Test
    fun testCodeReturnsCorrectValue() {
        assertEquals(6, FeedContentType.FEATURED_ARTICLE.code())
        assertEquals(0, FeedContentType.NEWS.code())
        assertEquals(3, FeedContentType.TOP_READ_ARTICLES.code())
    }

    @Test
    fun testSaveAndRestoreStateCycle() {
        FeedContentType.FEATURED_ARTICLE.isEnabled = false
        FeedContentType.NEWS.order = 99
        FeedContentType.TOP_READ_ARTICLES.langCodesSupported.addAll(listOf("en", "fr"))

        FeedContentType.saveState()
        FeedContentType.FEATURED_ARTICLE.isEnabled = true
        FeedContentType.NEWS.order = 0
        FeedContentType.TOP_READ_ARTICLES.langCodesSupported.clear()

        FeedContentType.restoreState()
        assertFalse(FeedContentType.FEATURED_ARTICLE.isEnabled)
        assertEquals(99, FeedContentType.NEWS.order)
        assertEquals(listOf("en", "fr"), FeedContentType.TOP_READ_ARTICLES.langCodesSupported)
    }

    @Test
    fun testRestoreStateWithEmptyLists() {
        FeedContentType.entries.forEach {
            it.isEnabled = true
            it.order = it.code()
            it.langCodesSupported.clear()
            it.langCodesDisabled.clear()
        }
        FeedContentType.saveState()
        FeedContentType.entries.forEach {
            it.isEnabled = false
            it.order = -1
        }
        FeedContentType.restoreState()
        FeedContentType.entries.forEach {
            assertTrue(it.isEnabled)
            assertEquals(it.code(), it.order)
        }
    }

    @Test
    fun testIsPerLanguage() {
        assertTrue(FeedContentType.FEATURED_ARTICLE.isPerLanguage)
        assertTrue(FeedContentType.NEWS.isPerLanguage)
        assertFalse(FeedContentType.BECAUSE_YOU_READ.isPerLanguage)
        assertFalse(FeedContentType.PLACES.isPerLanguage)
    }

    @Test
    fun testShowInConfig() {
        assertTrue(FeedContentType.FEATURED_ARTICLE.showInConfig)
        assertFalse(FeedContentType.ACCESSIBILITY.showInConfig)
    }

    @Test
    fun testSaveStateSavesLangDisabled() {
        FeedContentType.NEWS.langCodesDisabled.addAll(listOf("de", "ja"))
        FeedContentType.saveState()
        FeedContentType.NEWS.langCodesDisabled.clear()
        FeedContentType.restoreState()
        assertEquals(listOf("de", "ja"), FeedContentType.NEWS.langCodesDisabled)
    }
}
