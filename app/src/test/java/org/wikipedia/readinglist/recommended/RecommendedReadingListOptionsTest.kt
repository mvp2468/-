package org.wikipedia.readinglist.recommended

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RecommendedReadingListOptionsTest {

    @Test
    fun testUpdateFrequencyHasThreeValues() {
        val frequencies = RecommendedReadingListUpdateFrequency.entries
        assertEquals(3, frequencies.size)
    }

    @Test
    fun testUpdateFrequencyOrdinals() {
        assertEquals(0, RecommendedReadingListUpdateFrequency.DAILY.ordinal)
        assertEquals(1, RecommendedReadingListUpdateFrequency.WEEKLY.ordinal)
        assertEquals(2, RecommendedReadingListUpdateFrequency.MONTHLY.ordinal)
    }

    @Test
    fun testUpdateFrequencyStringResources() {
        assertTrue(RecommendedReadingListUpdateFrequency.DAILY.displayStringRes != 0)
        assertTrue(RecommendedReadingListUpdateFrequency.WEEKLY.dialogStringRes != 0)
        assertTrue(RecommendedReadingListUpdateFrequency.MONTHLY.snackbarStringRes != 0)
    }

    @Test
    fun testSourceHasAllEntries() {
        val sources = RecommendedReadingListSource.entries
        assertTrue(sources.contains(RecommendedReadingListSource.INTERESTS))
        assertTrue(sources.contains(RecommendedReadingListSource.READING_LIST))
        assertTrue(sources.contains(RecommendedReadingListSource.HISTORY))
    }
}
