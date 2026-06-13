package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.mwapi.MwException
import org.wikipedia.dataclient.mwapi.MwServiceError
import org.wikipedia.dataclient.okhttp.HttpStatusException
import org.wikipedia.login.LoginFailedException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

@RunWith(RobolectricTestRunner::class)
class ThrowableUtilTest {

    @Test
    fun testGetInnermostThrowableWithNoCause() {
        val ex = RuntimeException("Top level")
        assertEquals(ex, ThrowableUtil.getInnermostThrowable(ex))
    }

    @Test
    fun testGetInnermostThrowableWithSingleCause() {
        val cause = IllegalArgumentException("Root cause")
        val ex = RuntimeException("Wrapper", cause)
        assertEquals(cause, ThrowableUtil.getInnermostThrowable(ex))
    }

    @Test
    fun testGetInnermostThrowableWithMultipleCauses() {
        val deepest = IllegalStateException("Deepest")
        val middle = RuntimeException("Middle", deepest)
        val top = Exception("Top", middle)
        assertEquals(deepest, ThrowableUtil.getInnermostThrowable(top))
    }

    @Test
    fun testGetInnermostThrowableWithLongChain() {
        var current: Throwable = NullPointerException("Deep")
        for (i in 0..4) {
            current = RuntimeException("Level $i", current)
        }
        val inner = ThrowableUtil.getInnermostThrowable(current)
        assertTrue(inner is NullPointerException)
    }

    @Test
    fun testIsOfflineWithUnknownHostException() {
        assertTrue(ThrowableUtil.isOffline(UnknownHostException()))
    }

    @Test
    fun testIsOfflineWithSocketException() {
        assertTrue(ThrowableUtil.isOffline(SocketException()))
    }

    @Test
    fun testIsOfflineWithOtherException() {
        assertFalse(ThrowableUtil.isOffline(RuntimeException()))
    }

    @Test
    fun testIsOfflineWithNull() {
        assertFalse(ThrowableUtil.isOffline(null))
    }

    @Test
    fun testIsTimeoutWithSocketTimeoutException() {
        assertTrue(ThrowableUtil.isTimeout(SocketTimeoutException()))
    }

    @Test
    fun testIsTimeoutWithOtherException() {
        assertFalse(ThrowableUtil.isTimeout(RuntimeException()))
    }

    @Test
    fun testIsTimeoutWithNull() {
        assertFalse(ThrowableUtil.isTimeout(null))
    }

    @Test
    fun testIs404WithMatchingCode() {
        val ex = HttpStatusException(404, "https://en.wikipedia.org/wiki/NotFound", "Not Found")
        assertTrue(ThrowableUtil.is404(ex))
    }

    @Test
    fun testIs404WithNonMatchingCode() {
        val ex = HttpStatusException(500, "https://en.wikipedia.org", "Server Error")
        assertFalse(ThrowableUtil.is404(ex))
    }

    @Test
    fun testIs404WithOtherException() {
        assertFalse(ThrowableUtil.is404(RuntimeException()))
    }

    @Test
    fun testIsEmptyException() {
        val ex = ThrowableUtil.EmptyException()
        assertTrue(ThrowableUtil.isEmptyException(ex))
    }

    @Test
    fun testIsEmptyExceptionWithOtherException() {
        assertFalse(ThrowableUtil.isEmptyException(RuntimeException()))
    }

    @Test
    fun testIsNetworkErrorWithUnknownHostException() {
        assertTrue(ThrowableUtil.isNetworkError(UnknownHostException()))
    }

    @Test
    fun testIsNetworkErrorWithTimeoutException() {
        assertTrue(ThrowableUtil.isNetworkError(TimeoutException()))
    }

    @Test
    fun testIsNetworkErrorWithSSLException() {
        assertTrue(ThrowableUtil.isNetworkError(SSLException("SSL error")))
    }

    @Test
    fun testIsNetworkErrorWithOtherException() {
        assertFalse(ThrowableUtil.isNetworkError(RuntimeException()))
    }

    @Test
    fun testIsNetworkErrorWithNestedException() {
        val ex = RuntimeException(UnknownHostException("No network"))
        assertTrue(ThrowableUtil.isNetworkError(ex))
    }

    @Test
    fun testIsNetworkErrorWithDeeplyNestedException() {
        val ex = Exception(IllegalStateException(SSLException("SSL")))
        assertTrue(ThrowableUtil.isNetworkError(ex))
    }

    @Test
    fun testIsNetworkErrorWithNestedUnknownHostAsTimeout() {
        // SocketTimeoutException does NOT extend java.util.concurrent.TimeoutException,
        // so it's not caught by isNetworkError. But UnknownHostException as a cause works.
        val ex = RuntimeException(UnknownHostException("No network"))
        assertTrue(ThrowableUtil.isNetworkError(ex))
    }

    @Test
    fun testIsNotLoggedInWithMwException() {
        val error = MwServiceError(code = "notloggedin", html = "Not logged in")
        val ex = MwException(error)
        assertTrue(ThrowableUtil.isNotLoggedIn(ex))
    }

    @Test
    fun testIsNotLoggedInWithDifferentCode() {
        val error = MwServiceError(code = "badtoken", html = "Bad token")
        val ex = MwException(error)
        assertFalse(ThrowableUtil.isNotLoggedIn(ex))
    }

    @Test
    fun testIsNotLoggedInWithNullCode() {
        val error = MwServiceError(code = null)
        val ex = MwException(error)
        assertFalse(ThrowableUtil.isNotLoggedIn(ex))
    }

    @Test
    fun testIsNotLoggedInWithOtherException() {
        assertFalse(ThrowableUtil.isNotLoggedIn(RuntimeException()))
    }

    @Test
    fun testIsNotLoggedInWithNull() {
        assertFalse(ThrowableUtil.isNotLoggedIn(null))
    }

    @Test
    fun testAppErrorDefaultValues() {
        val error = ThrowableUtil.AppError("Error message", "Detail message")
        assertEquals("Error message", error.error)
        assertEquals("Detail message", error.detail)
    }

    @Test
    fun testAppErrorWithNullDetail() {
        val error = ThrowableUtil.AppError("Error message", null)
        assertEquals("Error message", error.error)
        assertNull(error.detail)
    }
}
