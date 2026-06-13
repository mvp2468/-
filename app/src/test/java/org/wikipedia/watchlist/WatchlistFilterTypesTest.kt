package org.wikipedia.watchlist

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WatchlistFilterTypesTest {

    @Test
    fun testFindReturnsExactMatch() {
        assertEquals(WatchlistFilterTypes.PAGE_EDITS, WatchlistFilterTypes.find("pageEdits"))
        assertEquals(WatchlistFilterTypes.MINOR_EDITS, WatchlistFilterTypes.find("minorEdits"))
        assertEquals(WatchlistFilterTypes.BOT, WatchlistFilterTypes.find("bot"))
    }

    @Test
    fun testFindReturnsPrefixMatch() {
        // "pageEdits-foo" starts with "pageEdits" so returns PAGE_EDITS
        assertEquals(WatchlistFilterTypes.PAGE_EDITS, WatchlistFilterTypes.find("pageEdits-extra"))
    }

    @Test
    fun testFindReturnsDefaultForUnknownId() {
        assertEquals(WatchlistFilterTypes.PAGE_EDITS, WatchlistFilterTypes.find("nonexistent"))
        assertEquals(WatchlistFilterTypes.PAGE_EDITS, WatchlistFilterTypes.find(""))
    }

    @Test
    fun testFindGroupMinorEdits() {
        val group = WatchlistFilterTypes.findGroup("minorEdits")
        assertEquals(WatchlistFilterTypes.MINOR_EDITS_GROUP, group)
        assertEquals(3, group.size)
        assertTrue(group.contains(WatchlistFilterTypes.ALL_EDITS))
        assertTrue(group.contains(WatchlistFilterTypes.MINOR_EDITS))
        assertTrue(group.contains(WatchlistFilterTypes.NON_MINOR_EDITS))
    }

    @Test
    fun testFindGroupBotEdits() {
        val group = WatchlistFilterTypes.findGroup("bot")
        assertEquals(WatchlistFilterTypes.BOT_EDITS_GROUP, group)
        assertEquals(3, group.size)
        assertTrue(group.contains(WatchlistFilterTypes.ALL_EDITORS))
        assertTrue(group.contains(WatchlistFilterTypes.BOT))
        assertTrue(group.contains(WatchlistFilterTypes.HUMAN))
    }

    @Test
    fun testFindGroupUnseenChanges() {
        val group = WatchlistFilterTypes.findGroup("unseenChanges")
        assertEquals(WatchlistFilterTypes.UNSEEN_CHANGES_GROUP, group)
        assertTrue(group.contains(WatchlistFilterTypes.UNSEEN_CHANGES))
        assertTrue(group.contains(WatchlistFilterTypes.SEEN_CHANGES))
    }

    @Test
    fun testFindGroupLatestRevisions() {
        val group = WatchlistFilterTypes.findGroup("latestRevision")
        assertEquals(WatchlistFilterTypes.LATEST_REVISIONS_GROUP, group)
        assertTrue(group.contains(WatchlistFilterTypes.NOT_LATEST_REVISION))
    }

    @Test
    fun testFindGroupUserStatus() {
        val group = WatchlistFilterTypes.findGroup("unregistered")
        assertEquals(WatchlistFilterTypes.USER_STATUS_GROUP, group)
        assertTrue(group.contains(WatchlistFilterTypes.ALL_USERS))
        assertTrue(group.contains(WatchlistFilterTypes.UNREGISTERED))
        assertTrue(group.contains(WatchlistFilterTypes.REGISTERED))
    }

    @Test
    fun testFindGroupReturnsEmptyForUnknownType() {
        val group = WatchlistFilterTypes.findGroup("nonexistent")
        assertTrue(group.isEmpty())
    }

    @Test
    fun testCodeReturnsOrdinal() {
        assertEquals(0, WatchlistFilterTypes.PAGE_EDITS.code())
        assertEquals(1, WatchlistFilterTypes.PAGE_CREATIONS.code())
        assertEquals(2, WatchlistFilterTypes.CATEGORY_CHANGES.code())
    }

    @Test
    fun testAllEntriesHaveNonEmptyId() {
        WatchlistFilterTypes.entries.forEach {
            assertTrue(it.id.isNotEmpty())
        }
    }

    @Test
    fun testDefaultFilterSetContainsExpectedEntries() {
        val defaults = WatchlistFilterTypes.DEFAULT_FILTER_TYPE_SET
        assertTrue(defaults.contains(WatchlistFilterTypes.ALL_EDITS))
        assertTrue(defaults.contains(WatchlistFilterTypes.ALL_CHANGES))
        assertTrue(defaults.contains(WatchlistFilterTypes.LATEST_REVISION))
        assertTrue(defaults.contains(WatchlistFilterTypes.ALL_EDITORS))
        assertTrue(defaults.contains(WatchlistFilterTypes.ALL_USERS))
    }

    @Test
    fun testTypeOfChangesGroupValues() {
        assertEquals("edit", WatchlistFilterTypes.PAGE_EDITS.value)
        assertEquals("new", WatchlistFilterTypes.PAGE_CREATIONS.value)
        assertEquals("categorize", WatchlistFilterTypes.CATEGORY_CHANGES.value)
        assertEquals("external", WatchlistFilterTypes.WIKIDATA_EDITS.value)
        assertEquals("log", WatchlistFilterTypes.LOGGED_ACTIONS.value)
    }
}
