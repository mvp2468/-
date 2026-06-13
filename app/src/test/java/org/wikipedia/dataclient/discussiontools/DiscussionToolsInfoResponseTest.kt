package org.wikipedia.dataclient.discussiontools

import org.junit.Assert.*
import org.junit.Test

class DiscussionToolsInfoResponseTest {

    @Test
    fun testDefaultValues() {
        val response = DiscussionToolsInfoResponse()
        assertNull(response.pageInfo)
    }

    @Test
    fun testPageInfoDefaults() {
        val info = DiscussionToolsInfoResponse.PageInfo()
        assertTrue(info.threads.isEmpty())
    }
}
