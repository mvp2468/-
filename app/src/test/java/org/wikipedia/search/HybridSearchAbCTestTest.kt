package org.wikipedia.search

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.analytics.ABTest
import org.wikipedia.settings.Prefs
import org.wikipedia.settings.PrefsIoUtil

@RunWith(RobolectricTestRunner::class)
class HybridSearchAbCTestTest {

    private lateinit var test: HybridSearchAbCTest

    @Before
    fun setUp() {
        test = HybridSearchAbCTest()
    }

    private fun setGroup(abTest: HybridSearchAbCTest, group: Int) {
        PrefsIoUtil.setInt("ab_test_apps_hybridsearch", group)
    }

    @Test
    fun testGetGroupNameControl() {
        setGroup(test, ABTest.GROUP_1)
        assertEquals(HybridSearchAbCTest.GROUP_CONTROL, test.getGroupName())
    }

    @Test
    fun testGetGroupNameLexicalSemantic() {
        setGroup(test, ABTest.GROUP_2)
        assertEquals(HybridSearchAbCTest.GROUP_LEXICAL_SEMANTIC, test.getGroupName())
    }

    @Test
    fun testGetGroupNameSemanticLexical() {
        setGroup(test, ABTest.GROUP_3)
        assertEquals(HybridSearchAbCTest.GROUP_SEMANTIC_LEXICAL, test.getGroupName())
    }

    @Test
    fun testIsTestGroupUserTrueForGroup2() {
        setGroup(test, ABTest.GROUP_2)
        assertTrue(test.isTestGroupUser())
    }

    @Test
    fun testIsTestGroupUserTrueForGroup3() {
        setGroup(test, ABTest.GROUP_3)
        assertTrue(test.isTestGroupUser())
    }

    @Test
    fun testIsTestGroupUserFalseForGroup1() {
        setGroup(test, ABTest.GROUP_1)
        assertFalse(test.isTestGroupUser())
    }

    @Test
    fun testIsHybridSearchEnabledRequiresAllConditions() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchEnabled = true
        assertTrue(test.isHybridSearchEnabled("en"))
        assertTrue(test.isHybridSearchEnabled("pt"))
        assertTrue(test.isHybridSearchEnabled("fr"))
        assertTrue(test.isHybridSearchEnabled("el"))
    }

    @Test
    fun testIsHybridSearchDisabledForUnsupportedLanguage() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchEnabled = true
        assertFalse(test.isHybridSearchEnabled("zh"))
        assertFalse(test.isHybridSearchEnabled("de"))
        assertFalse(test.isHybridSearchEnabled("ja"))
    }

    @Test
    fun testIsHybridSearchDisabledForControlGroup() {
        setGroup(test, ABTest.GROUP_1)
        Prefs.isHybridSearchEnabled = true
        assertFalse(test.isHybridSearchEnabled("en"))
    }

    @Test
    fun testIsHybridSearchDisabledWhenPrefOff() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchEnabled = false
        assertFalse(test.isHybridSearchEnabled("en"))
    }

    @Test
    fun testShouldShowOnboardingRequiresAllConditions() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchOnboardingShown = false
        val result = test.shouldShowOnboarding("en")
        assertNotNull(result)
    }

    @Test
    fun testShouldNotShowOnboardingIfAlreadyShown() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchOnboardingShown = true
        assertFalse(test.shouldShowOnboarding("en"))
    }

    @Test
    fun testShouldNotShowOnboardingForUnsupportedLanguage() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchOnboardingShown = false
        assertFalse(test.shouldShowOnboarding("zh"))
    }

    @Test
    fun testShouldNotShowOnboardingForControlGroup() {
        setGroup(test, ABTest.GROUP_1)
        Prefs.isHybridSearchOnboardingShown = false
        assertFalse(test.shouldShowOnboarding("en"))
    }

    @Test
    fun testCompanionConstants() {
        assertEquals("control", HybridSearchAbCTest.GROUP_CONTROL)
        assertEquals("lexicalsemantic", HybridSearchAbCTest.GROUP_LEXICAL_SEMANTIC)
        assertEquals("semanticlexical", HybridSearchAbCTest.GROUP_SEMANTIC_LEXICAL)
    }

    @Test
    fun testSupportedLanguagesCaseInsensitive() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchEnabled = true
        assertTrue(test.isHybridSearchEnabled("EN"))
        assertTrue(test.isHybridSearchEnabled("Pt"))
        assertTrue(test.isHybridSearchEnabled("FR"))
    }

    @Test
    fun testIsHybridSearchEnabledWithNullLanguage() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchEnabled = true
        assertFalse(test.isHybridSearchEnabled(null))
    }

    @Test
    fun testShouldShowOnboardingWithNullLanguage() {
        setGroup(test, ABTest.GROUP_2)
        Prefs.isHybridSearchOnboardingShown = false
        assertFalse(test.shouldShowOnboarding(null))
    }
}
