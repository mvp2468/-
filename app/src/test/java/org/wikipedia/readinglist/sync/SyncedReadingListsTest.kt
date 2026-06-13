package org.wikipedia.readinglist.sync

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.Normalizer
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class SyncedReadingListsTest {

    @Test
    fun testRemoteReadingListNameNormalized() {
        val list = SyncedReadingLists.RemoteReadingList(
            id = 1,
            name = "Caf\u00E9 List",
            description = "A list with accents",
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
        assertEquals(Normalizer.normalize("Café List", Normalizer.Form.NFC), list.name())
    }

    @Test
    fun testRemoteReadingListNameAlreadyNormalized() {
        val list = SyncedReadingLists.RemoteReadingList(
            id = 1,
            name = "Simple List",
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
        assertEquals("Simple List", list.name())
    }

    @Test
    fun testRemoteReadingListDescriptionNullReturnsEmpty() {
        val list = SyncedReadingLists.RemoteReadingList(
            id = 1,
            name = "test",
            description = null,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
        assertEquals("", list.description())
    }

    @Test
    fun testRemoteReadingListDescriptionNormalized() {
        val list = SyncedReadingLists.RemoteReadingList(
            id = 1,
            name = "test",
            description = "café résumé",
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
        assertEquals(Normalizer.normalize("café résumé", Normalizer.Form.NFC), list.description())
    }

    @Test
    fun testRemoteReadingListProperties() {
        val now = LocalDateTime.now()
        val list = SyncedReadingLists.RemoteReadingList(
            id = 42,
            isDefault = true,
            name = "Default List",
            isDeleted = false,
            created = now,
            updated = now
        )
        assertEquals(42L, list.id)
        assertTrue(list.isDefault)
        assertFalse(list.isDeleted)
    }

    @Test
    fun testRemoteReadingListEntryProjectNormalized() {
        val entry = SyncedReadingLists.RemoteReadingListEntry(
            id = 1,
            listId = 1,
            project = "enwikivoyage",
            title = "Paris",
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
        assertEquals("enwikivoyage", entry.project())
    }

    @Test
    fun testRemoteReadingListEntryTitleNormalized() {
        val entry = SyncedReadingLists.RemoteReadingListEntry(
            id = 1,
            listId = 1,
            project = "enwiki",
            title = "Caf\u00E9",
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
        assertEquals(Normalizer.normalize("Café", Normalizer.Form.NFC), entry.title())
    }

    @Test
    fun testRemoteReadingListEntryProperties() {
        val now = LocalDateTime.now()
        val entry = SyncedReadingLists.RemoteReadingListEntry(
            id = 100,
            listId = 200,
            project = "enwiki",
            title = "Test Article",
            isDeleted = false,
            created = now,
            updated = now
        )
        assertEquals(100L, entry.id)
        assertEquals(200L, entry.listId)
        assertFalse(entry.isDeleted)
    }
}
