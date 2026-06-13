package org.wikipedia.activitytab

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActivityTabModulesTest {

    @Test
    fun testAllModulesEnabledByDefaultExceptDonations() {
        val modules = ActivityTabModules()
        assertTrue(modules.isModuleEnabled(ModuleType.TIME_SPENT))
        assertTrue(modules.isModuleEnabled(ModuleType.READING_INSIGHTS))
        assertTrue(modules.isModuleEnabled(ModuleType.EDITING_INSIGHTS))
        assertTrue(modules.isModuleEnabled(ModuleType.IMPACT))
        assertTrue(modules.isModuleEnabled(ModuleType.GAMES))
        assertFalse(modules.isModuleEnabled(ModuleType.DONATIONS))
        assertTrue(modules.isModuleEnabled(ModuleType.TIMELINE))
    }

    @Test
    fun testSetModuleEnabledReturnsNewCopy() {
        val modules = ActivityTabModules()
        val updated = modules.setModuleEnabled(ModuleType.DONATIONS, true)
        assertFalse(modules.isModuleEnabled(ModuleType.DONATIONS))
        assertTrue(updated.isModuleEnabled(ModuleType.DONATIONS))
    }

    @Test
    fun testSetModuleEnabledDisableReadingInsights() {
        val modules = ActivityTabModules()
        val updated = modules.setModuleEnabled(ModuleType.READING_INSIGHTS, false)
        assertTrue(modules.isModuleEnabled(ModuleType.READING_INSIGHTS))
        assertFalse(updated.isModuleEnabled(ModuleType.READING_INSIGHTS))
    }

    @Test
    fun testIsModuleVisibleForDonationsRequiresDonation() {
        val modules = ActivityTabModules(isDonationsEnabled = true)
        assertTrue(modules.isModuleVisible(ModuleType.DONATIONS, haveAtLeastOneDonation = true))
        assertFalse(modules.isModuleVisible(ModuleType.DONATIONS, haveAtLeastOneDonation = false))
    }

    @Test
    fun testIsModuleVisibleForDonationsWhenDisabled() {
        val modules = ActivityTabModules(isDonationsEnabled = false)
        assertFalse(modules.isModuleVisible(ModuleType.DONATIONS, haveAtLeastOneDonation = true))
    }

    @Test
    fun testIsModuleVisibleForGamesRequiresAvailability() {
        val modules = ActivityTabModules()
        assertTrue(modules.isModuleVisible(ModuleType.GAMES, areGamesAvailable = true))
        assertFalse(modules.isModuleVisible(ModuleType.GAMES, areGamesAvailable = false))
    }

    @Test
    fun testIsModuleVisibleForGamesWhenDisabled() {
        val modules = ActivityTabModules(isGamesEnabled = false)
        assertFalse(modules.isModuleVisible(ModuleType.GAMES, areGamesAvailable = true))
    }

    @Test
    fun testIsModuleVisibleNormalModulesAlwaysVisibleIfEnabled() {
        val modules = ActivityTabModules()
        assertTrue(modules.isModuleVisible(ModuleType.TIME_SPENT))
        assertTrue(modules.isModuleVisible(ModuleType.IMPACT))
        assertTrue(modules.isModuleVisible(ModuleType.TIMELINE))
    }

    @Test
    fun testNoModulesVisibleAllEnabled() {
        val modules = ActivityTabModules(isDonationsEnabled = true)
        assertFalse(modules.noModulesVisible(haveAtLeastOneDonation = true, areGamesAvailable = true))
    }

    @Test
    fun testNoModulesVisibleAllDisabled() {
        val modules = ActivityTabModules(
            isTimeSpentEnabled = false,
            isReadingInsightsEnabled = false,
            isEditingInsightsEnabled = false,
            isImpactEnabled = false,
            isGamesEnabled = false,
            isDonationsEnabled = false,
            isTimelineEnabled = false
        )
        assertTrue(modules.noModulesVisible())
    }

    @Test
    fun testNoModulesVisiblePartialDisabled() {
        val modules = ActivityTabModules(
            isTimeSpentEnabled = false,
            isReadingInsightsEnabled = false,
            isEditingInsightsEnabled = false,
            isImpactEnabled = false,
            isGamesEnabled = false,
            isTimelineEnabled = false
        )
        assertTrue(modules.noModulesVisible(areGamesAvailable = false))
    }

    @Test
    fun testAreAllModulesEnabledWhenAllTrue() {
        val modules = ActivityTabModules(isDonationsEnabled = true)
        assertTrue(modules.areAllModulesEnabled())
    }

    @Test
    fun testAreAllModulesEnabledWhenSomeFalse() {
        val modules = ActivityTabModules()
        assertFalse(modules.areAllModulesEnabled())
    }
}
