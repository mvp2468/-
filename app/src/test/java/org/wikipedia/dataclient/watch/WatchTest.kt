package org.wikipedia.dataclient.watch

import org.junit.Assert.*
import org.junit.Test

class WatchTest {

    @Test
    fun testDefaultValues() {
        val watch = Watch(title = "Test")
        assertEquals("Test", watch.title)
        assertEquals(0, watch.ns)
        assertEquals(0, watch.pageid)
        assertNull(watch.expiry)
        assertFalse(watch.watched)
        assertFalse(watch.unwatched)
        assertFalse(watch.missing)
    }

    @Test
    fun testWatchedPage() {
        val watch = Watch(
            title = "Wikipedia:Test",
            ns = 4,
            pageid = 12345,
            watched = true
        )
        assertEquals("Wikipedia:Test", watch.title)
        assertEquals(4, watch.ns)
        assertEquals(12345, watch.pageid)
        assertTrue(watch.watched)
        assertFalse(watch.unwatched)
    }

    @Test
    fun testUnwatchedPage() {
        val watch = Watch(
            title = "Main Page",
            ns = 0,
            pageid = 1,
            unwatched = true
        )
        assertTrue(watch.unwatched)
        assertFalse(watch.watched)
    }

    @Test
    fun testMissingPage() {
        val watch = Watch(
            title = "NonExistent",
            missing = true
        )
        assertTrue(watch.missing)
    }

    @Test
    fun testWithExpiry() {
        val watch = Watch(
            title = "Test",
            expiry = "2024-12-31T23:59:59Z",
            watched = true
        )
        assertEquals("2024-12-31T23:59:59Z", watch.expiry)
    }
}
