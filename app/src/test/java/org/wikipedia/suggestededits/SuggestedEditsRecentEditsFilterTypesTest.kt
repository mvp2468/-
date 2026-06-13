package org.wikipedia.suggestededits

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SuggestedEditsRecentEditsFilterTypesTest {

    @Test
    fun testFindReturnsExactMatch() {
        assertEquals(SuggestedEditsRecentEditsFilterTypes.ALL_EDITS, SuggestedEditsRecentEditsFilterTypes.find("allEdits"))
        assertEquals(SuggestedEditsRecentEditsFilterTypes.MINOR_EDITS, SuggestedEditsRecentEditsFilterTypes.find("minorEdits"))
        assertEquals(SuggestedEditsRecentEditsFilterTypes.UNREGISTERED, SuggestedEditsRecentEditsFilterTypes.find("unregistered"))
    }

    @Test
    fun testFindIsPrefixAware() {
        assertEquals(SuggestedEditsRecentEditsFilterTypes.NEWCOMERS, SuggestedEditsRecentEditsFilterTypes.find("newcomers_extra"))
    }

    @Test
    fun testFindReturnsDefaultForInvalidId() {
        assertEquals(SuggestedEditsRecentEditsFilterTypes.ALL_EDITS, SuggestedEditsRecentEditsFilterTypes.find("nonexistent"))
        assertEquals(SuggestedEditsRecentEditsFilterTypes.ALL_EDITS, SuggestedEditsRecentEditsFilterTypes.find(""))
    }

    @Test
    fun testFindGroupReturnsCorrectGroup() {
        val minorGroup = SuggestedEditsRecentEditsFilterTypes.findGroup("minorEdits")
        assertTrue(minorGroup.contains(SuggestedEditsRecentEditsFilterTypes.ALL_EDITS))
        assertTrue(minorGroup.contains(SuggestedEditsRecentEditsFilterTypes.MINOR_EDITS))
        assertTrue(minorGroup.contains(SuggestedEditsRecentEditsFilterTypes.NON_MINOR_EDITS))

        val botGroup = SuggestedEditsRecentEditsFilterTypes.findGroup("bot")
        assertTrue(botGroup.contains(SuggestedEditsRecentEditsFilterTypes.ALL_EDITORS))
        assertTrue(botGroup.contains(SuggestedEditsRecentEditsFilterTypes.BOT))
        assertTrue(botGroup.contains(SuggestedEditsRecentEditsFilterTypes.HUMAN))
    }

    @Test
    fun testFindGroupForUnmatchedIdReturnsEmpty() {
        val result = SuggestedEditsRecentEditsFilterTypes.findGroup("unregistered")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testCodeReturnsOrdinal() {
        assertEquals(0, SuggestedEditsRecentEditsFilterTypes.ALL_EDITS.code())
        assertEquals(1, SuggestedEditsRecentEditsFilterTypes.MINOR_EDITS.code())
    }

    @Test
    fun testDefaultFilterTypeSetIsNotEmpty() {
        assertFalse(SuggestedEditsRecentEditsFilterTypes.DEFAULT_FILTER_TYPE_SET.isEmpty())
    }

    @Test
    fun testGroupDefinitionsAreComplete() {
        assertEquals(3, SuggestedEditsRecentEditsFilterTypes.MINOR_EDITS_GROUP.size)
        assertEquals(3, SuggestedEditsRecentEditsFilterTypes.BOT_EDITS_GROUP.size)
        assertEquals(2, SuggestedEditsRecentEditsFilterTypes.LATEST_REVISIONS_GROUP.size)
        assertEquals(2, SuggestedEditsRecentEditsFilterTypes.USER_REGISTRATION_GROUP.size)
        assertEquals(3, SuggestedEditsRecentEditsFilterTypes.USER_EXPERIENCE_GROUP.size)
        assertEquals(4, SuggestedEditsRecentEditsFilterTypes.DAMAGING_GROUP.size)
        assertEquals(4, SuggestedEditsRecentEditsFilterTypes.GOODFAITH_GROUP.size)
    }
}
