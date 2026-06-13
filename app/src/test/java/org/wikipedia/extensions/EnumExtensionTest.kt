package org.wikipedia.extensions

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.feed.model.CardType

@RunWith(RobolectricTestRunner::class)
class EnumExtensionTest {

    @Test
    fun testGetByCodeValid() {
        val result = CardType.entries.getByCode(0)
        assertEquals(CardType.SEARCH_BAR, result)
    }

    @Test
    fun testGetByCodeAnotherValid() {
        val result = CardType.entries.getByCode(2)
        assertEquals(CardType.BECAUSE_YOU_READ_LIST, result)
    }

    @Test
    fun testGetByCodeThrowsForInvalidCode() {
        try {
            CardType.entries.getByCode(-1)
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected
        }
    }

    @Test
    fun testGetByCodeThrowsForOutOfRangeCode() {
        try {
            CardType.entries.getByCode(999)
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected
        }
    }
}