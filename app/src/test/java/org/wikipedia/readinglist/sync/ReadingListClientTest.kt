package org.wikipedia.readinglist.sync

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.okhttp.HttpStatusException
import org.wikipedia.dataclient.mwapi.MwServiceError
import org.wikipedia.dataclient.WikiSite

@RunWith(RobolectricTestRunner::class)
class ReadingListClientTest {

    private val wiki = WikiSite.forLanguageCode("en")
    private val client = ReadingListClient(wiki)

    // ---- isErrorType ----

    @Test
    fun testIsErrorTypeMatchingKey() {
        val error = mockk<MwServiceError> {
            every { key } returns "already-set-up"
        }
        val exception = mockk<HttpStatusException> {
            every { serviceError } returns error
        }
        assertTrue(client.isErrorType(exception, "already-set-up"))
    }

    @Test
    fun testIsErrorTypeMatchingKeyContains() {
        val error = mockk<MwServiceError> {
            every { key } returns "readinglist-already-set-up-error"
        }
        val exception = mockk<HttpStatusException> {
            every { serviceError } returns error
        }
        assertTrue(client.isErrorType(exception, "already-set-up"))
    }

    @Test
    fun testIsErrorTypeNonMatchingKey() {
        val error = mockk<MwServiceError> {
            every { key } returns "different-error"
        }
        val exception = mockk<HttpStatusException> {
            every { serviceError } returns error
        }
        assertFalse(client.isErrorType(exception, "already-set-up"))
    }

    @Test
    fun testIsErrorTypeNullException() {
        assertFalse(client.isErrorType(null, "any-error"))
    }

    @Test
    fun testIsErrorTypeNonHttpStatusException() {
        val exception = RuntimeException("Not HTTP")
        assertFalse(client.isErrorType(exception, "any-error"))
    }

    @Test
    fun testIsErrorTypeNullServiceError() {
        val exception = mockk<HttpStatusException> {
            every { serviceError } returns null
        }
        assertFalse(client.isErrorType(exception, "any-error"))
    }

    // ---- isServiceError ----

    @Test
    fun testIsServiceErrorCode400() {
        val exception = mockk<HttpStatusException> {
            every { code } returns 400
        }
        assertTrue(client.isServiceError(exception))
    }

    @Test
    fun testIsServiceErrorCodeNot400() {
        val exception = mockk<HttpStatusException> {
            every { code } returns 500
        }
        assertFalse(client.isServiceError(exception))
    }

    @Test
    fun testIsServiceErrorNullException() {
        assertFalse(client.isServiceError(null))
    }

    @Test
    fun testIsServiceErrorNonHttpStatusException() {
        assertFalse(client.isServiceError(RuntimeException()))
    }

    // ---- isUnavailableError ----

    @Test
    fun testIsUnavailableErrorCode405() {
        val exception = mockk<HttpStatusException> {
            every { code } returns 405
        }
        assertTrue(client.isUnavailableError(exception))
    }

    @Test
    fun testIsUnavailableErrorCodeNot405() {
        val exception = mockk<HttpStatusException> {
            every { code } returns 404
        }
        assertFalse(client.isUnavailableError(exception))
    }

    @Test
    fun testIsUnavailableErrorNullException() {
        assertFalse(client.isUnavailableError(null))
    }

    @Test
    fun testIsUnavailableErrorNonHttpStatusException() {
        assertFalse(client.isUnavailableError(RuntimeException()))
    }

    // ---- constructor stores wiki ----

    @Test
    fun testLastDateHeaderInitiallyNull() {
        assertTrue(client.lastDateHeader == null)
    }
}
