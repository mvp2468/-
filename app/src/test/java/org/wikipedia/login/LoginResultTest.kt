package org.wikipedia.login

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite

@RunWith(RobolectricTestRunner::class)
class LoginResultTest {

    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testPassReturnsTrueForPassStatus() {
        val result = LoginResult(wikiSite, LoginResult.STATUS_PASS, "user", "pass", null)
        assertTrue(result.pass())
    }

    @Test
    fun testPassReturnsFalseForFailStatus() {
        val result = LoginResult(wikiSite, LoginResult.STATUS_FAIL, "user", "pass", "error")
        assertFalse(result.pass())
    }

    @Test
    fun testPassReturnsFalseForUIStatus() {
        val result = LoginResult(wikiSite, LoginResult.STATUS_UI, "user", "pass", "prompt")
        assertFalse(result.pass())
    }

    @Test
    fun testFailReturnsTrueForFailStatus() {
        val result = LoginResult(wikiSite, LoginResult.STATUS_FAIL, "user", "pass", "error")
        assertTrue(result.fail())
    }

    @Test
    fun testFailReturnsFalseForPassStatus() {
        val result = LoginResult(wikiSite, LoginResult.STATUS_PASS, "user", "pass", null)
        assertFalse(result.fail())
    }

    @Test
    fun testFailReturnsFalseForUIStatus() {
        val result = LoginResult(wikiSite, LoginResult.STATUS_UI, "user", "pass", "prompt")
        assertFalse(result.fail())
    }

    @Test
    fun testStatusConstants() {
        assertEquals("PASS", LoginResult.STATUS_PASS)
        assertEquals("FAIL", LoginResult.STATUS_FAIL)
        assertEquals("UI", LoginResult.STATUS_UI)
    }

    @Test
    fun testResultProperties() {
        val result = LoginResult(wikiSite, "PASS", "TestUser", "TestPass", "msg")
        result.userId = 42
        result.groups = setOf("sysop", "bureaucrat")
        result.messageCode = "error_code"

        assertEquals(wikiSite, result.site)
        assertEquals("PASS", result.status)
        assertEquals("TestUser", result.userName)
        assertEquals("TestPass", result.password)
        assertEquals("msg", result.message)
        assertEquals(42, result.userId)
        assertEquals(setOf("sysop", "bureaucrat"), result.groups)
        assertEquals("error_code", result.messageCode)
    }

    @Test
    fun testResultDefaultProperties() {
        val result = LoginResult(wikiSite, "PASS", "user", "pass", null)
        assertEquals(0, result.userId)
        assertTrue(result.groups.isEmpty())
        assertNull(result.messageCode)
    }

    @Test
    fun testOATHResultIsLoginResult() {
        val result = LoginOATHResult(wikiSite, LoginResult.STATUS_UI, "user", "pass", "2FA required")
        assertTrue(result is LoginResult)
        assertEquals(LoginResult.STATUS_UI, result.status)
        assertEquals("2FA required", result.message)
    }

    @Test
    fun testEmailAuthResultIsLoginResult() {
        val result = LoginEmailAuthResult(wikiSite, LoginResult.STATUS_UI, "user", "pass", "Email auth required")
        assertTrue(result is LoginResult)
        assertEquals(LoginResult.STATUS_UI, result.status)
    }

    @Test
    fun testResetPasswordResultIsLoginResult() {
        val result = LoginResetPasswordResult(wikiSite, LoginResult.STATUS_UI, "user", "pass", "Reset your password")
        assertTrue(result is LoginResult)
    }

    @Test
    fun testModuleSelectResultIsLoginResult() {
        val result = LoginModuleSelectResult(wikiSite, LoginResult.STATUS_UI, "user", "pass", "Select module")
        assertTrue(result is LoginResult)
    }
}
