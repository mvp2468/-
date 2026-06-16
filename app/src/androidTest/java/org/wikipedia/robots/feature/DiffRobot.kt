package org.wikipedia.robots.feature

import BaseRobot
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.wikipedia.R
import org.wikipedia.base.TestConfig

class DiffRobot : BaseRobot() {

    fun clickFirstRevisionInEditHistory() = apply {
        waitForViewDisplayed(withId(R.id.edit_history_recycler), timeoutSeconds = 30)
        delay(TestConfig.DELAY_SHORT)
        list.clickRecyclerViewItemAtPosition(R.id.edit_history_recycler, position = 2)
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun verifyDiffScreenVisible() = apply {
        waitForViewDisplayed(withId(R.id.diffRecyclerView), timeoutSeconds = 30)
        verify.viewExists(R.id.diffRecyclerView)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyArticleTitleVisible() = apply {
        verify.viewExists(R.id.articleTitleView)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyRevisionDetailsVisible() = apply {
        verify.viewExists(R.id.revisionDetailsView)
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickThankButton() = apply {
        try {
            click.onDisplayedViewWithContentDescription("Thank")
        } catch (e: NoMatchingViewException) {
            click.onDisplayedView(R.id.thankButton)
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickWatchButton() = apply {
        try {
            click.onDisplayedViewWithContentDescription("Watch")
        } catch (e: NoMatchingViewException) {
            click.onDisplayedView(R.id.watchButton)
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickUndoButton() = apply {
        try {
            click.onDisplayedViewWithContentDescription("Undo")
        } catch (e: NoMatchingViewException) {
            click.onDisplayedView(R.id.undoButton)
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickOlderRevisionButton() = apply {
        try {
            click.onDisplayedViewWithContentDescription("Previous edit")
        } catch (e: NoMatchingViewException) {
            click.onDisplayedView(R.id.olderIdButton)
        }
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun clickNewerRevisionButton() = apply {
        try {
            click.onDisplayedViewWithContentDescription("Next edit")
        } catch (e: NoMatchingViewException) {
            click.onDisplayedView(R.id.newerIdButton)
        }
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun clickTalkButton() = apply {
        try {
            click.onDisplayedViewWithContentDescription("Talk")
        } catch (e: NoMatchingViewException) {
            click.onDisplayedView(R.id.talkButton)
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifySnackbarWithText(text: String) = apply {
        verify.partialString(text)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyThankSnackbar() = apply {
        try {
            verify.partialString("Thank")
        } catch (e: AssertionError) {
            verify.partialString("感谢")
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun pressBack() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }
}
