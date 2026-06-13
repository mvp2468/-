package org.wikipedia.notifications

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationCategoryTest {

    @Test
    fun testFindReturnsExactMatch() {
        assertEquals(NotificationCategory.SYSTEM, NotificationCategory.find("system"))
        assertEquals(NotificationCategory.EDIT_USER_TALK, NotificationCategory.find("edit-user-talk"))
        assertEquals(NotificationCategory.MENTION, NotificationCategory.find("mention"))
    }

    @Test
    fun testFindReturnsPrefixMatch() {
        assertEquals(NotificationCategory.MENTION, NotificationCategory.find("mention-failure"))
        assertEquals(NotificationCategory.MENTION, NotificationCategory.find("mention-success"))
    }

    @Test
    fun testFindReturnsDefaultForUnknownId() {
        assertEquals(NotificationCategory.SYSTEM, NotificationCategory.find("nonexistent-category"))
        assertEquals(NotificationCategory.SYSTEM, NotificationCategory.find(""))
    }

    @Test
    fun testIsMentionsGroupForMentionCategories() {
        assertTrue(NotificationCategory.isMentionsGroup("mention"))
        assertTrue(NotificationCategory.isMentionsGroup("mention-failure"))
        assertTrue(NotificationCategory.isMentionsGroup("edit-user-talk"))
        assertTrue(NotificationCategory.isMentionsGroup("emailuser"))
        assertTrue(NotificationCategory.isMentionsGroup("user-rights"))
        assertTrue(NotificationCategory.isMentionsGroup("reverted"))
    }

    @Test
    fun testIsMentionsGroupReturnsFalseForNonMentionCategories() {
        assertFalse(NotificationCategory.isMentionsGroup("system"))
        assertFalse(NotificationCategory.isMentionsGroup("edit-thank"))
        assertFalse(NotificationCategory.isMentionsGroup("login-fail"))
        assertFalse(NotificationCategory.isMentionsGroup("article-linked"))
    }

    @Test
    fun testIsFiltersGroupForFilterCategories() {
        assertTrue(NotificationCategory.isFiltersGroup("edit-user-talk"))
        assertTrue(NotificationCategory.isFiltersGroup("mention"))
        assertTrue(NotificationCategory.isFiltersGroup("mention-failure"))
        assertTrue(NotificationCategory.isFiltersGroup("emailuser"))
        assertTrue(NotificationCategory.isFiltersGroup("reverted"))
        assertTrue(NotificationCategory.isFiltersGroup("user-rights"))
        assertTrue(NotificationCategory.isFiltersGroup("edit-thank"))
        assertTrue(NotificationCategory.isFiltersGroup("thank-you-edit"))
        assertTrue(NotificationCategory.isFiltersGroup("login-fail"))
        assertTrue(NotificationCategory.isFiltersGroup("system"))
        assertTrue(NotificationCategory.isFiltersGroup("article-linked"))
    }

    @Test
    fun testIsFiltersGroupReturnsFalseForNonFilterCategories() {
        assertFalse(NotificationCategory.isFiltersGroup("alpha-builder-checker"))
        assertFalse(NotificationCategory.isFiltersGroup("reading-list-syncing"))
    }

    @Test
    fun testCodeReturnsOrdinal() {
        assertEquals(0, NotificationCategory.SYSTEM.code())
        assertEquals(1, NotificationCategory.MILESTONE_EDIT.code())
        assertEquals(2, NotificationCategory.EDIT_USER_TALK.code())
        assertEquals(3, NotificationCategory.EDIT_THANK.code())
    }

    @Test
    fun testAllCategoriesHaveValidIds() {
        NotificationCategory.entries.forEach {
            assertNotNull(it.id)
            assertTrue(it.id.isNotEmpty())
        }
    }
}
