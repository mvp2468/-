package org.wikipedia.edit

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EditTest {

    @Test
    fun testEditSucceededWithSuccessStatus() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Success"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertTrue(edit.edit!!.editSucceeded)
    }

    @Test
    fun testEditNotSucceededWithNonSuccessStatus() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Failure"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertFalse(edit.edit!!.editSucceeded)
    }

    @Test
    fun testEditNotSucceededWithNullStatus() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"newrevid":123}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertFalse(edit.edit!!.editSucceeded)
    }

    @Test
    fun testCaptchaIdEmptyWhenNoCaptcha() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Success","newrevid":123}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertEquals("", edit.edit!!.captchaId)
    }

    @Test
    fun testCaptchaIdReturnsIdWhenCaptchaPresent() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Failure","captcha":{"id":"12345"}}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertEquals("12345", edit.edit!!.captchaId)
    }

    @Test
    fun testHasEditErrorCodeWhenCodePresent() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Failure","code":"error_code"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertTrue(edit.edit!!.hasEditErrorCode)
    }

    @Test
    fun testHasEditErrorCodeFalseWhenCodeMissing() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Success"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertFalse(edit.edit!!.hasEditErrorCode)
    }

    @Test
    fun testHasCaptchaResponseWhenCaptchaPresent() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Failure","captcha":{"id":"abc"}}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertTrue(edit.edit!!.hasCaptchaResponse)
    }

    @Test
    fun testHasCaptchaResponseFalseWhenNoCaptcha() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Success"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertFalse(edit.edit!!.hasCaptchaResponse)
    }

    @Test
    fun testHasSpamBlacklistResponseWhenPresent() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Failure","spamblacklist":"blocked.com"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertTrue(edit.edit!!.hasSpamBlacklistResponse)
    }

    @Test
    fun testHasSpamBlacklistResponseFalseWhenMissing() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Success"}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertFalse(edit.edit!!.hasSpamBlacklistResponse)
    }

    @Test
    fun testNewRevId() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"edit":{"result":"Success","newrevid":4242}}"""
        val edit = json.decodeFromString<Edit>(response)
        assertEquals(4242L, edit.edit!!.newRevId)
    }
}
