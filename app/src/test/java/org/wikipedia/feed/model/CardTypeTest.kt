package org.wikipedia.feed.model

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CardTypeTest {

    @Test
    fun testCodeReturnsCorrectValue() {
        assertEquals(0, CardType.SEARCH_BAR.code())
        assertEquals(4, CardType.FEATURED_ARTICLE.code())
        assertEquals(24, CardType.WIKI_GAMES.code())
    }

    @Test
    fun testOfReturnsCorrectCardType() {
        assertEquals(CardType.SEARCH_BAR, CardType.of(0))
        assertEquals(CardType.BECAUSE_YOU_READ_LIST, CardType.of(2))
        assertEquals(CardType.WIKI_GAMES, CardType.of(24))
        assertEquals(CardType.DAY_HEADER, CardType.of(97))
        assertEquals(CardType.OFFLINE, CardType.of(98))
        assertEquals(CardType.PROGRESS, CardType.of(99))
    }

    @Test
    fun testContentTypeForCardWithFeedContentType() {
        assertEquals(org.wikipedia.feed.FeedContentType.FEATURED_ARTICLE, CardType.FEATURED_ARTICLE.contentType())
        assertEquals(org.wikipedia.feed.FeedContentType.TOP_READ_ARTICLES, CardType.TOP_READ_LIST.contentType())
        assertEquals(org.wikipedia.feed.FeedContentType.WIKI_GAMES, CardType.WIKI_GAMES.contentType())
    }

    @Test
    fun testContentTypeForCardWithoutFeedContentType() {
        assertNull(CardType.SEARCH_BAR.contentType())
        assertNull(CardType.MOST_READ_ITEM.contentType())
    }

    @Test
    fun testNewViewDefaultThrowsUnsupportedOperationException() {
        val nonAbstractType = CardType.MOST_READ_ITEM
        try {
            nonAbstractType.newView(org.robolectric.RuntimeEnvironment.getApplication())
            fail("Expected UnsupportedOperationException")
        } catch (e: UnsupportedOperationException) {
            // expected
        }
    }
}
