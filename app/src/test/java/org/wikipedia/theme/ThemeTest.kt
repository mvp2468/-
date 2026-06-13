package org.wikipedia.theme

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThemeTest {

    @Test
    fun testOfMarshallingIdValid() {
        assertEquals(Theme.LIGHT, Theme.ofMarshallingId(0))
        assertEquals(Theme.DARK, Theme.ofMarshallingId(1))
        assertEquals(Theme.BLACK, Theme.ofMarshallingId(2))
        assertEquals(Theme.SEPIA, Theme.ofMarshallingId(3))
    }

    @Test
    fun testOfMarshallingIdInvalid() {
        assertNull(Theme.ofMarshallingId(-1))
        assertNull(Theme.ofMarshallingId(99))
        assertNull(Theme.ofMarshallingId(4))
    }

    @Test
    fun testCode() {
        assertEquals(0, Theme.LIGHT.code())
        assertEquals(1, Theme.DARK.code())
        assertEquals(2, Theme.BLACK.code())
        assertEquals(3, Theme.SEPIA.code())
    }

    @Test
    fun testIsDark() {
        assertFalse(Theme.LIGHT.isDark)
        assertTrue(Theme.DARK.isDark)
        assertTrue(Theme.BLACK.isDark)
        assertFalse(Theme.SEPIA.isDark)
    }

    @Test
    fun testIsDefault() {
        assertTrue(Theme.LIGHT.isDefault)
        assertFalse(Theme.DARK.isDefault)
    }

    @Test
    fun testFallback() {
        assertEquals(Theme.LIGHT, Theme.fallback)
    }

    @Test
    fun testTags() {
        assertEquals("light", Theme.LIGHT.tag)
        assertEquals("dark", Theme.DARK.tag)
        assertEquals("black", Theme.BLACK.tag)
        assertEquals("sepia", Theme.SEPIA.tag)
    }

    @Test
    fun testFourEntries() {
        assertEquals(4, Theme.entries.size)
    }

    @Test
    fun testValueOf() {
        assertEquals(Theme.LIGHT, Theme.valueOf("LIGHT"))
        assertEquals(Theme.DARK, Theme.valueOf("DARK"))
        assertEquals(Theme.BLACK, Theme.valueOf("BLACK"))
        assertEquals(Theme.SEPIA, Theme.valueOf("SEPIA"))
    }
}