package org.wikipedia.robots.navigation

import BaseRobot
import android.util.Log
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.wikipedia.R
import org.wikipedia.TestUtil.childAtPosition
import org.wikipedia.base.TestConfig

class BottomNavRobot : BaseRobot() {
    fun navigateToExploreFeed() = apply {
        delay(TestConfig.DELAY_MEDIUM)
        onView(withId(R.id.nav_tab_explore)).perform(click())
        delay(TestConfig.DELAY_LARGE)
    }

    fun navigateToSavedPage() = apply {
        delay(TestConfig.DELAY_LARGE)
        onView(withId(R.id.nav_tab_reading_lists)).perform(click())
        delay(TestConfig.DELAY_LARGE)
    }

    fun navigateToSearchPage() = apply {
        delay(TestConfig.DELAY_LARGE)
        onView(withId(R.id.nav_tab_search)).perform(click())
        delay(TestConfig.DELAY_LARGE)
    }

    fun navigateToActivityTab() = apply {
        onView(
            childAtPosition(childAtPosition(withId(R.id.main_nav_tab_layout), 0), 3)
        ).perform(click())
        val isOnboardingActivity = composeTestRule.onNodeWithText("Introducing Activity").isDisplayed()
        if (isOnboardingActivity) {
            pressBack()
        }
        delay(TestConfig.DELAY_LARGE)
    }

    fun navigateToMoreMenu() = apply {
        delay(TestConfig.DELAY_LARGE)
        try {
            onView(withId(R.id.nav_tab_more)).perform(click())
        } catch (_: Exception) {
            // Fallback: use childAtPosition for the 5th tab (index 4)
            onView(
                childAtPosition(childAtPosition(withId(R.id.main_nav_tab_layout), 0), 4)
            ).perform(click())
        }
        delay(TestConfig.DELAY_LARGE)
    }

    fun goToSettings() = apply {
        // Click on `Settings` option
        onView(withId(R.id.main_drawer_settings_container)).perform(click())
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickLoginMenuItem() = apply {
        try {
            click.onViewWithId(R.id.main_drawer_login_button)
            delay(TestConfig.DELAY_SHORT)
        } catch (e: Exception) {
            Log.e("BottomNavRobotError:", "User logged in.")
        }
    }

    fun clickEditsMenuItem() = apply {
        try {
            click.onViewWithId(R.id.main_drawer_edit_container)
            delay(TestConfig.DELAY_SHORT)
        } catch (e: Exception) {
            Log.e("BottomNavRobotError:", "Cannot find edits container.")
        }
    }

    fun gotoWatchList() = apply {
        click.onViewWithId(R.id.main_drawer_watchlist_container)
        delay(TestConfig.DELAY_SHORT)
    }

    fun pressBack() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }
}
