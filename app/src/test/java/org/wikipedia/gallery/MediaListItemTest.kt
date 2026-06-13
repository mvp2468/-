package org.wikipedia.gallery

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaListItemTest {

    @Test
    fun testIsVideoTrueForVideoType() {
        val item = MediaListItem(title = "test", type = "video")
        assertTrue(item.isVideo)
    }

    @Test
    fun testIsVideoFalseForImageType() {
        val item = MediaListItem(title = "test", type = "image")
        assertFalse(item.isVideo)
    }

    @Test
    fun testIsVideoFalseForEmptyType() {
        val item = MediaListItem()
        assertFalse(item.isVideo)
    }

    @Test
    fun testIsInCommonsWhenSrcContainsCommonsFragment() {
        val srcSet = MediaListItem.ImageSrcSet(
            _scale = "1x",
            src = "https://upload.wikimedia.org/wikipedia/commons/a/b/file.jpg"
        )
        val item = MediaListItem(title = "test", srcSets = listOf(srcSet))
        assertTrue(item.isInCommons)
    }

    @Test
    fun testIsInCommonsFalseWhenSrcDoesNotContainCommons() {
        val srcSet = MediaListItem.ImageSrcSet(
            _scale = "1x",
            src = "https://example.com/image.jpg"
        )
        val item = MediaListItem(title = "test", srcSets = listOf(srcSet))
        assertFalse(item.isInCommons)
    }

    @Test
    fun testIsInCommonsFalseForEmptySrcSets() {
        val item = MediaListItem()
        assertFalse(item.isInCommons)
    }

    @Test
    fun testImageSrcSetScaleWithNoX() {
        val srcSet = MediaListItem.ImageSrcSet(_scale = "1", src = "")
        assertEquals(1.0f, srcSet.scale)
    }

    @Test
    fun testImageSrcSetScaleWithX() {
        val srcSet = MediaListItem.ImageSrcSet(_scale = "2x", src = "")
        assertEquals(2.0f, srcSet.scale)
    }

    @Test
    fun testImageSrcSetScaleWithDecimalX() {
        val srcSet = MediaListItem.ImageSrcSet(_scale = "1.5x", src = "")
        assertEquals(1.5f, srcSet.scale)
    }

    @Test
    fun testImageSrcSetScaleNullReturnsZero() {
        val srcSet = MediaListItem.ImageSrcSet(_scale = null, src = "")
        assertEquals(0f, srcSet.scale)
    }

    @Test
    fun testGetImageUrlReturnsFirstSrcWhenOnlyOne() {
        val srcSet = MediaListItem.ImageSrcSet(_scale = "1x", src = "https://example.com/image.jpg")
        val item = MediaListItem(title = "test", srcSets = listOf(srcSet))
        assertEquals("https://example.com/image.jpg", item.getImageUrl(1.0f))
    }

    @Test
    fun testGetImageUrlSelectsBestScale() {
        val srcSet1 = MediaListItem.ImageSrcSet(_scale = "1x", src = "https://example.com/small.jpg")
        val srcSet2 = MediaListItem.ImageSrcSet(_scale = "2x", src = "https://example.com/large.jpg")
        val srcSet3 = MediaListItem.ImageSrcSet(_scale = "3x", src = "https://example.com/xlarge.jpg")
        val item = MediaListItem(title = "test", srcSets = listOf(srcSet1, srcSet2, srcSet3))
        assertEquals("https://example.com/large.jpg", item.getImageUrl(2.0f))
    }
}
