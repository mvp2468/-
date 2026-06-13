package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageUrlUtilTest {

    @Test
    fun testIsGifReturnsTrueForGifUrl() {
        assertTrue(ImageUrlUtil.isGif("https://upload.wikimedia.org/image.gif"))
    }

    @Test
    fun testIsGifReturnsTrueForUpperCaseGif() {
        assertTrue(ImageUrlUtil.isGif("https://upload.wikimedia.org/image.GIF"))
    }

    @Test
    fun testIsGifReturnsFalseForPngUrl() {
        assertFalse(ImageUrlUtil.isGif("https://upload.wikimedia.org/image.png"))
    }

    @Test
    fun testIsGifReturnsFalseForNullUrl() {
        assertFalse(ImageUrlUtil.isGif(null))
    }

    @Test
    fun testIsGifReturnsFalseForEmptyUrl() {
        assertFalse(ImageUrlUtil.isGif(""))
    }

    @Test
    fun testGetUrlForPreferredSizeWithDifferentSize() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Test.jpg/200px-Test.jpg"
        val result = ImageUrlUtil.getUrlForPreferredSize(original, 400)
        assertTrue(result.contains("400px"))
        assertFalse(result.contains("200px"))
    }

    @Test
    fun testGetUrlForPreferredSizeWithSameSize() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Test.jpg/200px-Test.jpg"
        val result = ImageUrlUtil.getUrlForPreferredSize(original, 200)
        assertEquals(original, result)
    }

    @Test
    fun testGetUrlForPreferredSizeWithNoMatch() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/Test.jpg"
        val result = ImageUrlUtil.getUrlForPreferredSize(original, 200)
        assertEquals(original, result)
    }

    @Test
    fun testGetUrlForPreferredSizeWithPagePrefix() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Test.jpg/page1-200px-Test.jpg"
        val result = ImageUrlUtil.getUrlForPreferredSize(original, 400)
        assertTrue(result.contains("page1-400px"))
        assertFalse(result.contains("page1-200px"))
    }

    @Test
    fun testInsertLangIntoThumbUrl() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Test.jpg/200px-Test.jpg"
        val result = ImageUrlUtil.insertLangIntoThumbUrl(original, "en")
        assertTrue(result.contains("langen-200px"))
    }

    @Test
    fun testInsertLangIntoThumbUrlWithNoMatch() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/Test.jpg"
        val result = ImageUrlUtil.insertLangIntoThumbUrl(original, "en")
        assertEquals(original, result)
    }

    @Test
    fun testInsertLangIntoThumbUrlWithPagePrefix() {
        val original = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Test.jpg/page1-200px-Test.jpg"
        val result = ImageUrlUtil.insertLangIntoThumbUrl(original, "fr")
        assertTrue(result.contains("langfr-200px"))
    }
}
