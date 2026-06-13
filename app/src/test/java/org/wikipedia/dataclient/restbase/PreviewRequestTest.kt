package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class PreviewRequestTest {

    @Test
    fun testConstructor() {
        val request = PreviewRequest(wikitext = "== Heading ==\nSome text.")
        assertEquals("== Heading ==\nSome text.", request.wikitext)
    }

    @Test
    fun testEmptyWikitext() {
        val request = PreviewRequest(wikitext = "")
        assertEquals("", request.wikitext)
    }
}
