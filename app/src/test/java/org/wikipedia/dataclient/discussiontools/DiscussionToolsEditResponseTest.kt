package org.wikipedia.dataclient.discussiontools

import org.junit.Assert.*
import org.junit.Test

class DiscussionToolsEditResponseTest {

    @Test
    fun testDefaultValues() {
        val response = DiscussionToolsEditResponse()
        assertNull(response.result)
    }

    @Test
    fun testEditResultDefaults() {
        val result = DiscussionToolsEditResponse.EditResult()
        assertEquals("", result.result)
        assertEquals("", result.content)
        assertEquals(0L, result.newRevId)
        assertFalse(result.watched)
    }

    @Test
    fun testEditResultWithValues() {
        val result = DiscussionToolsEditResponse.EditResult(
            result = "success",
            content = "<p>Hello</p>",
            newRevId = 12345L,
            watched = true
        )
        assertEquals("success", result.result)
        assertEquals("<p>Hello</p>", result.content)
        assertEquals(12345L, result.newRevId)
        assertTrue(result.watched)
    }
}
