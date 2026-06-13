package org.wikipedia.dataclient.discussiontools

import org.junit.Assert.*
import org.junit.Test

class DiscussionToolsSubscribeResponseTest {

    @Test
    fun testDefaultValues() {
        val response = DiscussionToolsSubscribeResponse()
        assertNull(response.status)
    }

    @Test
    fun testSubscribeStatusDefaults() {
        val status = DiscussionToolsSubscribeResponse.SubscribeStatus()
        assertEquals("", status.page)
        assertEquals("", status.topicName)
        assertFalse(status.subscribe)
    }

    @Test
    fun testSubscribeStatusSubscribed() {
        val status = DiscussionToolsSubscribeResponse.SubscribeStatus(
            page = "Talk:Test",
            topicName = "topic-123",
            subscribe = true
        )
        assertEquals("Talk:Test", status.page)
        assertEquals("topic-123", status.topicName)
        assertTrue(status.subscribe)
    }
}
