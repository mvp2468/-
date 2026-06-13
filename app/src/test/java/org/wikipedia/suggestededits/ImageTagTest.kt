package org.wikipedia.suggestededits

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageTagTest {

    @Test
    fun testDefaultIsSelectedIsFalse() {
        val tag = ImageTag("Q123", "Nature")
        assertFalse(tag.isSelected)
    }

    @Test
    fun testSetIsSelected() {
        val tag = ImageTag("Q123", "Nature")
        tag.isSelected = true
        assertTrue(tag.isSelected)
    }

    @Test
    fun testLabel() {
        val tag = ImageTag("Q123", "Nature")
        assertEquals("Nature", tag.label)
    }

    @Test
    fun testWikidataId() {
        val tag = ImageTag("Q123", "Nature")
        assertEquals("Q123", tag.wikidataId)
    }

    @Test
    fun testDescription() {
        val tag = ImageTag("Q123", "Nature", "A natural landscape")
        assertEquals("A natural landscape", tag.description)
    }

    @Test
    fun testDescriptionDefaultNull() {
        val tag = ImageTag("Q123", "Nature")
        assertNull(tag.description)
    }

    @Test
    fun testMultipleTags() {
        val tag1 = ImageTag("Q1", "Building")
        val tag2 = ImageTag("Q2", "Person")
        assertFalse(tag1.isSelected)
        assertFalse(tag2.isSelected)
        tag1.isSelected = true
        assertTrue(tag1.isSelected)
        assertFalse(tag2.isSelected)
    }
}
