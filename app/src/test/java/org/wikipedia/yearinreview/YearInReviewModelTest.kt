package org.wikipedia.yearinreview

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class YearInReviewModelTest {

    @Test
    fun testDeserializeFromJson() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "totalReadingTimeMinutes": 120,
            "localReadingArticlesCount": 25,
            "localSavedArticlesCount": 10,
            "localSavedArticles": ["Article1", "Article2"],
            "localTopVisitedArticles": ["Top1"],
            "localTopCategories": ["Science", "History"],
            "favoriteTimeToRead": 14,
            "favoriteDayToRead": 3,
            "favoriteMonthDidMostReading": 6,
            "largestClusterLocation": {"first": 40.7128, "second": -74.0060},
            "largestClusterTopLeft": {"first": 40.7200, "second": -74.0100},
            "largestClusterBottomRight": {"first": 40.7000, "second": -74.0000},
            "largestClusterCountryName": "United States",
            "largestClusterArticles": ["NYC"],
            "userEditsCount": 5,
            "userEditsViewedTimes": 1000,
            "isCustomIconUnlocked": false
        }
        """.trimIndent()
        val model = json.decodeFromString<YearInReviewModel>(jsonStr)
        assertEquals(120L, model.totalReadingTimeMinutes)
        assertEquals(25, model.localReadingArticlesCount)
        assertEquals(10, model.localSavedArticlesCount)
        assertEquals(listOf("Article1", "Article2"), model.localSavedArticles)
        assertEquals(14, model.favoriteTimeToRead)
        assertEquals(3, model.favoriteDayToRead)
        assertEquals(6, model.favoriteMonthDidMostReading)
        assertEquals("United States", model.largestClusterCountryName)
        assertEquals(5, model.userEditsCount)
        assertEquals(1000L, model.userEditsViewedTimes)
        assertFalse(model.isCustomIconUnlocked)
    }

    @Test
    fun testDefaultValues() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "totalReadingTimeMinutes": 0,
            "localReadingArticlesCount": 0,
            "localSavedArticlesCount": 0,
            "localSavedArticles": [],
            "localTopVisitedArticles": [],
            "localTopCategories": [],
            "favoriteTimeToRead": 0,
            "favoriteDayToRead": 0,
            "favoriteMonthDidMostReading": 0,
            "largestClusterLocation": {"first": 0.0, "second": 0.0},
            "largestClusterTopLeft": {"first": 0.0, "second": 0.0},
            "largestClusterBottomRight": {"first": 0.0, "second": 0.0},
            "largestClusterCountryName": "",
            "largestClusterArticles": [],
            "userEditsCount": 0,
            "userEditsViewedTimes": 0,
            "isCustomIconUnlocked": false
        }
        """.trimIndent()
        val model = json.decodeFromString<YearInReviewModel>(jsonStr)
        assertEquals(0, model.slideViewedCount)
        assertFalse(model.isReadingListDialogShown)
        assertFalse(model.isReadingListCreated)
    }

    @Test
    fun testSlideViewedCountCustomValue() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "totalReadingTimeMinutes": 0,
            "localReadingArticlesCount": 0,
            "localSavedArticlesCount": 0,
            "localSavedArticles": [],
            "localTopVisitedArticles": [],
            "localTopCategories": [],
            "favoriteTimeToRead": 0,
            "favoriteDayToRead": 0,
            "favoriteMonthDidMostReading": 0,
            "largestClusterLocation": {"first": 0.0, "second": 0.0},
            "largestClusterTopLeft": {"first": 0.0, "second": 0.0},
            "largestClusterBottomRight": {"first": 0.0, "second": 0.0},
            "largestClusterCountryName": "",
            "largestClusterArticles": [],
            "userEditsCount": 0,
            "userEditsViewedTimes": 0,
            "isCustomIconUnlocked": false,
            "slideViewedCount": 5,
            "isReadingListDialogShown": true,
            "isReadingListCreated": true
        }
        """.trimIndent()
        val model = json.decodeFromString<YearInReviewModel>(jsonStr)
        assertEquals(5, model.slideViewedCount)
        assertTrue(model.isReadingListDialogShown)
        assertTrue(model.isReadingListCreated)
    }

    @Test
    fun testLargestClusterLocation() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "totalReadingTimeMinutes": 0, "localReadingArticlesCount": 0, "localSavedArticlesCount": 0,
            "localSavedArticles": [], "localTopVisitedArticles": [], "localTopCategories": [],
            "favoriteTimeToRead": 0, "favoriteDayToRead": 0, "favoriteMonthDidMostReading": 0,
            "largestClusterLocation": {"first": 35.6895, "second": 139.6917},
            "largestClusterTopLeft": {"first": 35.70, "second": 139.68},
            "largestClusterBottomRight": {"first": 35.68, "second": 139.70},
            "largestClusterCountryName": "Japan",
            "largestClusterArticles": ["Tokyo"],
            "userEditsCount": 0, "userEditsViewedTimes": 0, "isCustomIconUnlocked": false
        }
        """.trimIndent()
        val model = json.decodeFromString<YearInReviewModel>(jsonStr)
        assertEquals(35.6895, model.largestClusterLocation.first, 0.0001)
        assertEquals(139.6917, model.largestClusterLocation.second, 0.0001)
    }
}
