package org.wikipedia.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.wikipedia.WikipediaApp
import org.wikipedia.history.db.HistoryEntryDao
import org.wikipedia.history.db.HistoryEntryWithImageDao
import org.wikipedia.offline.db.OfflineObjectDao
import org.wikipedia.pageimages.db.PageImageDao
import org.wikipedia.readinglist.db.ReadingListDao
import org.wikipedia.readinglist.db.ReadingListPageDao
import org.wikipedia.search.db.RecentSearchDao
import org.wikipedia.talk.db.TalkPageSeenDao

@RunWith(Parameterized::class)
class UpgradeFromPreRoomTest(private val fromVersion: Int) {
    private lateinit var db: AppDatabase
    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var readingListDao: ReadingListDao
    private lateinit var readingListPageDao: ReadingListPageDao
    private lateinit var pageImageDao: PageImageDao
    private lateinit var historyDao: HistoryEntryDao
    private lateinit var historyWithImageDao: HistoryEntryWithImageDao
    private lateinit var offlineObjectDao: OfflineObjectDao
    private lateinit var talkPageSeenDao: TalkPageSeenDao

    @Before
    fun createDb() {
        // Reset app language to English to avoid locale mismatches in migration tests.
        WikipediaApp.instance.languageState.let {
            it.removeAppLanguageCodes(it.appLanguageCodes.filter { it != "en" })
        }

        val helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), AppDatabase::class.java)

        var helperDb = helper.createDatabase(DB_NAME, fromVersion)
        InstrumentationRegistry.getInstrumentation().context.assets.open("database/wikipedia_v$fromVersion.sql").bufferedReader().lines().forEach {
            helperDb.execSQL(it)
        }
        helperDb.close()

        helperDb = helper.runMigrationsAndValidate(DB_NAME, DATABASE_VERSION, true,
            AppDatabase.MIGRATION_19_20, AppDatabase.MIGRATION_20_21, AppDatabase.MIGRATION_21_22, AppDatabase.MIGRATION_22_23,
            AppDatabase.MIGRATION_23_24, AppDatabase.MIGRATION_24_25, AppDatabase.MIGRATION_25_26, AppDatabase.MIGRATION_26_27,
            AppDatabase.MIGRATION_27_28, AppDatabase.MIGRATION_28_29, AppDatabase.MIGRATION_29_30, AppDatabase.MIGRATION_30_31,
            AppDatabase.MIGRATION_31_32)
        helperDb.close()

        db = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java, DB_NAME)
            .addMigrations(AppDatabase.MIGRATION_19_20, AppDatabase.MIGRATION_20_21, AppDatabase.MIGRATION_21_22, AppDatabase.MIGRATION_22_23,
                AppDatabase.MIGRATION_23_24, AppDatabase.MIGRATION_24_25, AppDatabase.MIGRATION_25_26, AppDatabase.MIGRATION_26_27,
                AppDatabase.MIGRATION_27_28, AppDatabase.MIGRATION_28_29, AppDatabase.MIGRATION_29_30, AppDatabase.MIGRATION_30_31,
                AppDatabase.MIGRATION_31_32)
            .fallbackToDestructiveMigration()
            .build()
        recentSearchDao = db.recentSearchDao()
        readingListDao = db.readingListDao()
        readingListPageDao = db.readingListPageDao()
        historyDao = db.historyEntryDao()
        historyWithImageDao = db.historyEntryWithImageDao()
        pageImageDao = db.pageImagesDao()
        offlineObjectDao = db.offlineObjectDao()
        talkPageSeenDao = db.talkPageSeenDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testTablesAfterMigration() = runBlocking {
        val recentSearches = recentSearchDao.getRecentSearches()
        assertEquals(4, recentSearches.size)
        assertEquals("obama", recentSearches[0].text)
        assertEquals("trump", recentSearches[3].text)

        val readingLists = readingListDao.getAllLists()
        assertEquals(3, readingLists.size)
        assertEquals(2, readingLists[0].pages.size)

        assertEquals("Barack_Obama", readingLists[0].pages[1].apiTitle)
        assertEquals("Barack Obama", readingLists[0].pages[1].displayTitle)
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/256px-President_Barack_Obama.jpg", readingLists[0].pages[1].thumbUrl)
        assertTrue("Expected 'en' or 'zh-cn' but got '${readingLists[0].pages[1].lang}'",
            readingLists[0].pages[1].lang in listOf("en", "zh-cn"))
        assertEquals("44th president of the United States", readingLists[0].pages[1].description)
        assertEquals(5695183, readingLists[0].pages[1].sizeBytes)
        assertEquals(44, readingLists[0].pages[1].remoteId)

        assertEquals("Joe Biden", readingLists[1].pages[0].apiTitle)
        assertEquals("Joe Biden", readingLists[1].pages[0].displayTitle)
        assertNull(readingLists[1].pages[0].thumbUrl)
        assertTrue("Expected 'en' or 'zh-cn' but got '${readingLists[1].pages[0].lang}'",
            readingLists[1].pages[0].lang in listOf("en", "zh-cn"))
        assertNull(readingLists[1].pages[0].description)
        assertEquals(43, readingLists[1].pages[0].remoteId)

        assertEquals("People", readingLists[1].title)
        assertEquals("", readingLists[1].description)
        assertEquals(101, readingLists[1].remoteId)
        assertEquals(3, readingLists[1].pages.size)

        assertEquals("More people", readingLists[2].title)
        assertEquals("Example list description", readingLists[2].description)
        assertEquals(1, readingLists[2].pages.size)
        assertEquals("ברק_אובמה", readingLists[2].pages[0].apiTitle)

        val pageImages = pageImageDao.getAllPageImages()
        assertEquals(7, pageImages.size)
        assertEquals("Barack_Obama", pageImages[0].apiTitle)
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/256px-President_Barack_Obama.jpg", pageImages[0].imageName)

        val historyEntries = historyWithImageDao.findEntriesBySearchTerm("%%")
        assertEquals(6, historyEntries.size)
        assertEquals("ברק_אובמה", historyEntries[0].apiTitle)
        assertEquals("ברק אובמה", historyEntries[0].displayTitle)
        assertEquals("he", historyEntries[0].lang)
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/256px-President_Barack_Obama.jpg", historyEntries[0].imageName)
        assertEquals("Joe_Biden", historyEntries[4].apiTitle)
        assertTrue("Expected 'en' or 'zh-cn' but got '${historyEntries[4].lang}'",
            historyEntries[4].lang in listOf("en", "zh-cn"))

        val historyEntry = historyDao.findEntryBy("ru.wikipedia.org", "ru", "Обама,_Барак")!!
        assertEquals("Обама, Барак", historyEntry.displayTitle)

        val talkPageSeen = talkPageSeenDao.getAll()
        if (fromVersion == 22) {
            assertEquals(2, talkPageSeen.count())
            assertEquals("/data/user/0/org.wikipedia.dev/files/offline_files/481b1ef996728fd9994bd97ab19733d8", offlineObjectDao.getOfflineObject("https://en.wikipedia.org/api/rest_v1/page/summary/Joe_Biden")!!.path)
        } else {
            assertEquals(0, talkPageSeen.count())
            assertNull(offlineObjectDao.getOfflineObject("https://en.wikipedia.org/api/rest_v1/page/summary/Joe_Biden"))
        }
    }

    companion object {
        const val DB_NAME = "wikipedia.db"

        @JvmStatic
        @Parameterized.Parameters
        fun params(): List<Any> {
            return listOf(19, 22)
        }
    }
}
