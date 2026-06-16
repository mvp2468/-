package org.wikipedia.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.wikipedia.R
import org.wikipedia.TestLogRule
import org.wikipedia.TestUtil
import org.wikipedia.WikipediaApp
import org.wikipedia.settings.Prefs
import org.wikipedia.settings.PrefsIoUtil
import java.util.Locale
import java.util.concurrent.TimeUnit

object TestConfig {
    const val DELAY_SHORT = 1L
    const val DELAY_MEDIUM = 2L
    const val DELAY_LARGE = 4L
    const val DELAY_SWIPE_TO_REFRESH = 6L
    const val SEARCH_TERM = "hopf fibration"
    const val SEARCH_TERM2 = "world cup"
    const val ARTICLE_TITLE = "Hopf fibration"
    const val ARTICLE_TITLE_ESPANOL = "Fibración de Hopf"
    const val TEST_WIKI_URL_APPLE = "https://en.wikipedia.org/wiki/Apple"
    const val ARTICLE_TITLE_WORLD_CUP = "World cup"
}

data class DataInjector(
    val isInitialOnboardingEnabled: Boolean = false,
    val overrideEditsContribution: Int? = null,
    val intentBuilder: (Intent.() -> Unit)? = null,
    val showOneTimeCustomizeToolbarTooltip: Boolean = false,
    val readingListShareTooltipShown: Boolean = true,
    val otdEntryDialogShown: Boolean = true,
    val enableYearInReview: Boolean = false,
    val showReadingListSyncEnablePrompt: Boolean = false,
    val isSuggestedEditsHighestPriorityEnabled: Boolean = true,
)

