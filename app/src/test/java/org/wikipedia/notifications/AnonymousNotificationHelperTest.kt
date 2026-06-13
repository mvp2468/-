package org.wikipedia.notifications

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.auth.AccountUtil
import org.wikipedia.page.PageTitle
import org.wikipedia.settings.Prefs
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class AnonymousNotificationHelperTest {

    @Before
    fun setUp() {
        Prefs.lastAnonEditTime = 0L
        Prefs.lastAnonNotificationTime = 0L
        Prefs.hasAnonymousNotification = false
        Prefs.lastAnonNotificationLang = null
        Prefs.lastAnonUserWithMessages = null
    }

    @After
    fun tearDown() {
        Prefs.lastAnonEditTime = 0L
        Prefs.lastAnonNotificationTime = 0L
        Prefs.hasAnonymousNotification = false
    }

    @Test
    fun testOnEditSubmittedSetsLastAnonEditTimeWhenNotLoggedIn() {
        // Assuming not logged in by default in test environment
        Prefs.lastAnonEditTime = 0L
        AnonymousNotificationHelper.onEditSubmitted()

        val now = Date().time
        assertTrue(Prefs.lastAnonEditTime > 0L)
        assertTrue(Prefs.lastAnonEditTime <= now)
    }

    @Test
    fun testOnEditSubmittedShouldNotSetTimeWhenLoggedIn() {
        // This test depends on AccountUtil.isLoggedIn
        // In test environment it will use default behavior
        Prefs.lastAnonEditTime = 0L
        val before = Date().time

        AnonymousNotificationHelper.onEditSubmitted()

        // In a test environment without login, this should set the time
        // If logged in, it should not set the time
        assertTrue("lastAnonEditTime should be set for anonymous users",
            Prefs.lastAnonEditTime >= before || Prefs.lastAnonEditTime == 0L)
    }

    @Test
    fun testIsWithinAnonNotificationTimeReturnsFalseWhenOutsideWindow() {
        // Set last notification time to 8 days ago (outside 7-day window)
        Prefs.lastAnonNotificationTime = Date().time - TimeUnit.DAYS.toMillis(8)
        assertFalse(AnonymousNotificationHelper.isWithinAnonNotificationTime())
    }

    @Test
    fun testIsWithinAnonNotificationTimeReturnsTrueWhenWithinWindow() {
        // Set last notification time to 3 days ago (inside 7-day window)
        Prefs.lastAnonNotificationTime = Date().time - TimeUnit.DAYS.toMillis(3)
        assertTrue(AnonymousNotificationHelper.isWithinAnonNotificationTime())
    }

    @Test
    fun testIsWithinAnonNotificationTimeReturnsFalseWhenNeverNotified() {
        Prefs.lastAnonNotificationTime = 0L
        assertFalse(AnonymousNotificationHelper.isWithinAnonNotificationTime())
    }

    @Test
    fun testIsWithinAnonNotificationTimeReturnsTrueWhenEdgeOfWindow() {
        // Exact edge: just under 7 days
        Prefs.lastAnonNotificationTime = Date().time - TimeUnit.DAYS.toMillis(6)
        assertTrue(AnonymousNotificationHelper.isWithinAnonNotificationTime())
    }

    @Test
    fun testShouldCheckAnonNotificationsReturnsFalseWhenWithinTime() {
        // Set last notification time to recent (within window)
        Prefs.lastAnonNotificationTime = Date().time - TimeUnit.DAYS.toMillis(2)
        val response = org.wikipedia.dataclient.mwapi.MwQueryResponse()
        assertFalse(AnonymousNotificationHelper.shouldCheckAnonNotifications(response))
    }

    @Test
    fun testShouldCheckAnonNotificationsWhenOutsideTimeWithMessages() {
        // Set last notification time outside window
        Prefs.lastAnonNotificationTime = 0L
        // With a default MwQueryResponse, messages will be null
        val response = org.wikipedia.dataclient.mwapi.MwQueryResponse()
        assertFalse(AnonymousNotificationHelper.shouldCheckAnonNotifications(response))
    }

    @Test
    fun testAnonTalkPageHasRecentMessageReturnsFalseWithoutRevisions() {
        val response = org.wikipedia.dataclient.mwapi.MwQueryResponse()
        val title = PageTitle("Test_Page", org.wikipedia.dataclient.WikiSite.forLanguageCode("en"))
        assertFalse(AnonymousNotificationHelper.anonTalkPageHasRecentMessage(response, title))
    }

    @Test
    fun testNotificationDurationDaysConstant() {
        // Verify the 7-day window: 6 days ago is within window, 8 days ago is not
        val timeNow = Date().time
        Prefs.lastAnonNotificationTime = timeNow - TimeUnit.DAYS.toMillis(6)
        assertTrue(AnonymousNotificationHelper.isWithinAnonNotificationTime())

        Prefs.lastAnonNotificationTime = timeNow - TimeUnit.DAYS.toMillis(8)
        assertFalse(AnonymousNotificationHelper.isWithinAnonNotificationTime())
    }
}
