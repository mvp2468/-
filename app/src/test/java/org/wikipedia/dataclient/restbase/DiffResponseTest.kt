package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class DiffResponseTest {

    @Test
    fun testConstructorDefaults() {
        val diff = DiffResponse()
        assertTrue(diff.diff.isEmpty())
    }

    @Test
    fun testDiffItemDefaults() {
        val item = DiffResponse.DiffItem()
        assertEquals(0, item.type)
        assertEquals(-1, item.lineNumber)
        assertEquals("", item.text)
        assertTrue(item.highlightRanges.isEmpty())
    }

    @Test
    fun testHighlightRangeDefaults() {
        val range = DiffResponse.HighlightRange()
        assertEquals(0, range.start)
        assertEquals(0, range.length)
        assertEquals(0, range.type)
    }

    @Test
    fun testDiffTypeConstants() {
        assertEquals(0, DiffResponse.DIFF_TYPE_LINE_WITH_SAME_CONTENT)
        assertEquals(1, DiffResponse.DIFF_TYPE_LINE_ADDED)
        assertEquals(2, DiffResponse.DIFF_TYPE_LINE_REMOVED)
        assertEquals(3, DiffResponse.DIFF_TYPE_LINE_WITH_DIFF)
        assertEquals(4, DiffResponse.DIFF_TYPE_PARAGRAPH_MOVED_FROM)
        assertEquals(5, DiffResponse.DIFF_TYPE_PARAGRAPH_MOVED_TO)
        assertEquals(0, DiffResponse.HIGHLIGHT_TYPE_ADD)
        assertEquals(1, DiffResponse.HIGHLIGHT_TYPE_DELETE)
    }
}
