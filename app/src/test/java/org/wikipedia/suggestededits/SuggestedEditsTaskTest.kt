package org.wikipedia.suggestededits

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SuggestedEditsTaskTest {

    @Test
    fun testDefaultProperties() {
        val task = SuggestedEditsTask()
        assertNull(task.title)
        assertNull(task.description)
        assertNull(task.primaryAction)
        assertNull(task.secondaryAction)
        assertFalse(task.disabled)
        assertFalse(task.new)
        assertEquals(0, task.primaryActionIcon)
        assertEquals(0, task.imageDrawable)
    }

    @Test
    fun testSetTitle() {
        val task = SuggestedEditsTask()
        task.title = "My Task"
        assertEquals("My Task", task.title)
    }

    @Test
    fun testSetDescription() {
        val task = SuggestedEditsTask()
        task.description = "A task description"
        assertEquals("A task description", task.description)
    }

    @Test
    fun testSetPrimaryAction() {
        val task = SuggestedEditsTask()
        task.primaryAction = "Edit"
        assertEquals("Edit", task.primaryAction)
    }

    @Test
    fun testSetSecondaryAction() {
        val task = SuggestedEditsTask()
        task.secondaryAction = "View"
        assertEquals("View", task.secondaryAction)
    }

    @Test
    fun testDisabledFlag() {
        val task = SuggestedEditsTask()
        task.disabled = true
        assertTrue(task.disabled)
    }

    @Test
    fun testNewFlag() {
        val task = SuggestedEditsTask()
        task.new = true
        assertTrue(task.new)
    }

    @Test
    fun testImageDrawable() {
        val task = SuggestedEditsTask()
        task.imageDrawable = 42
        assertEquals(42, task.imageDrawable)
    }
}
