package org.wikipedia.dataclient.okhttp

import okhttp3.Request
import org.junit.Assert.*
import org.junit.Test
import org.wikipedia.dataclient.Service

class OfflineCacheInterceptorCompanionTest {

    @Test
    fun testShouldSaveTrueForGetWithSaveHeader() {
        val request = Request.Builder()
            .url(Service.WIKIPEDIA_URL)
            .addHeader(OfflineCacheInterceptor.SAVE_HEADER, OfflineCacheInterceptor.SAVE_HEADER_SAVE)
            .build()
        assertTrue(OfflineCacheInterceptor.shouldSave(request))
    }

    @Test
    fun testShouldSaveFalseForGetWithoutSaveHeader() {
        val request = Request.Builder()
            .url(Service.WIKIPEDIA_URL)
            .build()
        assertFalse(OfflineCacheInterceptor.shouldSave(request))
    }

    @Test
    fun testShouldSaveFalseForPostWithSaveHeader() {
        val request = Request.Builder()
            .url(Service.WIKIPEDIA_URL)
            .addHeader(OfflineCacheInterceptor.SAVE_HEADER, OfflineCacheInterceptor.SAVE_HEADER_SAVE)
            .post(okhttp3.RequestBody.create(null, ""))
            .build()
        assertFalse(OfflineCacheInterceptor.shouldSave(request))
    }

    @Test
    fun testShouldSaveFalseForWrongSaveHeaderValue() {
        val request = Request.Builder()
            .url(Service.WIKIPEDIA_URL)
            .addHeader(OfflineCacheInterceptor.SAVE_HEADER, "wrong_value")
            .build()
        assertFalse(OfflineCacheInterceptor.shouldSave(request))
    }

    @Test
    fun testCompanionConstants() {
        assertEquals("X-Offline-Lang", OfflineCacheInterceptor.LANG_HEADER)
        assertEquals("X-Offline-Title", OfflineCacheInterceptor.TITLE_HEADER)
        assertEquals("X-Offline-Save", OfflineCacheInterceptor.SAVE_HEADER)
        assertEquals("save", OfflineCacheInterceptor.SAVE_HEADER_SAVE)
        assertEquals("offline_files", OfflineCacheInterceptor.OFFLINE_PATH)
    }
}
