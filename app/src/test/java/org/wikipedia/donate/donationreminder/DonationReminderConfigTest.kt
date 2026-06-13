package org.wikipedia.donate.donationreminder

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.settings.Prefs
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class DonationReminderConfigTest {

    @Before
    fun setUp() {
        Prefs.donationReminderConfig = DonationReminderConfig()
    }

    @After
    fun tearDown() {
        Prefs.donationReminderConfig = DonationReminderConfig()
    }

    @Test
    fun testIsSetupReturnsFalseByDefault() {
        val config = DonationReminderConfig()
        assertFalse(config.isSetup)
    }

    @Test
    fun testIsSetupReturnsTrueWhenConfigured() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 10,
            donateAmount = 5f
        )
        assertTrue(config.isSetup)
    }

    @Test
    fun testIsSetupReturnsFalseWhenNotEnabled() {
        val config = DonationReminderConfig(
            userEnabled = false,
            setupTimestamp = 12345L,
            articleFrequency = 10
        )
        assertFalse(config.isSetup)
    }

    @Test
    fun testIsSetupReturnsFalseWhenNoTimestamp() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 0L,
            articleFrequency = 10
        )
        assertFalse(config.isSetup)
    }

    @Test
    fun testIsSetupReturnsFalseWhenNoFrequency() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 0
        )
        assertFalse(config.isSetup)
    }

    @Test
    fun testShouldShowNowReturnsFalseWhenNotSetup() {
        val config = DonationReminderConfig(
            userEnabled = false,
            isReminderReady = true,
            promptLastSeen = 0
        )
        assertFalse(config.shouldShowNow())
    }

    @Test
    fun testShouldShowNowReturnsFalseWhenNotReady() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 10,
            isReminderReady = false
        )
        assertFalse(config.shouldShowNow())
    }

    @Test
    fun testShouldShowNowReturnsFalseWhenMaxPromptsReached() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 10,
            isReminderReady = true,
            timesReminderShown = 2
        )
        assertFalse(config.shouldShowNow())
    }

    @Test
    fun testShouldShowNowReturnsTrueWhenSetupAndNotSeenToday() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 10,
            isReminderReady = true,
            timesReminderShown = 0,
            promptLastSeen = LocalDate.now().toEpochDay() - 1 // Last seen yesterday
        )
        assertTrue(config.shouldShowNow())
    }

    @Test
    fun testShouldShowNowReturnsFalseWhenAlreadySeenToday() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 10,
            isReminderReady = true,
            timesReminderShown = 0,
            promptLastSeen = LocalDate.now().toEpochDay() // Seen today
        )
        assertFalse(config.shouldShowNow())
    }

    @Test
    fun testShouldShowNowWhenOverMaxReminders() {
        val config = DonationReminderConfig(
            userEnabled = true,
            setupTimestamp = 12345L,
            articleFrequency = 10,
            isReminderReady = true,
            timesReminderShown = 3, // Over MAX_REMINDER_PROMPTS (2)
            promptLastSeen = LocalDate.now().toEpochDay() - 1
        )
        assertFalse(config.shouldShowNow())
    }

    @Test
    fun testDefaultConfigValues() {
        val config = DonationReminderConfig()
        assertFalse(config.userEnabled)
        assertEquals(0L, config.promptLastSeen)
        assertEquals(0L, config.setupTimestamp)
        assertEquals(0, config.articleVisit)
        assertEquals(0, config.articleFrequency)
        assertEquals(0f, config.donateAmount)
        assertFalse(config.isReminderReady)
        assertEquals(0, config.timesReminderShown)
        assertEquals(0, config.goalReachedCount)
    }

    @Test
    fun testCopyModifiesSpecificField() {
        val config = DonationReminderConfig(articleFrequency = 10)
        val newConfig = config.copy(articleFrequency = 25)
        assertEquals(25, newConfig.articleFrequency)
        assertEquals(10, config.articleFrequency) // Original unchanged
    }
}
