package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResourceTest {

    @Test
    fun testLoadingIsResource() {
        val loading = Resource.Loading<String>()
        assertTrue(loading is Resource<String>)
    }

    @Test
    fun testSuccessHoldsData() {
        val success = Resource.Success("hello")
        assertTrue(success is Resource<String>)
        assertEquals("hello", success.data)
    }

    @Test
    fun testSuccessWithNullData() {
        val success = Resource.Success(null as String?)
        assertNull(success.data)
    }

    @Test
    fun testErrorHoldsThrowable() {
        val exception = RuntimeException("test error")
        val error = Resource.Error<String>(exception)
        assertTrue(error is Resource<String>)
        assertEquals("test error", error.throwable.message)
    }

    @Test
    fun testUiStateLoadingIsSingleton() {
        val loading1: UiState<Int> = UiState.Loading
        val loading2: UiState<String> = UiState.Loading
        assertSame(loading1, loading2)
    }

    @Test
    fun testUiStateSuccessHoldsData() {
        val success: UiState<Int> = UiState.Success(42)
        assertTrue(success is UiState<Int>)
        assertEquals(42, (success as UiState.Success).data)
    }

    @Test
    fun testUiStateErrorHoldsThrowable() {
        val exception = RuntimeException("ui error")
        val error: UiState<Int> = UiState.Error(exception)
        assertTrue(error is UiState.Error)
        assertEquals("ui error", (error as UiState.Error).error.message)
    }

    @Test
    fun testResourceSuccessWithInt() {
        val success = Resource.Success(100)
        assertEquals(100, success.data)
    }
}
