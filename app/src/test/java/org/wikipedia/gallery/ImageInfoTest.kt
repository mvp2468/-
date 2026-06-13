package org.wikipedia.gallery

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageInfoTest {

    @Test
    fun testDefaultProperties() {
        val info = ImageInfo()
        assertEquals("", info.source)
        assertEquals("", info.commonsUrl)
        assertEquals("", info.thumbUrl)
        assertEquals(0, info.thumbWidth)
        assertEquals(0, info.thumbHeight)
        assertEquals("", info.originalUrl)
        assertEquals("*/*", info.mime)
        assertEquals("", info.user)
        assertEquals("", info.timestamp)
        assertEquals(0, info.size)
        assertEquals(0, info.width)
        assertEquals(0, info.height)
    }

    @Test
    fun testGetBestDerivativeForSizeEmptyDerivativesReturnsNull() {
        val info = ImageInfo()
        assertNull(info.getBestDerivativeForSize(100))
    }

    @Test
    fun testGetBestDerivativeForSizeWithDerivatives() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "thumburl": "https://example.com/thumb.jpg",
            "thumbwidth": 200,
            "thumbheight": 150,
            "url": "https://example.com/original.jpg",
            "derivatives": [
                {"src": "small.jpg", "type": "image/jpeg", "width": 100},
                {"src": "medium.jpg", "type": "image/jpeg", "width": 300},
                {"src": "large.jpg", "type": "image/jpeg", "width": 600}
            ]
        }
        """.trimIndent()
        val info = json.decodeFromString<ImageInfo>(jsonStr)
        val result = info.getBestDerivativeForSize(400)
        assertNotNull(result)
        assertEquals("medium.jpg", result!!.src)
        assertEquals(300, result.width)
    }

    @Test
    fun testGetBestDerivativeForSizeExcludesOggTypes() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "derivatives": [
                {"src": "video.ogg", "type": "video/ogg", "width": 200},
                {"src": "video.ogv", "type": "video/ogv", "width": 300},
                {"src": "image.jpg", "type": "image/jpeg", "width": 250}
            ]
        }
        """.trimIndent()
        val info = json.decodeFromString<ImageInfo>(jsonStr)
        val result = info.getBestDerivativeForSize(400)
        assertNotNull(result)
        assertEquals("image.jpg", result!!.src)
    }

    @Test
    fun testGetBestDerivativeForSizeReturnsNullWhenAllTooLarge() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "derivatives": [
                {"src": "large.jpg", "type": "image/jpeg", "width": 500}
            ]
        }
        """.trimIndent()
        val info = json.decodeFromString<ImageInfo>(jsonStr)
        assertNull(info.getBestDerivativeForSize(100))
    }

    @Test
    fun testGetMetadataTranslationsEmptyByDefault() {
        val info = ImageInfo()
        assertTrue(info.getMetadataTranslations().isEmpty())
    }

    @Test
    fun testDeserializeBasicProperties() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """
        {
            "thumburl": "https://example.com/thumb.jpg",
            "thumbwidth": 320,
            "thumbheight": 240,
            "url": "https://example.com/original.jpg",
            "mime": "image/jpeg",
            "user": "TestUser",
            "size": 102400,
            "width": 1920,
            "height": 1080
        }
        """.trimIndent()
        val info = json.decodeFromString<ImageInfo>(jsonStr)
        assertEquals("https://example.com/thumb.jpg", info.thumbUrl)
        assertEquals(320, info.thumbWidth)
        assertEquals(240, info.thumbHeight)
        assertEquals("https://example.com/original.jpg", info.originalUrl)
        assertEquals("image/jpeg", info.mime)
        assertEquals("TestUser", info.user)
        assertEquals(102400, info.size)
        assertEquals(1920, info.width)
        assertEquals(1080, info.height)
    }
}
