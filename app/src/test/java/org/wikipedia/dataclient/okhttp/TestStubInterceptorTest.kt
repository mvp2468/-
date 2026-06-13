package org.wikipedia.dataclient.okhttp

import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.After
import org.junit.Test

class TestStubInterceptorTest {
    private val interceptor = TestStubInterceptor()

    @After
    fun tearDown() {
        TestStubInterceptor.CALLBACK = null
    }

    @Test
    fun testPassthroughWhenNoCallback() {
        val request = Request.Builder().url("https://example.com/").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), "ok"))
            .build()

        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)
        assertEquals(200, result.code)
    }

    @Test
    fun testUsesCallbackWhenSet() {
        val request = Request.Builder().url("https://example.com/").build()
        val stubResponse = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(418)
            .message("I'm a teapot")
            .body(ResponseBody.create("text/plain".toMediaType(), "stub"))
            .build()

        TestStubInterceptor.CALLBACK = object : TestStubInterceptor.Callback {
            override fun getResponse(request: okhttp3.Interceptor.Chain): Response = stubResponse
        }

        val chain = mockk<okhttp3.Interceptor.Chain>()
        val result = interceptor.intercept(chain)
        assertEquals(418, result.code)
        assertEquals("I'm a teapot", result.message)
    }

    @Test
    fun testNullCallbackReturnsPassthrough() {
        TestStubInterceptor.CALLBACK = null
        val request = Request.Builder().url("https://example.com/").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), ""))
            .build()

        val chain = mockk<okhttp3.Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)
        assertEquals(200, result.code)
    }
}
