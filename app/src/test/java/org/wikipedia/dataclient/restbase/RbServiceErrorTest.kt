package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class RbServiceErrorTest {

    @Test
    fun testCreateFromMediaWikiError() {
        val json = """{"errorKey":"apierror-blocked","messageTranslations":{"en":"You have been blocked from editing."}}"""
        val error = RbServiceError.create(json)
        assertNotNull(error)
        assertEquals("apierror-blocked", error.key)
        assertEquals("You have been blocked from editing.", error.message)
    }

    @Test
    fun testCreateFromRestBaseError() {
        val json = """{"title":"not_found","detail":"The requested resource could not be found."}"""
        val error = RbServiceError.create(json)
        assertNotNull(error)
        assertEquals("not_found", error.key)
        assertEquals("The requested resource could not be found.", error.message)
    }

    @Test
    fun testKeyFallsBackToTitle() {
        val json = """{"title":"restbase_error","detail":"Something went wrong."}"""
        val error = RbServiceError.create(json)
        assertEquals("restbase_error", error.key)
    }

    @Test
    fun testEmptyMessageTranslationsReturnsEmpty() {
        val json = """{"errorKey":"test","messageTranslations":{}}"""
        val error = RbServiceError.create(json)
        assertEquals("", error.message)
    }

    @Test
    fun testNullDetailReturnsEmptyMessage() {
        val json = """{"errorKey":"test_error"}"""
        val error = RbServiceError.create(json)
        assertEquals("test_error", error.key)
        assertEquals("", error.message)
    }
}
