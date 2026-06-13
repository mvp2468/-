package org.wikipedia.page

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SectionTest {

    @Test
    fun testIsLeadTrueWhenIdIsZero() {
        val section = Section(id = 0)
        assertTrue(section.isLead)
    }

    @Test
    fun testIsLeadFalseWhenIdIsNonZero() {
        val section = Section(id = 1)
        assertFalse(section.isLead)
    }

    @Test
    fun testIsLeadFalseWhenIdIsNegative() {
        val section = Section(id = -1)
        assertFalse(section.isLead)
    }

    @Test
    fun testDefaultValues() {
        val section = Section()
        assertEquals(0, section.id)
        assertEquals(1, section.level)
        assertEquals("", section.title)
        assertEquals("", section.anchor)
        assertEquals("", section.text)
        assertTrue(section.isLead)
    }

    @Test
    fun testCustomSection() {
        val section = Section(id = 2, level = 3, title = "History", anchor = "History", text = "Some text")
        assertEquals(2, section.id)
        assertEquals(3, section.level)
        assertEquals("History", section.title)
        assertEquals("History", section.anchor)
        assertEquals("Some text", section.text)
        assertFalse(section.isLead)
    }

    @Test
    fun testLeadSectionWithTitle() {
        val section = Section(id = 0, level = 1, title = "Introduction", anchor = "", text = "Intro text")
        assertTrue(section.isLead)
        assertEquals("Introduction", section.title)
        assertEquals("Intro text", section.text)
    }

    @Test
    fun testDataClassEquality() {
        val s1 = Section(id = 1, level = 2, title = "A", text = "content")
        val s2 = Section(id = 1, level = 2, title = "A", text = "content")
        assertEquals(s1, s2)
        assertEquals(s1.hashCode(), s2.hashCode())
    }

    @Test
    fun testSectionCopyWithNewValues() {
        val section = Section(id = 1, level = 2, title = "Original")
        val copy = section.copy(title = "Changed")
        assertEquals("Original", section.title)
        assertEquals("Changed", copy.title)
        assertEquals(section.id, copy.id)
        assertEquals(section.level, copy.level)
    }
}
