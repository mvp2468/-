package org.wikipedia.extensions

import com.hcaptcha.sdk.HCaptchaException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.createaccount.CreateAccountException
import org.wikipedia.dataclient.mwapi.MwException
import org.wikipedia.dataclient.mwapi.MwServiceError
import org.wikipedia.dataclient.okhttp.HttpStatusException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@RunWith(RobolectricTestRunner::class)
class ThrowableExtensionsTest {

    @Test
    fun testHttpStatusException() {
        val ex = mockk<HttpStatusException>()
        every { ex.code } returns 404
        every { ex.message } returns null

        val context = ex.getInstrumentActionContext()
        assertEquals("404", context["code"])
        assertEquals("HttpStatusException", context["class"])
    }

    @Test
    fun testSocketTimeoutException() {
        val ex = mockk<SocketTimeoutException>()
        every { ex.message } returns "Connection timed out"

        val context = ex.getInstrumentActionContext()
        assertEquals("timeout", context["code"])
        assertEquals("SocketTimeoutException", context["class"])
    }

    @Test
    fun testUnknownHostException() {
        val ex = mockk<UnknownHostException>()
        every { ex.message } returns "Unable to resolve host"

        val context = ex.getInstrumentActionContext()
        assertEquals("network_unavailable", context["code"])
    }

    @Test
    fun testCreateAccountException() {
        val ex = mockk<CreateAccountException>()
        every { ex.messageCode } returns "some_error_code"
        every { ex.message } returns null

        val context = ex.getInstrumentActionContext()
        assertEquals("some_error_code", context["code"])
    }

    @Test
    fun testHCaptchaException() {
        val ex = mockk<HCaptchaException>()
        every { ex.statusCode } returns 400

        val context = ex.getInstrumentActionContext()
        assertEquals("400", context["code"])
        assertEquals("HCaptchaException", context["class"])
    }

    @Test
    fun testMwException() {
        val mwError = mockk<MwServiceError>()
        every { mwError.code } returns "protected-page"

        val ex = mockk<MwException>()
        every { ex.error } returns mwError

        val context = ex.getInstrumentActionContext()
        assertEquals("protected-page", context["code"])
    }

    @Test
    fun testGenericThrowableWithMessage() {
        val ex = RuntimeException("Something went wrong!")
        val context = ex.getInstrumentActionContext()
        assertEquals("RuntimeException", context["class"])
        assertEquals("Something went wrong!", context["message"])
    }

    @Test
    fun testGenericThrowableLongMessageTruncated() {
        val longMessage = "A".repeat(100)
        val ex = RuntimeException(longMessage)
        val context = ex.getInstrumentActionContext()
        assertTrue(context["message"]!!.length <= 64)
    }

    @Test
    fun testGenericThrowableNoMessage() {
        val ex = RuntimeException()
        val context = ex.getInstrumentActionContext()
        assertEquals("RuntimeException", context["class"])
        assertNull(context["code"])
        assertNull(context["message"])
    }

    @Test
    fun testHttpStatusExceptionWithMessageAndCode() {
        val ex = mockk<HttpStatusException>()
        every { ex.code } returns 500
        every { ex.message } returns "Server Error"

        val context = ex.getInstrumentActionContext()
        assertEquals("500", context["code"])
        // Code takes priority over message
        assertNull(context["message"])
    }

    @Test
    fun testMwExceptionEmptyCode() {
        val mwError = mockk<MwServiceError>()
        every { mwError.code } returns ""

        val ex = mockk<MwException>()
        every { ex.error } returns mwError
        every { ex.message } returns "Fallback message"

        val context = ex.getInstrumentActionContext()
        assertEquals("", context["code"])
        assertNull(context["message"]) // code key exists even if empty
    }
}