package org.wikipedia.dataclient.growthtasks

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.*
import org.junit.Test

class GrowthUserImpactTest {

    @Test
    fun testDefaultConstructor() {
        val impact = GrowthUserImpact()
        assertEquals(0, impact.userId)
        assertEquals("", impact.userName)
        assertEquals(0, impact.totalEditsCount)
        assertTrue(impact.editCountByNamespace.isEmpty())
        assertTrue(impact.editCountByDay.isEmpty())
        assertNull(impact.longestEditingStreak)
        assertTrue(impact.dailyTotalViews.isEmpty())
        assertTrue(impact.topViewedArticles.isEmpty())
    }

    @Test
    fun testEditCountByNamespaceParsing() {
        val namespaceJson = buildJsonObject {
            put("0", 10)
            put("1", 5)
        }
        val impact = GrowthUserImpact(mEditCountByNamespace = namespaceJson)
        assertEquals(2, impact.editCountByNamespace.size)
        assertEquals(10, impact.editCountByNamespace[0])
        assertEquals(5, impact.editCountByNamespace[1])
    }

    @Test
    fun testEditCountByDayParsing() {
        val dayJson = buildJsonObject {
            put("2024-06-01", 3)
            put("2024-06-02", 7)
            put("2024-06-03", 2)
        }
        val impact = GrowthUserImpact(mEditCountByDay = dayJson)
        assertEquals(3, impact.editCountByDay.size)
        assertEquals(7, impact.editCountByDay["2024-06-02"])
    }

    @Test
    fun testEditCountByTaskTypeParsing() {
        val taskJson = buildJsonObject {
            put("copyedit", 15)
            put("links", 8)
        }
        val impact = GrowthUserImpact(mEditCountByTaskType = taskJson)
        assertEquals(2, impact.editCountByTaskType.size)
        assertEquals(15, impact.editCountByTaskType["copyedit"])
    }

    @Test
    fun testGroupEditsByMonth() {
        val dayJson = buildJsonObject {
            put("2024-06-01", 5)
            put("2024-06-15", 3)
            put("2024-07-01", 10)
            put("2024-07-02", 7)
        }
        val impact = GrowthUserImpact(mEditCountByDay = dayJson)
        val monthlyEdits = impact.groupEditsByMonth
        assertEquals(2, monthlyEdits.size)
        assertEquals(8, monthlyEdits["2024-06"])
        assertEquals(17, monthlyEdits["2024-07"])
    }

    @Test
    fun testEmptyJsonElementsProduceEmptyMaps() {
        // When JSON is an empty array (not JsonObject), it should produce empty maps
        val impact = GrowthUserImpact(
            mEditCountByNamespace = null,
            mEditCountByDay = null,
            mDailyTotalViews = null,
            mTopViewedArticles = null
        )
        assertTrue(impact.editCountByNamespace.isEmpty())
        assertTrue(impact.editCountByDay.isEmpty())
        assertTrue(impact.dailyTotalViews.isEmpty())
        assertTrue(impact.topViewedArticles.isEmpty())
    }

    @Test
    fun testLongestEditingStreakParsing() {
        val streakJson = buildJsonObject {
            put("totalEditCountForPeriod", 42)
            putJsonObject("datePeriod") {
                put("start", "2024-01-01")
                put("end", "2024-01-15")
                put("days", 15)
            }
        }
        val impact = GrowthUserImpact(mLongestEditingStreak = streakJson)
        val streak = impact.longestEditingStreak
        assertNotNull(streak)
        assertEquals(42, streak!!.totalEditCountForPeriod)
        assertEquals("2024-01-01", streak.datePeriod!!.start)
        assertEquals("2024-01-15", streak.datePeriod!!.end)
        assertEquals(15, streak.datePeriod!!.days)
    }

    @Test
    fun testArticleViewsParsing() {
        val viewsJson = buildJsonObject {
            put("2024-06-01", 100)
            put("2024-06-02", 200)
        }
        val articleViews = GrowthUserImpact.ArticleViews(
            firstEditDate = "2024-01-01",
            newestEdit = "2024-06-01",
            imageUrl = "https://example.com/image.jpg",
            viewsCount = 300,
            views = viewsJson
        )
        assertEquals("2024-01-01", articleViews.firstEditDate)
        assertEquals(300L, articleViews.viewsCount)
        assertEquals(100, articleViews.viewsByDay["2024-06-01"])
    }

    @Test
    fun testLastThirtyDaysEdits() {
        val today = java.time.LocalDate.now()
        val yesterdayStr = today.minusDays(1).toString()
        val dayJson = buildJsonObject {
            put(yesterdayStr, 5)
        }
        val impact = GrowthUserImpact(mEditCountByDay = dayJson)
        val thirtyDays = impact.lastThirtyDaysEdits
        assertEquals(yesterdayStr to 5, thirtyDays.entries.first { it.value > 0 }.toPair())
    }
}
