package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class RbRelatedPagesTest {

    @Test
    fun testDefaultValues() {
        val pages = RbRelatedPages()
        assertNull(pages.pages)
    }

    @Test
    fun testGetPagesReturnsEmptyForNull() {
        val pages = RbRelatedPages()
        assertEquals(0, pages.getPages(10).size)
    }

    @Test
    fun testGetPagesRespectsLimit() {
        // Since pages is null by default, it returns empty
        val pages = RbRelatedPages()
        val result = pages.getPages(10)
        assertTrue(result.isEmpty())

        // getPages with limit 0 should also work
        val result0 = pages.getPages(0)
        assertTrue(result0.isEmpty())
    }
}
