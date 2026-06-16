import android.util.Log
import android.view.View
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import org.wikipedia.R
import org.wikipedia.TestUtil.waitOnId
import org.wikipedia.base.ComposeTestManager
import org.wikipedia.base.actions.ClickActions
import org.wikipedia.base.actions.InputActions
import org.wikipedia.base.actions.ListActions
import org.wikipedia.base.actions.ScrollActions
import org.wikipedia.base.actions.SwipeActions
import org.wikipedia.base.actions.SystemActions
import org.wikipedia.base.actions.VerificationActions
import org.wikipedia.base.actions.WebActions
import org.wikipedia.base.TestConfig
import java.util.concurrent.TimeUnit

abstract class BaseRobot {
    protected val composeTestRule: ComposeTestRule
        get() = ComposeTestManager.getComposeTestRule()

    protected val click = ClickActions()
    protected val input = InputActions()
    protected val list = ListActions()
    protected val scroll = ScrollActions()
    protected val swipe = SwipeActions()
    protected val system = SystemActions()
    protected val verify = VerificationActions()
    protected val web = WebActions()

    protected fun delay(seconds: Long) {
        onView(isRoot()).perform(waitOnId(TimeUnit.SECONDS.toMillis(seconds)))
    }

    protected fun goBack() {
        pressBack()
    }

    /**
     * Safely return to the home screen by pressing back until the bottom navigation
     * bar ([R.id.main_nav_tab_layout]) is visible. Unlike a fixed number of back presses,
     * this checks after each press and stops early, avoiding accidental app exit (闪退)
     * when the navigation stack is shallower than expected.
     */
    fun returnToHome() = apply {
        for (i in 1..5) {
            try {
                onView(withId(R.id.main_nav_tab_layout)).check(matches(isDisplayed()))
                delay(TestConfig.DELAY_SHORT)
                return@apply
            } catch (_: Exception) {
                goBack()
                delay(TestConfig.DELAY_SHORT)
            }
        }
        // Final safety net
        onView(withId(R.id.main_nav_tab_layout)).check(matches(isDisplayed()))
    }

    /**
     * Wait for a view to be displayed using Espresso's built-in retry mechanism.
     * This is more reliable than fixed-time delays because it actively polls for
     * the view to appear, which handles slow app initialization, splash screens,
     * and network/database loading on slower devices.
     *
     * @param matcher The view matcher to wait for.
     * @param timeoutSeconds Maximum time to wait before giving up (default 60s).
     * @throws AssertionError if the view is not displayed within the timeout.
     */
    protected fun waitForViewDisplayed(matcher: Matcher<View>, timeoutSeconds: Long = 60) {
        val deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutSeconds)
        var lastException: NoMatchingViewException? = null
        while (System.currentTimeMillis() < deadline) {
            try {
                onView(matcher).check(matches(isDisplayed()))
                return // View found and displayed
            } catch (e: NoMatchingViewException) {
                lastException = e
                // Let the main thread process events before retrying
                onView(isRoot()).perform(waitOnId(500))
            }
        }
        throw AssertionError("View not displayed after ${timeoutSeconds}s: $matcher", lastException)
    }
}
