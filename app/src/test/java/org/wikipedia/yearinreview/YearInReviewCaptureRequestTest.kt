package org.wikipedia.yearinreview

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class YearInReviewCaptureRequestTest {

    @Test
    fun testStandardScreenIsSealedSubclass() {
        val screenData = YearInReviewScreenData.StandardScreen(slideName = "test")
        val request = YearInReviewCaptureRequest.StandardScreen(screenData)
        assertTrue(request is YearInReviewCaptureRequest)
        assertEquals(screenData, request.screenData)
    }

    @Test
    fun testGeoScreenIsSealedSubclass() {
        val screenData = YearInReviewScreenData.StandardScreen(slideName = "test")
        val request = YearInReviewCaptureRequest.GeoScreen(screenData)
        assertTrue(request is YearInReviewCaptureRequest)
        assertEquals(screenData, request.screenData)
        assertNull(request.requestScreenshotBitmap)
    }

    @Test
    fun testGeoScreenWithScreenshotBitmap() {
        val screenData = YearInReviewScreenData.StandardScreen(slideName = "test")
        val callback: (Int, Int) -> android.graphics.Bitmap = { _, _ ->
            android.graphics.Bitmap.createBitmap(1, 1, android.graphics.Bitmap.Config.ARGB_8888)
        }
        val request = YearInReviewCaptureRequest.GeoScreen(screenData, callback)
        assertNotNull(request.requestScreenshotBitmap)
    }

    @Test
    fun testHighlightsScreenIsSealedSubclass() {
        val data = YearInReviewScreenData.HighlightsScreen(
            highlights = emptyList(),
            screenshotUrl = "",
            slideName = "test"
        )
        val request = YearInReviewCaptureRequest.HighlightsScreen(data)
        assertTrue(request is YearInReviewCaptureRequest)
        assertEquals(data, request.data)
    }
}
