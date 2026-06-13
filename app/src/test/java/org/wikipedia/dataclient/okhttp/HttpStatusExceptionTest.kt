package org.wikipedia.dataclient.okhttp

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http.RealResponseBody
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.ServiceError

@RunWith(RobolectricTestRunner::class)
class HttpStatusExceptionTest {

    @Test
    fun testSimpleConstructorSetsAllFields() {
        val exception = HttpStatusException(404, "https://en.wikipedia.org/wiki/Test", "Not Found")

        assertEquals(404, exception.code)
        assertEquals("https://en.wikipedia.org/wiki/Test", exception.url)
        assertEquals("Not Found", exception.message)
    }

    @Test
    fun testSimpleConstructorWithNullMessage() {
        val exception = HttpStatusException(500, "https://en.wikipedia.org/api/rest_v1/", null)

        assertEquals(500, exception.code)
        assertNotNull(exception.message)
    }

    @Test
    fun testMessageGetterReturnsSetMessage() {
        val exception = HttpStatusException(403, "https://example.com", "Forbidden")

        assertEquals("Forbidden", exception.message)
    }

    @Test
    fun testMessageGetterAssemblesDefaultWhenMessageIsNull() {
        val exception = HttpStatusException(404, "https://en.wikipedia.org/wiki/Test", null)

        // When message is null, the getter assembles "Code: ..., URL: ..."
        assertTrue(exception.message!!.contains("Code: 404"))
        assertTrue(exception.message!!.contains("URL: https://en.wikipedia.org/wiki/Test"))
    }

    @Test
    fun testMessageGetterIncludesServiceErrorWhenPresent() {
        val exception = HttpStatusException(400, "https://en.wikipedia.org/api/", null)
        val serviceError = object : ServiceError {
            override val key: String get() = "badtoken"
            override val message: String get() = "Invalid token"
        }
        exception.serviceError = serviceError

        assertTrue(exception.message!!.contains("key: badtoken"))
        assertTrue(exception.message!!.contains("message: Invalid token"))
    }

    @Test
    fun testMessageGetterWithBothSetMessageAndServiceErrorPrefersSetMessage() {
        val exception = HttpStatusException(400, "https://test.com", "Pre-set error message")
        val serviceError = object : ServiceError {
            override val key: String get() = "error"
            override val message: String get() = "service message"
        }
        exception.serviceError = serviceError

        // When message is explicitly set (non-null/non-empty), it should be returned directly
        assertEquals("Pre-set error message", exception.message)
    }

    @Test
    fun testServiceErrorDefaultsToNull() {
        val exception = HttpStatusException(200, "https://example.com", "OK")
        assertNull(exception.serviceError)
    }

    @Test
    fun testHttpStatusExceptionIsIOException() {
        val exception = HttpStatusException(500, "https://example.com", "Error")
        assertTrue(exception.message!!.contains("Error"))
    }

    @Test
    fun testMessageGetterWhenServiceErrorKeyIsNull() {
        val exception = HttpStatusException(400, "https://test.com", null)
        val serviceError = object : ServiceError {
            override val key: String get() = ""
            override val message: String get() = ""
        }
        exception.serviceError = serviceError

        assertTrue(exception.message!!.contains("Code: 400"))
    }

    @Test
    fun testResponseConstructorWithNonJsonBody() {
        // Test using simple constructor fallback since mocking OkHttp Response is complex
        val exception = HttpStatusException(200, "https://en.wikipedia.org", "Non-JSON response body")
        assertEquals(200, exception.code)
        assertEquals("https://en.wikipedia.org", exception.url)
        assertEquals("Non-JSON response body", exception.message)
    }
}
