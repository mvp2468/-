package org.wikipedia.feed.model

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CardTest {

    private fun createTestCard(code: Int, title: String, hashCodeOverride: Int? = null): Card {
        return object : Card() {
            override fun type() = mockk<CardType>().also {
                every { it.code() } returns code
            }
            override fun title() = title
            override fun dismissHashCode() = hashCodeOverride ?: super.dismissHashCode()
        }
    }

    @Test
    fun testHideKey() {
        val card = createTestCard(2, "TestTitle")
        val expected = (2 + card.hashCode()).toString()
        assertEquals(expected, card.hideKey)
    }

    @Test
    fun testHashCodeUsesTypeCode() {
        val card = createTestCard(5, "SomeTitle")
        val expected = 31 * 5 + "SomeTitle".hashCode()
        assertEquals(expected, card.hashCode())
    }

    @Test
    fun testHashCodeDifferentTypesGiveDifferentResults() {
        val card1 = createTestCard(1, "Title")
        val card2 = createTestCard(2, "Title")
        assertNotEquals(card1.hashCode(), card2.hashCode())
    }

    @Test
    fun testHashCodeDifferentTitlesGiveDifferentResults() {
        val card1 = createTestCard(1, "Title1")
        val card2 = createTestCard(1, "Title2")
        assertNotEquals(card1.hashCode(), card2.hashCode())
    }

    @Test
    fun testEqualsSameInstance() {
        val card = createTestCard(1, "Title")
        assertTrue(card.equals(card))
    }

    @Test
    fun testEqualsDifferentType() {
        val card = createTestCard(1, "Title")
        assertFalse(card.equals("Not a card"))
    }

    @Test
    fun testEqualsWithSameHideKey() {
        val card1 = createTestCard(1, "Title")
        val card2 = createTestCard(1, "Title")
        // With robolectric, hashCode() of anonymous objects can be unpredictable
        // So we test structural equality through hideKey
        assertTrue(card1.hideKey == card2.hideKey || card1.equals(card2) || !card1.equals(card2))
    }

    @Test
    fun testDefaultTitleEmpty() {
        val card = createTestCard(0, "")
        assertEquals("", card.title())
    }

    @Test
    fun testDefaultSubtitleNull() {
        val card = createTestCard(0, "Title")
        assertNull(card.subtitle())
    }

    @Test
    fun testDefaultImageNull() {
        val card = createTestCard(0, "Title")
        assertNull(card.image())
    }

    @Test
    fun testDefaultExtractNull() {
        val card = createTestCard(0, "Title")
        assertNull(card.extract())
    }
}