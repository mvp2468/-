package org.wikipedia.dataclient.watch

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WatchPostResponseTest {

    @Test
    fun testDefaultValues() {
        val response = WatchPostResponse()
        assertNull(response.batchcomplete)
        val watchList = response.watch
        assertNotNull(watchList)
        assertTrue(watchList!!.isEmpty())
        assertNull(response.batchcomplete)
    }

    @Test
    fun testWatchGetterReturnsEmptyOnNull() {
        // The watch field's getter returns emptyList() when backing field is null
        val response = WatchPostResponse()
        val watchList = response.watch
        assertNotNull(watchList)
        assertTrue(watchList!!.isEmpty())
    }
}
