package org.wikipedia.tests.watchlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.wikipedia.base.BaseTest
import org.wikipedia.robots.feature.WatchlistRobot
import org.wikipedia.watchlist.WatchlistActivity

/**
 * Tests for the Watchlist feature.
 *
 * Coverage targets:
 * - org.wikipedia.watchlist.WatchlistActivity (22 lines)
 * - org.wikipedia.watchlist.WatchlistFragment
 * - org.wikipedia.watchlist.WatchlistViewModel
 * - org.wikipedia.watchlist.WatchlistFilterActivity (240 lines)
 * - org.wikipedia.watchlist.WatchlistFilterAdapter
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class WatchlistActivityTest : BaseTest<WatchlistActivity>(
    activityClass = WatchlistActivity::class.java
) {
    private val watchlistRobot = WatchlistRobot()

    @Test
    fun testWatchlistActivityLaunches() {
        watchlistRobot
            .verifyWatchlistLoaded()
            .verifyRecyclerViewVisible()
    }

    @Test
    fun testWatchlistEmptyState() {
        watchlistRobot
            .verifyWatchlistLoaded()
            .verifyProgressBarGone()
    }
}
