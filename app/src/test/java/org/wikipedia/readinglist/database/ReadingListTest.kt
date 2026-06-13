package org.wikipedia.readinglist.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.Namespace

@RunWith(RobolectricTestRunner::class)
class ReadingListTest {

    private lateinit var enwiki: WikiSite

    @Before
    fun setUp() {
        enwiki = WikiSite.forLanguageCode("en")
    }

    private fun makePage(title: String, mtime: Long = 0, offline: Boolean = false,
                         status: Long = ReadingListPage.STATUS_SAVED, sizeBytes: Long = 0): ReadingListPage {
        return ReadingListPage(enwiki, Namespace.MAIN, title, title, mtime = mtime, atime = mtime,
            offline = offline, status = status, sizeBytes = sizeBytes).also {
            if (offline && status == ReadingListPage.STATUS_SAVED) {
                // ensures numPagesOffline calculation works
            }
        }
    }

    private fun makeList(title: String, id: Long = 1): ReadingList {
        return ReadingList(title, null, id = id)
    }

    // ---- sort constants ----

    @Test
    fun testSortConstants() {
        assertEquals(0, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(1, ReadingList.SORT_BY_NAME_DESC)
        assertEquals(2, ReadingList.SORT_BY_RECENT_ASC)
        assertEquals(3, ReadingList.SORT_BY_RECENT_DESC)
    }

    // ---- sort(list, sortMode) ----

    @Test
    fun testSortSingleListByNameAsc() {
        val list = makeList("MyList")
        list.pages.addAll(listOf(makePage("C", mtime = 300), makePage("A", mtime = 100), makePage("B", mtime = 200)))
        ReadingList.sort(list, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(listOf("A", "B", "C"), list.pages.map { it.apiTitle })
    }

    @Test
    fun testSortSingleListByNameDesc() {
        val list = makeList("MyList")
        list.pages.addAll(listOf(makePage("A", mtime = 100), makePage("C", mtime = 300), makePage("B", mtime = 200)))
        ReadingList.sort(list, ReadingList.SORT_BY_NAME_DESC)
        assertEquals(listOf("C", "B", "A"), list.pages.map { it.apiTitle })
    }

    @Test
    fun testSortSingleListByRecentAsc() {
        val list = makeList("MyList")
        list.pages.addAll(listOf(makePage("C", mtime = 300), makePage("A", mtime = 100), makePage("B", mtime = 200)))
        ReadingList.sort(list, ReadingList.SORT_BY_RECENT_ASC)
        assertEquals(listOf("A", "B", "C"), list.pages.map { it.apiTitle })
    }

    @Test
    fun testSortSingleListByRecentDesc() {
        val list = makeList("MyList")
        list.pages.addAll(listOf(makePage("A", mtime = 100), makePage("C", mtime = 300), makePage("B", mtime = 200)))
        ReadingList.sort(list, ReadingList.SORT_BY_RECENT_DESC)
        assertEquals(listOf("C", "B", "A"), list.pages.map { it.apiTitle })
    }

    @Test
    fun testSortEmptyListDoesNotThrow() {
        val list = makeList("MyList")
        ReadingList.sort(list, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(0, list.pages.size)
    }

    @Test
    fun testSortSinglePageListStaysSame() {
        val list = makeList("MyList")
        list.pages.add(makePage("Only"))
        ReadingList.sort(list, ReadingList.SORT_BY_NAME_DESC)
        assertEquals(1, list.pages.size)
        assertEquals("Only", list.pages[0].apiTitle)
    }

    // ---- sort(lists, sortMode) ----

    @Test
    fun testSortListsByNameAsc() {
        val lists = mutableListOf(makeList("C"), makeList("A"), makeList("B"))
        ReadingList.sort(lists, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(listOf("A", "B", "C"), lists.map { it.listTitle })
    }

    @Test
    fun testSortListsByNameDesc() {
        val lists = mutableListOf(makeList("A"), makeList("C"), makeList("B"))
        ReadingList.sort(lists, ReadingList.SORT_BY_NAME_DESC)
        assertEquals(listOf("C", "B", "A"), lists.map { it.listTitle })
    }

    @Test
    fun testSortListsByRecentAsc() {
        // SORT_BY_RECENT_ASC = newest first
        val lists = mutableListOf(
            makeList("A").also { it.mtime = 100; it.atime = 100 },
            makeList("B").also { it.mtime = 200; it.atime = 200 },
            makeList("C").also { it.mtime = 300; it.atime = 300 }
        )
        ReadingList.sort(lists, ReadingList.SORT_BY_RECENT_ASC)
        assertEquals(listOf("C", "B", "A"), lists.map { it.listTitle })
    }

    @Test
    fun testSortListsByRecentDesc() {
        // SORT_BY_RECENT_DESC = oldest first
        val lists = mutableListOf(
            makeList("C").also { it.mtime = 300; it.atime = 300 },
            makeList("A").also { it.mtime = 100; it.atime = 100 },
            makeList("B").also { it.mtime = 200; it.atime = 200 }
        )
        ReadingList.sort(lists, ReadingList.SORT_BY_RECENT_DESC)
        assertEquals(listOf("A", "B", "C"), lists.map { it.listTitle })
    }

    @Test
    fun testSortListsKeepsDefaultListOnTop() {
        // Default list name comes from L10nUtil.getString(R.string.default_reading_list_name)
        // which in Robolectric test defaults to "Saved"
        val defaultList = makeList("")
        val lists = mutableListOf(makeList("B"), defaultList, makeList("A"))
        ReadingList.sort(lists, ReadingList.SORT_BY_NAME_ASC)
        assertEquals("", lists[0].listTitle)
        assertEquals(listOf("", "A", "B"), lists.map { it.listTitle })
    }

    @Test
    fun testSortListsDefaultAlreadyOnTopStaysOnTop() {
        val defaultList = makeList("")
        val lists = mutableListOf(defaultList, makeList("B"), makeList("A"))
        ReadingList.sort(lists, ReadingList.SORT_BY_RECENT_DESC)
        assertEquals("", lists[0].listTitle)
    }

    @Test
    fun testSortEmptyLists() {
        val lists = mutableListOf<ReadingList>()
        ReadingList.sort(lists, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(0, lists.size)
    }

    @Test
    fun testSortListsSingleItem() {
        val lists = mutableListOf(makeList("Only"))
        ReadingList.sort(lists, ReadingList.SORT_BY_RECENT_ASC)
        assertEquals(1, lists.size)
        assertEquals("Only", lists[0].listTitle)
    }

    // ---- sortGenericList ----

    @Test
    fun testSortGenericListByNameAsc() {
        val lists = mutableListOf<Any>(makeList("C"), makeList("A"), makeList("B"))
        ReadingList.sortGenericList(lists, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(listOf("A", "B", "C"), lists.filterIsInstance<ReadingList>().map { it.listTitle })
    }

    @Test
    fun testSortGenericListByNameDesc() {
        val lists = mutableListOf<Any>(makeList("A"), makeList("C"), makeList("B"))
        ReadingList.sortGenericList(lists, ReadingList.SORT_BY_NAME_DESC)
        assertEquals(listOf("C", "B", "A"), lists.filterIsInstance<ReadingList>().map { it.listTitle })
    }

    @Test
    fun testSortGenericListDefaultOnTop() {
        val defaultList = makeList("")
        val lists = mutableListOf<Any>(makeList("B"), defaultList, makeList("A"))
        ReadingList.sortGenericList(lists, ReadingList.SORT_BY_NAME_ASC)
        assertEquals("", (lists[0] as ReadingList).listTitle)
    }

    @Test
    fun testSortGenericListWithNonReadingListItems() {
        val lists = mutableListOf<Any>("NotAList", makeList("B"), makeList("A"))
        ReadingList.sortGenericList(lists, ReadingList.SORT_BY_NAME_ASC)
        // Non-ReadingList items should be included at original positions, ReadingLists sorted
        assertTrue(lists.any { it == "NotAList" })
        val sortedLists = lists.filterIsInstance<ReadingList>()
        assertEquals(2, sortedLists.size)
    }

    @Test
    fun testSortGenericListEmpty() {
        val lists = mutableListOf<Any>()
        ReadingList.sortGenericList(lists, ReadingList.SORT_BY_NAME_ASC)
        assertEquals(0, lists.size)
    }

    // ---- touch ----

    @Test
    fun testTouchUpdatesAtime() {
        val list = makeList("MyList")
        val originalAtime = list.atime
        Thread.sleep(10)
        list.touch()
        assertTrue(list.atime > originalAtime)
    }

    // ---- compareTo ----

    @Test
    fun testCompareToSameList() {
        val list1 = ReadingList("Title", "desc", id = 1)
        list1.pages.add(makePage("Page"))
        val list2 = ReadingList("Title", "desc", id = 1)
        list2.pages.add(makePage("Page"))
        assertTrue(list1.compareTo(list2))
        assertTrue(list2.compareTo(list1))
    }

    @Test
    fun testCompareToDifferentId() {
        val list1 = ReadingList("Title", null, id = 1)
        val list2 = ReadingList("Title", null, id = 2)
        assertFalse(list1.compareTo(list2))
    }

    @Test
    fun testCompareToDifferentDescription() {
        val list1 = ReadingList("Title", "desc1", id = 1)
        val list2 = ReadingList("Title", "desc2", id = 1)
        assertFalse(list1.compareTo(list2))
    }

    @Test
    fun testCompareToDifferentPageCount() {
        val list1 = ReadingList("Title", null, id = 1)
        list1.pages.add(makePage("Page"))
        val list2 = ReadingList("Title", null, id = 1)
        assertFalse(list1.compareTo(list2))
    }

    @Test
    fun testCompareToDifferentTypes() {
        val list1 = ReadingList("Title", null, id = 1)
        assertFalse(list1.compareTo("NotReadingList"))
    }

    @Test
    fun testCompareToDifferentTitle() {
        val list1 = ReadingList("Title1", null, id = 1)
        val list2 = ReadingList("Title2", null, id = 1)
        assertFalse(list1.compareTo(list2))
    }

    @Test
    fun testCompareToSamePagesButDifferentOffline() {
        val list1 = ReadingList("Title", null, id = 1)
        list1.pages.add(makePage("Page", offline = true, status = ReadingListPage.STATUS_SAVED))
        val list2 = ReadingList("Title", null, id = 1)
        list2.pages.add(makePage("Page", offline = false))
        assertFalse(list1.compareTo(list2))
    }

    // ---- numPagesOffline ----

    @Test
    fun testNumPagesOfflineAllOffline() {
        val list = makeList("MyList")
        list.pages.add(makePage("P1", offline = true, status = ReadingListPage.STATUS_SAVED))
        list.pages.add(makePage("P2", offline = true, status = ReadingListPage.STATUS_SAVED))
        assertEquals(2, list.numPagesOffline)
    }

    @Test
    fun testNumPagesOfflineNoneOffline() {
        val list = makeList("MyList")
        list.pages.add(makePage("P1", offline = false))
        list.pages.add(makePage("P2", offline = false))
        assertEquals(0, list.numPagesOffline)
    }

    @Test
    fun testNumPagesOfflineMixedStatus() {
        val list = makeList("MyList")
        list.pages.add(makePage("P1", offline = true, status = ReadingListPage.STATUS_SAVED))
        list.pages.add(makePage("P2", offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_SAVE))
        list.pages.add(makePage("P3", offline = true, status = ReadingListPage.STATUS_QUEUE_FOR_DELETE))
        list.pages.add(makePage("P4", offline = false))
        assertEquals(1, list.numPagesOffline) // only P1 is offline AND saved
    }

    @Test
    fun testNumPagesOfflineEmptyPages() {
        val list = makeList("MyList")
        assertEquals(0, list.numPagesOffline)
    }

    // ---- sizeBytesFromPages ----

    @Test
    fun testSizeBytesFromPagesSumOfflinePages() {
        val list = makeList("MyList")
        list.pages.add(makePage("P1", offline = true, sizeBytes = 1024))
        list.pages.add(makePage("P2", offline = true, sizeBytes = 2048))
        assertEquals(3072, list.sizeBytesFromPages)
    }

    @Test
    fun testSizeBytesFromPagesIgnoresOnlinePages() {
        val list = makeList("MyList")
        list.pages.add(makePage("P1", offline = true, sizeBytes = 1024))
        list.pages.add(makePage("P2", offline = false, sizeBytes = 2048))
        assertEquals(1024, list.sizeBytesFromPages)
    }

    @Test
    fun testSizeBytesFromPagesEmptyPages() {
        val list = makeList("MyList")
        assertEquals(0, list.sizeBytesFromPages)
    }

    // ---- pages list ----

    @Test
    fun testPagesListInitiallyEmpty() {
        val list = makeList("MyList")
        assertEquals(0, list.pages.size)
    }

    // ---- selected ----

    @Test
    fun testSelectedDefaultFalse() {
        val list = makeList("MyList")
        assertFalse(list.selected)
    }

    @Test
    fun testSelectedCanBeSet() {
        val list = makeList("MyList")
        list.selected = true
        assertTrue(list.selected)
    }
}
