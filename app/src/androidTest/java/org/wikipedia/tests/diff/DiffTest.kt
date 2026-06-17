package org.wikipedia.tests.diff

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.wikipedia.Constants
import org.wikipedia.FakeData
import org.wikipedia.base.BaseTest
import org.wikipedia.base.DataInjector
import org.wikipedia.page.PageActivity
import org.wikipedia.page.PageActivity.Companion.ACTION_LOAD_IN_CURRENT_TAB
import org.wikipedia.page.PageActivity.Companion.EXTRA_HISTORYENTRY
import org.wikipedia.robots.DialogRobot
import org.wikipedia.robots.SystemRobot
import org.wikipedia.robots.feature.DiffRobot
import org.wikipedia.robots.feature.PageRobot

/**
 * Tests for the Article Edit Details / Diff feature.
 *
 * Coverage targets:
 * - org.wikipedia.diff.ArticleEditDetailsActivity
 * - org.wikipedia.diff.ArticleEditDetailsFragment
 * - org.wikipedia.diff.ArticleEditDetailsViewModel
 * - org.wikipedia.diff.DiffUtil
 * - org.wikipedia.diff.DiffLineView
 * - org.wikipedia.diff.EmptyLineSpan
 * - org.wikipedia.diff.UndoEditDialog
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class DiffTest : BaseTest<PageActivity>(
    activityClass = PageActivity::class.java,
    dataInjector = DataInjector(
        intentBuilder = {
            action = ACTION_LOAD_IN_CURRENT_TAB
            putExtra(EXTRA_HISTORYENTRY, FakeData.historyEntry)
            putExtra(Constants.ARG_TITLE, FakeData.historyEntry.title)
        }
    )
) {
    private val systemRobot = SystemRobot()
    private val dialogRobot = DialogRobot()
    private val pageRobot = PageRobot(context)
    private val diffRobot = DiffRobot()

    @Test
    fun testViewEditHistoryFromAboutThisArticle() {
        // Dismiss system permission dialog
        systemRobot
            .clickOnSystemDialogWithText("Allow")

        dialogRobot
            .dismissBigEnglishDialog()
            .dismissContributionDialog()
            .dismissLoggedOutDialog()

        // Scroll to "About this article" footer section and open "View edit history"
        pageRobot
            .scrollToAboutThisArticle()
            .goToViewEditHistory()

        // In the edit history list, click the first revision to open diff details
        diffRobot
            .clickFirstRevisionInEditHistory()

        // Verify we landed on the diff/edit details page
        diffRobot
            .verifyDiffScreenVisible()
            .verifyArticleTitleVisible()
    }

}
