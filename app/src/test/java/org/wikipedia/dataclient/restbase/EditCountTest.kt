package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class EditCountTest {

    @Test
    fun testDefaultValues() {
        val count = EditCount()
        assertEquals(0, count.count)
        assertFalse(count.limit)
    }

    @Test
    fun testEditTypeConstants() {
        assertEquals("anonymous", EditCount.EDIT_TYPE_ANONYMOUS)
        assertEquals("temporary", EditCount.EDIT_TYPE_TEMPORARY)
        assertEquals("bot", EditCount.EDIT_TYPE_BOT)
        assertEquals("editors", EditCount.EDIT_TYPE_EDITORS)
        assertEquals("edits", EditCount.EDIT_TYPE_EDITS)
        assertEquals("minor", EditCount.EDIT_TYPE_MINOR)
        assertEquals("reverted", EditCount.EDIT_TYPE_REVERTED)
        assertEquals("all", EditCount.EDIT_TYPE_ALL)
    }
}
