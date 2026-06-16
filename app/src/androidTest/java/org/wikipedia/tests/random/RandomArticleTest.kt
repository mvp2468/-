package org.wikipedia.tests.random

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.wikipedia.TestConstants.RANDOM_CARD
import org.wikipedia.base.BaseTest
import org.wikipedia.main.MainActivity
import org.wikipedia.robots.DialogRobot
import org.wikipedia.robots.SystemRobot
import org.wikipedia.robots.feature.ExploreFeedRobot
import org.wikipedia.robots.feature.RandomArticleRobot
import org.wikipedia.robots.screen.HomeScreenRobot

/**
 * Tests for the Random Article feature.
 *
 * Coverage targets:
 * - org.wikipedia.random.RandomActivity
 * - org.wikipedia.random.RandomFragment
 * - org.wikipedia.random.RandomItemFragment
 * - org.wikipedia.random.RandomItemViewModel
 * - org.wikipedia.random.RandomViewModel
 * - org.wikipedia.random.PagerTransformer
 * - org.wikipedia.random.BottomViewBehavior
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class RandomArticleTest : BaseTest<MainActivity>(
    activityClass = MainActivity::class.java
) {
    private val systemRobot = SystemRobot()
    private val homeScreenRobot = HomeScreenRobot()
    private val dialogRobot = DialogRobot()
    private val exploreFeedRobot = ExploreFeedRobot()
    private val randomArticleRobot = RandomArticleRobot()

    @Test
    fun testRandomArticleFromExploreFeed() {
        waitForMainActivityReady()

        // Dismiss any system dialogs and onboarding
        systemRobot
            .clickOnSystemDialogWithText("Allow")
            .disableDarkMode(context)

        homeScreenRobot
            .dismissFeedCustomization()

        dialogRobot
            .dismissSurveyDialog()
            .dismissContributionDialog()
            .dismissLoggedOutDialog()

        // Navigate to Random Article from the Explore Feed
        exploreFeedRobot
            .scrollAndPerform(title = RANDOM_CARD) { position ->
                clickRandomArticle(position)
            }

        // Wait for the random article card to be visible
        randomArticleRobot
            .verifyArticleCardVisible()

        // Navigate to another random article
        randomArticleRobot
            .clickNextButton()
            .verifyArticleCardVisible()

        // Navigate one more time
        randomArticleRobot
            .clickNextButton()
            .verifyArticleCardVisible()

        // Go back to previous article
        randomArticleRobot
            .clickBackButton()
            .verifyArticleCardVisible()

        // Click on the article card to open the full article
        randomArticleRobot
            .clickArticleCard()

        // Dismiss any dialogs that may appear on the article page
        dialogRobot
            .dismissBigEnglishDialog()
            .dismissContributionDialog()
    }
}