abstract class BaseTest<T : AppCompatActivity>(
    activityClass: Class<T>,
    dataInjector: DataInjector = DataInjector()
) {
    @get:Rule
    val testLogRule = TestLogRule()

    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<T>

    @get:Rule
    var composeTestRule = createComposeRule()

    /**
     * A TestRule that wraps [GrantPermissionRule] but silently catches
     * [SecurityException] on devices where the shell user lacks
     * [android.permission.GRANT_RUNTIME_PERMISSIONS] (e.g., OPPO ColorOS, Xiaomi HyperOS).
     */
    class SafeGrantPermissionRule : TestRule {
        private val delegate: GrantPermissionRule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            GrantPermissionRule.grant()
        }

        override fun apply(base: Statement, description: Description): Statement {
            return try {
                delegate.apply(base, description)
            } catch (_: SecurityException) {
                // Permission grant not supported on this device; proceed without it.
                base
            }
        }
    }

    @get:Rule
    val permissionRule = SafeGrantPermissionRule()

    protected lateinit var activity: T
    protected lateinit var device: UiDevice
    protected var context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    companion object {
        private var animationsDisabled = false

        private fun disableAnimationsOnce() {
            if (animationsDisabled) return
            synchronized(this) {
                if (animationsDisabled) return
                try {
                    val instrumentation = InstrumentationRegistry.getInstrumentation()
                    instrumentation.uiAutomation.executeShellCommand("settings put global window_animation_scale 0")
                    instrumentation.uiAutomation.executeShellCommand("settings put global transition_animation_scale 0")
                    instrumentation.uiAutomation.executeShellCommand("settings put global animator_duration_scale 0")
                } catch (_: Exception) { }
                animationsDisabled = true
            }
        }
    }

    init {
        val intent = Intent(context, activityClass)
        activityScenarioRule = ActivityScenarioRule(intent)
        Prefs.apply {
            isInitialOnboardingEnabled = dataInjector.isInitialOnboardingEnabled
            showOneTimeCustomizeToolbarTooltip = dataInjector.showOneTimeCustomizeToolbarTooltip
            readingListShareTooltipShown = dataInjector.readingListShareTooltipShown
            otdEntryDialogShown = dataInjector.otdEntryDialogShown
            isYearInReviewEnabled = dataInjector.enableYearInReview
            showReadingListSyncEnablePrompt = dataInjector.showReadingListSyncEnablePrompt
            isSuggestedEditsHighestPriorityEnabled = dataInjector.isSuggestedEditsHighestPriorityEnabled
        }
        dataInjector.overrideEditsContribution?.let {
            Prefs.overrideSuggestedEditContribution = it
        }
        // Suppress all startup popups & dialogs that block the UI.
        // Must be set here (in init), not in @Before, because @Rule ActivityScenarioRule
        // launches the Activity BEFORE @Before runs.
        Prefs.announcementPauseTime = System.currentTimeMillis()
        Prefs.hasVisitedArticlePage = false
        Prefs.readingChallengeOnboardingShown = true
        Prefs.readingChallengeInstallPromptShown = true
        Prefs.loggedOutInBackground = false
        Prefs.yearInReviewVisited = true
        Prefs.showSearchTabTooltip = false
        Prefs.isActivityTabOnboardingShown = true
        Prefs.isRecommendedReadingListOnboardingShown = true
        PrefsIoUtil.setBoolean(R.string.preference_key_feed_customize_onboarding_card_enabled, false)
        // Disable donation reminders to prevent DonationReminderCardView from blocking UI
        Prefs.donationReminderConfig = Prefs.donationReminderConfig.copy(userEnabled = false)
        dataInjector.intentBuilder?.let {
            val newIntent = Intent(context, activityClass).apply(it)
            activityScenarioRule = ActivityScenarioRule(newIntent)
        }
    }

    @Before
    open fun setup() {
        Intents.init()
        ComposeTestManager.setComposeTestRule(composeTestRule)
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        IdlingPolicies.setMasterPolicyTimeout(20, TimeUnit.SECONDS)
        activityScenarioRule.scenario.onActivity {
            activity = it
        }
        WikipediaApp.instance.languageState.let {
            it.removeAppLanguageCodes(it.appLanguageCodes.filter { it != "en" })
        }
        // Set Java-level default locale to English for consistent Date/Number formatting.
        // NOTE: Do NOT change Android system_locales via shell command here — it triggers
        // a configuration change that kills the Activity lifecycle mid-test.
        Locale.setDefault(Locale.ENGLISH)
        // Disable animations — run once per test runner session to avoid
        // triggering configuration changes mid-test on Android 14+
        disableAnimationsOnce()
    }

    /**
     * Wait for the main activity to finish initialization by checking that the
     * bottom navigation bar is displayed. This ensures the app has completed:
     * - Splash screen / system splash (Android 12+)
     * - Welcome/onboarding flow (if any)
     * - Database initialization
     * - Network feed loading
     * - Bottom navigation layout inflating
     *
     * When Test Orchestrator is disabled, the Activity may persist on a sub-page
     * (e.g., article TOC) from a previous test. This method presses back to return
     * to the home screen in that case.
     *
     * Call this at the start of any test that needs to interact with the bottom nav.
     */
    protected fun waitForMainActivityReady() {
        // Dismiss any TOC overlay that may persist from a previous test
        // when Orchestrator is disabled. The TOC (SwipeableListView) is a
        // full-screen overlay that hides main_nav_tab_layout and all other
        // views, causing NoMatchingViewException for many tests.
        try {
            onView(withId(R.id.toc_list)).check(matches(isDisplayed()))
            Espresso.pressBack()
            TestUtil.delay(TestConfig.DELAY_MEDIUM)
        } catch (_: Throwable) { }
        try {
            // Check if bottom nav is already visible (Activity freshly launched)
            onView(withId(R.id.main_nav_tab_layout)).check(matches(isDisplayed()))
        } catch (_: Throwable) {
            // Activity is on a sub-page from a previous test when Orchestrator
            // is disabled (NoMatchingViewException if view absent, AssertionError
            // if view exists but not displayed). Dismiss keyboard, press back.
            try { Espresso.closeSoftKeyboard() } catch (_: Exception) { }
            try {
                for (i in 1..4) {
                    Espresso.pressBack()
                    TestUtil.delay(TestConfig.DELAY_SHORT)
                    try {
                        onView(withId(R.id.main_nav_tab_layout)).check(matches(isDisplayed()))
                        break
                    } catch (_: Throwable) { }
                }
            } catch (_: Exception) { }
        }
        // Standard wait with Espresso's retry mechanism
        onView(withId(R.id.main_nav_tab_layout)).check(matches(isDisplayed()))
        TestUtil.delay(TestConfig.DELAY_SHORT)
    }

    protected fun setDeviceOrientation(isLandscape: Boolean) {
        if (isLandscape) device.setOrientationRight() else device.setOrientationNatural()
        Thread.sleep(TestConfig.DELAY_MEDIUM)
    }

    fun isOnline(): Boolean {
        return WikipediaApp.instance.isOnline
    }

    /**
     * Clean up after each test by returning the app to a known state:
     * [MainActivity] with the bottom navigation bar visible. When Test
     * Orchestrator is disabled, the next test inherits whatever Activity
     * stack the previous test left behind — including [PageActivity] with
     * the TOC side-panel drawer open, which is a full-screen overlay that
     * makes views like [R.id.main_nav_tab_layout] unreachable.
     */
    @After
    open fun tearDown() {
        // Dismiss soft keyboard to avoid it intercepting the back press.
        try { Espresso.closeSoftKeyboard() } catch (_: Exception) { }
        // Dismiss the TOC side-panel if it is covering the current activity.
        try {
            onView(withId(R.id.toc_list)).check(matches(isDisplayed()))
            Espresso.pressBack()
            TestUtil.delay(TestConfig.DELAY_MEDIUM)
        } catch (_: Throwable) { }
        // Press back repeatedly until we reach the home screen (MainActivity
        // bottom nav is visible) or exhaust attempts. This handles multi-layer
        // back stacks (e.g. PageActivity → article → TOC) that a single press
        // cannot unwind.
        try {
            for (i in 1..6) {
                try {
                    onView(withId(R.id.main_nav_tab_layout)).check(matches(isDisplayed()))
                    break
                } catch (_: Throwable) {
                    Espresso.pressBack()
                    TestUtil.delay(TestConfig.DELAY_SHORT)
                }
            }
        } catch (_: Exception) { }
        Intents.release()
    }
}
