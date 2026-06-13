package org.wikipedia.dataclient.okhttp

import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Test

class TitleEncodeInterceptorTest {
    private val interceptor = TitleEncodeInterceptor()

    @Test
    fun testDoesNotModifyUrlWithFewSegments() {
        val originalUrl = "https://en.wikipedia.org/api/rest_v1/"
        val request = Request.Builder().url(originalUrl).build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        var forwardedRequest: Request? = null
        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(any()) } answers {
            forwardedRequest = firstArg()
            response
        }

        interceptor.intercept(chain)
        assertEquals(originalUrl, forwardedRequest?.url.toString())
    }

    @Test
    fun testEncodesPathSegmentsWithSpecialChars() {
        val originalUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/Test Page"
        val request = Request.Builder().url(originalUrl).build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        var forwardedRequest: Request? = null
        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(any()) } answers {
            forwardedRequest = firstArg()
            response
        }

        interceptor.intercept(chain)
        val forwardedUrl = forwardedRequest?.url.toString()
        assertTrue(forwardedUrl.contains("Test%20Page"))
    }

    @Test
    fun testPreservesEncodedPathSegments() {
        val originalUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/Already%20Encoded"
        val request = Request.Builder().url(originalUrl).build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        var forwardedRequest: Request? = null
        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(any()) } answers {
            forwardedRequest = firstArg()
            response
        }

        interceptor.intercept(chain)
        val forwardedUrl = forwardedRequest?.url.toString()
        assertTrue(forwardedUrl.contains("Already%20Encoded"))
    }
}
