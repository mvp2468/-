package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class RevisionTest {

    @Test
    fun testDefaultValues() {
        val rev = Revision()
        assertEquals(0L, rev.id)
        assertEquals(0, rev.size)
        assertEquals("", rev.timestamp)
        assertNull(rev.delta)
        assertEquals("", rev.source)
    }
}
