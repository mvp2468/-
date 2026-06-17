package org.wikipedia.robots.feature

import BaseRobot
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.wikipedia.R
import org.wikipedia.base.TestConfig

/**
 * Robot for Talk feature screens:
 * - TalkTopicsActivity (topics list)
 * - TalkTopicActivity (single topic detail)
 */
class TalkRobot : BaseRobot() {

    // === TalkTopicsActivity (topics list) ===

    fun verifyTopicsListLoaded() = apply {
        waitForViewDisplayed(withId(R.id.talkRecyclerView), timeoutSeconds = 30)
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun verifyToolbarVisible() = apply {
        verify.viewExists(R.id.toolbar)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyNewTopicButtonVisible() = apply {
        verify.viewExists(R.id.talkNewTopicButton)
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickTopicAtPosition(position: Int) = apply {
        list.clickRecyclerViewItemAtPosition(R.id.talkRecyclerView, position)
        delay(TestConfig.DELAY_MEDIUM)
    }

    // === TalkTopicActivity (topic detail) ===

    fun verifyTopicDetailLoaded() = apply {
        waitForViewDisplayed(withId(R.id.talkRecyclerView), timeoutSeconds = 30)
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun pressBack() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }
}
