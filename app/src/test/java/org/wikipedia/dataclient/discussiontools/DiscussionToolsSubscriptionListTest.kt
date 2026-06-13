package org.wikipedia.dataclient.discussiontools

import org.junit.Assert.*
import org.junit.Test

class DiscussionToolsSubscriptionListTest {

    @Test
    fun testDefaultValues() {
        val list = DiscussionToolsSubscriptionList()
        assertTrue(list.subscriptions.isEmpty())
    }

    @Test
    fun testSubscriptionsIsEmptyMap() {
        // This class is usually populated via deserialization
        val list = DiscussionToolsSubscriptionList()
        assertEquals(0, list.subscriptions.size)
    }
}
