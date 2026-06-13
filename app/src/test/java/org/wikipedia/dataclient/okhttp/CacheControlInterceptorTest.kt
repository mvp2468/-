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

class CacheControlInterceptorTest {
    private val interceptor = CacheControlInterceptor()

    @Test
    fun testStripsMustRevalidateHeader() {
        val request = Request.Builder().url("https://en.wikipedia.org/api/rest_v1/page/summary/Test").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .header("Cache-Control", "must-revalidate, max-age=3600")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)
        assertFalse(result.cacheControl.mustRevalidate)
        assertNotNull(result.header("Cache-Control"))
    }

    @Test
    fun testPreservesPrivateCacheControl() {
        val request = Request.Builder().url("https://en.wikipedia.org/api/rest_v1/page/summary/Test").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .header("Cache-Control", "private, max-age=0")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)
        assertTrue(result.cacheControl.isPrivate)
    }

    @Test
    fun testRemovesVaryHeaderWhenSavingForOffline() {
        val request = Request.Builder()
            .url("https://en.wikipedia.org/api/rest_v1/page/summary/Test")
            .header(OfflineCacheInterceptor.SAVE_HEADER, OfflineCacheInterceptor.SAVE_HEADER_SAVE)
            .build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .header("Cache-Control", "max-age=3600")
            .header("Vary", "Accept-Language")
            .header("set-cookie", "session=abc123")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)
        assertNull(result.header("Vary"))
        assertNull(result.header("set-cookie"))
    }

    @Test
    fun testDoesNotRemoveHeadersWhenNotSaving() {
        val request = Request.Builder()
            .url("https://en.wikipedia.org/api/rest_v1/page/summary/Test")
            .build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .header("Cache-Control", "max-age=3600")
            .header("Vary", "Accept-Language")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)
        assertEquals("Accept-Language", result.header("Vary"))
    }
}
