package org.wikipedia.offline.db

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OfflineObjectTest {

    @Test
    fun testUsedByWithEmptyString() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = ""
        )
        assertTrue(obj.usedBy.isEmpty())
    }

    @Test
    fun testUsedByWithSingleId() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|42|"
        )
        assertEquals(listOf(42L), obj.usedBy)
    }

    @Test
    fun testUsedByWithMultipleIds() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|1|2|3|"
        )
        assertEquals(listOf(1L, 2L, 3L), obj.usedBy)
    }

    @Test
    fun testAddUsedBy() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|10|"
        )
        obj.addUsedBy(20)
        assertEquals(listOf(10L, 20L), obj.usedBy)
    }

    @Test
    fun testAddUsedByDuplicateDoesNotAdd() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|10|20|"
        )
        obj.addUsedBy(10)
        assertEquals(listOf(10L, 20L), obj.usedBy)
        assertFalse(obj.usedByStr.contains("||"))
    }

    @Test
    fun testRemoveUsedBy() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|10|20|30|"
        )
        obj.removeUsedBy(20)
        assertEquals(listOf(10L, 30L), obj.usedBy)
    }

    @Test
    fun testRemoveUsedByNonExistentDoesNothing() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|10|20|"
        )
        obj.removeUsedBy(99)
        assertEquals(listOf(10L, 20L), obj.usedBy)
    }

    @Test
    fun testRemoveUsedByLastElement() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = "|10|"
        )
        obj.removeUsedBy(10)
        assertTrue(obj.usedBy.isEmpty())
    }

    @Test
    fun testAddUsedByToEmpty() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/wiki.zim",
            status = 1,
            usedByStr = ""
        )
        obj.addUsedBy(5)
        assertEquals(listOf(5L), obj.usedBy)
    }

    @Test
    fun testDefaultValues() {
        val obj = OfflineObject(
            url = "https://en.wikipedia.org/wiki/Test",
            lang = "en",
            path = "/sdcard/test.zim",
            status = 0
        )
        assertEquals(0, obj.id)
        assertEquals(0, obj.status)
        assertEquals("", obj.usedByStr)
        assertTrue(obj.usedBy.isEmpty())
    }
}
