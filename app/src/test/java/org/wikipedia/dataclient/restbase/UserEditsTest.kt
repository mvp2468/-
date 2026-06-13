package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class UserEditsTest {

    @Test
    fun testDefaultValues() {
        val edits = UserEdits()
        assertTrue(edits.items.isEmpty())
    }

    @Test
    fun testItemDefaults() {
        val item = UserEdits.Item()
        assertNull(item.timestamp)
        assertEquals(0, item.editCount)
    }
}
