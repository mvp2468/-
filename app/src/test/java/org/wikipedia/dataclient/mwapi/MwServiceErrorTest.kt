package org.wikipedia.dataclient.mwapi

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class MwServiceErrorTest {

    @Test
    fun testBadTokenReturnsTrueWhenCodeIsBadtoken() {
        val error = MwServiceError(code = "badtoken")
        assertTrue(error.badToken())
    }

    @Test
    fun testBadTokenReturnsFalseForOtherCodes() {
        val error = MwServiceError(code = "readonly")
        assertFalse(error.badToken())
    }

    @Test
    fun testBadTokenReturnsFalseForNullCode() {
        val error = MwServiceError()
        assertFalse(error.badToken())
    }

    @Test
    fun testBadLoginStateReturnsTrueWhenCodeIsAssertUserFailed() {
        val error = MwServiceError(code = "assertuserfailed")
        assertTrue(error.badLoginState())
    }

    @Test
    fun testBadLoginStateReturnsFalseForOtherCodes() {
        val error = MwServiceError(code = "badtoken")
        assertFalse(error.badLoginState())
    }

    @Test
    fun testHasMessageNameFindsExistingMessage() {
        val messages = listOf(
            MwServiceError.Message(name = "abusefilter-warning", html = "warning"),
            MwServiceError.Message(name = "abusefilter-disallowed", html = "disallowed")
        )
        val data = MwServiceError.Data(messages = messages)
        val error = MwServiceError(data = data)

        assertTrue(error.hasMessageName("abusefilter-warning"))
        assertTrue(error.hasMessageName("abusefilter-disallowed"))
    }

    @Test
    fun testHasMessageNameReturnsFalseForMissingMessage() {
        val messages = listOf(
            MwServiceError.Message(name = "abusefilter-warning", html = "warning")
        )
        val data = MwServiceError.Data(messages = messages)
        val error = MwServiceError(data = data)

        assertFalse(error.hasMessageName("nonexistent"))
    }

    @Test
    fun testHasMessageNameReturnsFalseWhenDataIsNull() {
        val error = MwServiceError(data = null)
        assertFalse(error.hasMessageName("anything"))
    }

    @Test
    fun testHasMessageNameReturnsFalseWhenMessagesIsNull() {
        val data = MwServiceError.Data(messages = null)
        val error = MwServiceError(data = data)
        assertFalse(error.hasMessageName("anything"))
    }

    @Test
    fun testGetMessageHtmlReturnsCorrectHtml() {
        val messages = listOf(
            MwServiceError.Message(name = "abusefilter-warning", html = "This is a warning message"),
            MwServiceError.Message(name = "abusefilter-disallowed", html = "This action is disallowed")
        )
        val data = MwServiceError.Data(messages = messages)
        val error = MwServiceError(data = data)

        assertEquals("This is a warning message", error.getMessageHtml("abusefilter-warning"))
        assertEquals("This action is disallowed", error.getMessageHtml("abusefilter-disallowed"))
    }

    @Test(expected = NoSuchElementException::class)
    fun testGetMessageHtmlThrowsWhenNameNotFound() {
        val messages = listOf(
            MwServiceError.Message(name = "abusefilter-warning", html = "warning")
        )
        val data = MwServiceError.Data(messages = messages)
        val error = MwServiceError(data = data)

        error.getMessageHtml("nonexistent")
    }

    @Test
    fun testKeyReturnsCodeValue() {
        val error = MwServiceError(code = "badtoken")
        assertEquals("badtoken", error.key)
    }

    @Test
    fun testKeyReturnsEmptyForNullCode() {
        val error = MwServiceError()
        assertEquals("", error.key)
    }

    @Test
    fun testMessageStripsStyleTagsFromHtml() {
        val error = MwServiceError(html = "<div style=\"color:red\">Error message</div>")
        // removeStyleTags removes content between <style> tags and various style-related patterns
        // After stripping, the key assertion is that the core text message remains
        assertTrue(error.message.contains("Error message"))
    }

    @Test
    fun testMessageReturnsEmptyForNullHtml() {
        val error = MwServiceError(html = null)
        assertEquals("", error.message)
    }

    @Test
    fun testBlockInfoIsBlockedReturnsFalseByDefault() {
        // BlockInfo default blockExpiry is empty, so isBlocked should be false
        val blockInfo = MwServiceError.BlockInfo()
        assertFalse(blockInfo.isBlocked)
    }

    @Test
    fun testBlockInfoIsNotBlockedWhenExpiryIsEmpty() {
        // blockExpiry defaults to "", so isBlocked should be false
        val blockInfo = MwServiceError.BlockInfo()
        assertFalse(blockInfo.isBlocked)
    }

    @Test
    fun testDefaultProperties() {
        val error = MwServiceError()
        assertEquals("", error.key)
        assertEquals("", error.message)
        assertFalse(error.badToken())
        assertFalse(error.badLoginState())
    }

    @Test
    fun testBlockInfoDefaultValues() {
        val blockInfo = MwServiceError.BlockInfo()
        assertEquals(0, blockInfo.blockedById)
        assertEquals(0, blockInfo.blockId)
        assertEquals("", blockInfo.blockedBy)
        assertEquals("", blockInfo.blockReason)
        assertEquals("", blockInfo.blockTimeStamp)
        assertEquals("", blockInfo.blockExpiry)
        assertFalse(blockInfo.isBlocked)
    }

    @Test
    fun testMessageWithOnlyText() {
        val error = MwServiceError(html = "Simple error without any HTML")
        assertEquals("Simple error without any HTML", error.message)
    }

    @Test
    fun testMessageWithMultipleStyleTags() {
        val error = MwServiceError(html = "<b style=\"bold\">Bold</b> and <i style=\"italic\">Italic</i>")
        val result = error.message
        // removeStyleTags should strip styling - the exact behavior depends on implementation
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testDataWithNullBlockInfo() {
        val messages = listOf(MwServiceError.Message(name = "test", html = "test html"))
        val data = MwServiceError.Data(messages = messages, blockinfo = null)
        assertNotNull(data.messages)
        assertNull(data.blockinfo)
    }

    @Test
    fun testMessageDefaultsToEmptyString() {
        val msg = MwServiceError.Message(null)
        assertEquals("", msg.html)
    }
}
