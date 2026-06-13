package org.wikipedia.page.tabs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.history.HistoryEntry
import org.wikipedia.page.PageBackStackItem
import org.wikipedia.page.PageTitle

@RunWith(RobolectricTestRunner::class)
class TabTest {

    private lateinit var tab: Tab
    private lateinit var enwiki: WikiSite

    @Before
    fun setUp() {
        tab = Tab()
        enwiki = WikiSite.forLanguageCode("en")
    }

    private fun makeItem(title: String): PageBackStackItem {
        val pageTitle = PageTitle(title, enwiki)
        val historyEntry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_INTERNAL_LINK)
        return PageBackStackItem(pageTitle, historyEntry)
    }

    // ---- Initial state ----

    @Test
    fun testInitialBackStackIsEmpty() {
        assertEquals(0, tab.backStack.size)
    }

    @Test
    fun testInitialBackStackPosition() {
        assertEquals(-1, tab.backStackPosition)
    }

    @Test
    fun testInitialBackStackPositionTitleIsNull() {
        assertNull(tab.backStackPositionTitle)
    }

    @Test
    fun testInitialCannotGoBack() {
        assertFalse(tab.canGoBack())
    }

    @Test
    fun testInitialCannotGoForward() {
        assertFalse(tab.canGoForward())
    }

    // ---- pushBackStackItem ----

    @Test
    fun testPushSingleItem() {
        tab.pushBackStackItem(makeItem("Page1"))
        assertEquals(1, tab.backStack.size)
        assertEquals(0, tab.backStackPosition)
    }

    @Test
    fun testPushSingleItemTitleMatches() {
        tab.pushBackStackItem(makeItem("Page1"))
        assertEquals("Page1", tab.backStackPositionTitle?.displayText)
    }

    @Test
    fun testPushMultipleItems() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        assertEquals(3, tab.backStack.size)
        assertEquals(2, tab.backStackPosition)
    }

    @Test
    fun testPushMultipleItemsTitleAtTop() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        assertEquals("Page3", tab.backStackPositionTitle?.displayText)
    }

    @Test
    fun testPushAfterGoBackClearsForwardItems() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        tab.moveBack()
        tab.moveBack()
        // Now at Page1, forward items are Page2 and Page3
        tab.pushBackStackItem(makeItem("Page4"))
        // Forward items should be cleared, new item pushed
        assertEquals(2, tab.backStack.size)
        assertEquals(1, tab.backStackPosition)
        assertEquals("Page4", tab.backStackPositionTitle?.displayText)
        assertEquals("Page1", tab.backStack[0].title.displayText)
    }

    @Test
    fun testPushAfterGoBackClearsForwardStackCorrectly() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        tab.moveBack() // back to position 1 (Page2)
        tab.pushBackStackItem(makeItem("Page3b"))
        assertEquals(3, tab.backStack.size)
        assertEquals("Page1", tab.backStack[0].title.displayText)
        assertEquals("Page2", tab.backStack[1].title.displayText)
        assertEquals("Page3b", tab.backStack[2].title.displayText)
    }

    // ---- canGoBack ----

    @Test
    fun testCanGoBackAfterSinglePush() {
        tab.pushBackStackItem(makeItem("Page1"))
        assertFalse(tab.canGoBack()) // at position 0, no previous items
    }

    @Test
    fun testCanGoBackAfterMultiplePushes() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        assertTrue(tab.canGoBack())
    }

    @Test
    fun testCannotGoBackAtStart() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.moveBack()
        tab.moveBack()
        assertFalse(tab.canGoBack())
    }

    // ---- canGoForward ----

    @Test
    fun testCannotGoForwardAtTop() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        assertFalse(tab.canGoForward())
    }

    @Test
    fun testCanGoForwardAfterGoingBack() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.moveBack()
        assertTrue(tab.canGoForward())
    }

    @Test
    fun testCannotGoForwardAfterLastItem() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.moveBack()
        tab.moveForward()
        assertFalse(tab.canGoForward())
    }

    // ---- moveBack ----

    @Test
    fun testMoveBackChangesPosition() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        assertEquals(2, tab.backStackPosition)
        tab.moveBack()
        assertEquals(1, tab.backStackPosition)
        assertEquals("Page2", tab.backStackPositionTitle?.displayText)
    }

    @Test
    fun testMoveBackDoesNotGoBelowZero() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.moveBack()
        assertEquals(0, tab.backStackPosition)
    }

    @Test
    fun testMoveBackOnEmptyStackDoesNothing() {
        tab.moveBack()
        assertEquals(-1, tab.backStackPosition)
    }

    // ---- moveForward ----

    @Test
    fun testMoveForwardChangesPosition() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        tab.moveBack()
        tab.moveBack()
        assertEquals(0, tab.backStackPosition)
        tab.moveForward()
        assertEquals(1, tab.backStackPosition)
    }

    @Test
    fun testMoveForwardDoesNotExceedSize() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.moveForward() // already at top
        assertEquals(1, tab.backStackPosition)
    }

    @Test
    fun testMoveForwardOnEmptyStackDoesNothing() {
        tab.moveForward()
        assertEquals(-1, tab.backStackPosition)
    }

    // ---- back and forward round-trip ----

    @Test
    fun testBackAndForwardRoundTrip() {
        tab.pushBackStackItem(makeItem("A"))
        tab.pushBackStackItem(makeItem("B"))
        tab.pushBackStackItem(makeItem("C"))
        tab.moveBack()
        tab.moveBack()
        assertEquals("A", tab.backStackPositionTitle?.displayText)
        tab.moveForward()
        assertEquals("B", tab.backStackPositionTitle?.displayText)
        tab.moveForward()
        assertEquals("C", tab.backStackPositionTitle?.displayText)
        tab.moveBack()
        assertEquals("B", tab.backStackPositionTitle?.displayText)
        tab.moveBack()
        assertEquals("A", tab.backStackPositionTitle?.displayText)
    }

    // ---- clearBackstack ----

    @Test
    fun testClearBackstackEmptiesStack() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.clearBackstack()
        assertEquals(0, tab.backStack.size)
    }

    @Test
    fun testClearBackstackResetsPosition() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.clearBackstack()
        assertEquals(-1, tab.backStackPosition)
    }

    @Test
    fun testClearBackstackTitleIsNull() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.clearBackstack()
        assertNull(tab.backStackPositionTitle)
    }

    // ---- squashBackstack ----

    @Test
    fun testSquashBackstackKeepsOnlyLastItem() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.pushBackStackItem(makeItem("Page3"))
        tab.squashBackstack()
        assertEquals(1, tab.backStack.size)
        assertEquals("Page3", tab.backStack[0].title.displayText)
    }

    @Test
    fun testSquashBackstackSetsPositionToZero() {
        tab.pushBackStackItem(makeItem("Page1"))
        tab.pushBackStackItem(makeItem("Page2"))
        tab.squashBackstack()
        assertEquals(0, tab.backStackPosition)
    }

    @Test
    fun testSquashBackstackOnEmptyDoesNothing() {
        tab.squashBackstack()
        assertEquals(0, tab.backStack.size)
        assertEquals(-1, tab.backStackPosition)
    }

    @Test
    fun testSquashBackstackOnSingleItemKeepsIt() {
        tab.pushBackStackItem(makeItem("Only"))
        tab.squashBackstack()
        assertEquals(1, tab.backStack.size)
        assertEquals("Only", tab.backStack[0].title.displayText)
        assertEquals(0, tab.backStackPosition)
    }

    // ---- setBackStackPositionTitle ----

    @Test
    fun testSetBackStackPositionTitle() {
        tab.pushBackStackItem(makeItem("Old"))
        val newTitle = PageTitle("New", enwiki)
        tab.setBackStackPositionTitle(newTitle)
        assertEquals("New", tab.backStackPositionTitle?.displayText)
    }

    @Test
    fun testSetBackStackPositionTitleOnEmptyDoesNothing() {
        val newTitle = PageTitle("New", enwiki)
        tab.setBackStackPositionTitle(newTitle)
        assertNull(tab.backStackPositionTitle)
    }

    // ---- backStackPosition edge cases ----

    @Test
    fun testBackStackPositionReturnsLastIndexWhenNegative() {
        tab.pushBackStackItem(makeItem("A"))
        tab.pushBackStackItem(makeItem("B"))
        // Force internal position to -1 via reflection or just test that getter normalizes
        // Default is -1, which getter maps to size-1
        tab.clearBackstack()
        assertEquals(-1, tab.backStackPosition) // -1 < 0, backStack is empty so size-1 = -1 (empty list)
    }

    @Test
    fun testPushManyItemsPerformance() {
        for (i in 1..100) {
            tab.pushBackStackItem(makeItem("Page$i"))
        }
        assertEquals(100, tab.backStack.size)
        assertEquals(99, tab.backStackPosition)
        assertTrue(tab.canGoBack())
        assertFalse(tab.canGoForward())
    }
}
