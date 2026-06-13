package org.wikipedia.login

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite

@RunWith(RobolectricTestRunner::class)
class LoginResponseTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val wikiSite = WikiSite.forLanguageCode("en")

    @Test
    fun testToLoginResultPass() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "PASS",
                "username": "TestUser"
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password123")
        assertNotNull(result)
        assertEquals("PASS", result!!.status)
        assertTrue(result.pass())
        assertEquals("TestUser", result.userName)
    }

    @Test
    fun testToLoginResultFail() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "FAIL",
                "message": "Incorrect password.",
                "messagecode": "wrongpassword"
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "wrongpass")
        assertNotNull(result)
        assertEquals("FAIL", result!!.status)
        assertTrue(result.fail())
        assertEquals("Incorrect password.", result.message)
    }

    @Test
    fun testToLoginResultUIWithOATHRequest() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "Please enter your 2FA code.",
                "requests": [
                    {"id": "MediaWiki\\Extension\\OATHAuth\\Auth\\TOTPAuthenticationRequest"}
                ]
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        assertTrue(result is LoginOATHResult)
        assertEquals("UI", result!!.status)
    }

    @Test
    fun testToLoginResultUIWithEmailAuthRequest() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "Check your email for a code.",
                "requests": [
                    {"id": "MediaWiki\\Auth\\EmailAuthAuthenticationRequest"}
                ]
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        assertTrue(result is LoginEmailAuthResult)
    }

    @Test
    fun testToLoginResultUIWithPasswordResetRequest() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "Please reset your password.",
                "requests": [
                    {"id": "MediaWiki\\Auth\\PasswordAuthenticationRequest"}
                ]
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        assertTrue(result is LoginResetPasswordResult)
    }

    @Test
    fun testToLoginResultUIWithModuleSelectRequest() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "Select a module.",
                "requests": [
                    {"id": "MediaWiki\\Auth\\TwoFactorModuleSelectAuthenticationRequest"}
                ]
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        assertTrue(result is LoginModuleSelectResult)
    }

    @Test
    fun testToLoginResultUIWithNoMatchingRequest() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "Unknown request type.",
                "requests": [
                    {"id": "SomeUnknownRequest"}
                ]
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        // Should fall through to generic LoginResult
        assertFalse(result is LoginOATHResult)
        assertFalse(result is LoginEmailAuthResult)
        assertFalse(result is LoginResetPasswordResult)
        assertFalse(result is LoginModuleSelectResult)
    }

    @Test
    fun testToLoginResultUIWithNullRequests() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "No requests available."
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        // When requests is null, should fall through to generic LoginResult
        assertEquals("UI", result!!.status)
        assertEquals("No requests available.", result.message)
    }

    @Test
    fun testToLoginResultUnknownStatus() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UNKNOWN",
                "message": "Something happened.",
                "messagecode": "unknown"
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        assertEquals("UNKNOWN", result!!.status)
        assertEquals("An unknown error occurred.", result.message)
    }

    @Test
    fun testToLoginResultNullClientLogin() {
        val jsonString = """
        {}
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNull(result)
    }

    @Test
    fun testToLoginResultUIWithMultipleRequestsFirstMatchWins() {
        val jsonString = """
        {
            "clientlogin": {
                "status": "UI",
                "message": "Multiple requests.",
                "requests": [
                    {"id": "MediaWiki\\Extension\\OATHAuth\\Auth\\TOTPAuthenticationRequest"},
                    {"id": "MediaWiki\\Auth\\EmailAuthAuthenticationRequest"}
                ]
            }
        }
        """.trimIndent()
        val response = json.decodeFromString<LoginResponse>(jsonString)
        val result = response.toLoginResult(wikiSite, "password")
        assertNotNull(result)
        // TOTP should match first
        assertTrue(result is LoginOATHResult)
    }
}
