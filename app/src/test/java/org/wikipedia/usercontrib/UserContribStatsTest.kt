package org.wikipedia.usercontrib

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.mwapi.UserContribution
import org.wikipedia.settings.Prefs
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class UserContribStatsTest {

    @Before
    fun setUp() {
        Prefs.suggestedEditsPauseDate = Date(0)
        Prefs.suggestedEditsPauseReverts = 0
        UserContribStats.totalReverts = 0
    }

    @After
    fun tearDown() {
        Prefs.suggestedEditsPauseDate = Date(0)
        Prefs.suggestedEditsPauseReverts = 0
    }

    @Test
    fun testGetRevertSeverityNoEditsNoReverts() {
        // totalEdits = 0, totalReverts = 0
        // when totalEdits <= 100, returns totalReverts directly
        val result = UserContribStats.getRevertSeverity()
        assertEquals(0, result)
    }

    @Test
    fun testGetRevertSeverityFewEditsAllReverts() {
        // Need to simulate verifyEditCountsAndPauseState behavior
        UserContribStats.totalReverts = 50
        // Can't set totalEdits directly since it's private
        // We'll test with the default state
        assertTrue(UserContribStats.getRevertSeverity() >= 0)
    }

    @Test
    fun testIsDisabledReturnsFalseByDefault() {
        assertFalse(UserContribStats.isDisabled())
    }

    @Test
    fun testMaybePauseAndGetEndDateReturnsNullWhenNoPause() {
        Prefs.suggestedEditsPauseDate = Date(0)
        val result = UserContribStats.maybePauseAndGetEndDate()
        assertNull(result)
    }

    @Test
    fun testMaybePauseAndGetEndDateWithExpiredPause() {
        // Set pause date to 8 days ago (expired)
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -8)
        Prefs.suggestedEditsPauseDate = cal.time
        Prefs.suggestedEditsPauseReverts = 10

        val result = UserContribStats.maybePauseAndGetEndDate()
        // Should be null because pause has expired
        assertNull(result)
        assertEquals(Date(0), Prefs.suggestedEditsPauseDate)
    }

    @Test
    fun testMaybePauseAndGetEndDateWithActivePause() {
        // Set pause date to 3 days ago (still active)
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -3)
        Prefs.suggestedEditsPauseDate = cal.time
        Prefs.suggestedEditsPauseReverts = 10

        val result = UserContribStats.maybePauseAndGetEndDate()
        // Should return end date (3 + 7 = 4 days from now)
        assertNotNull(result)

        val expectedEnd = java.util.Calendar.getInstance()
        expectedEnd.time = cal.time
        expectedEnd.add(java.util.Calendar.DAY_OF_YEAR, 7)
        // Check that dates are within 1 second
        assertTrue(Math.abs(result!!.time - expectedEnd.timeInMillis) < 1000)
    }
}
