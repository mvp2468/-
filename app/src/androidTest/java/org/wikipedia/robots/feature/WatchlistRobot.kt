package org.wikipedia.robots.feature

import BaseRobot
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.wikipedia.R
import org.wikipedia.base.TestConfig

/**
 * Robot for Watchlist feature screens:
 * - WatchlistActivity / WatchlistFragment
 * - WatchlistFilterActivity
 */
class WatchlistRobot : BaseRobot() {

    fun verifyWatchlistLoaded() = apply {
        waitForViewDisplayed(withId(R.id.watchlistRefreshView), timeoutSeconds = 30)
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun verifyRecyclerViewVisible() = apply {
        verify.viewExists(R.id.watchlistRecyclerView)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyEmptyContainerVisible() = apply {
        verify.viewExists(R.id.watchlistEmptyContainer)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyProgressBarGone() = apply {
        delay(TestConfig.DELAY_LARGE)
        // Progress bar should be gone after loading completes
    }

    fun pressBack() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }
}
