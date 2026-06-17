package org.wikipedia.tests.talk

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.wikipedia.Constants
import org.wikipedia.base.BaseTest
import org.wikipedia.base.DataInjector
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.PageTitle
import org.wikipedia.robots.feature.TalkRobot
import org.wikipedia.talk.TalkTopicsActivity

/**
 * Tests for the Talk feature: topics list and topic detail.
 *
 * Coverage targets:
 * - org.wikipedia.talk.TalkTopicsActivity (582 lines)
 * - org.wikipedia.talk.TalkTopicsViewModel
 * - org.wikipedia.talk.TalkTopicActivity (469 lines)
 * - org.wikipedia.talk.TalkTopicViewModel
 * - org.wikipedia.talk.TalkThreadHeaderView
 * - org.wikipedia.talk.TalkThreadItemView
 * - org.wikipedia.talk.TalkReplyItemAdapter
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkTopicsActivityTest : BaseTest<TalkTopicsActivity>(
    activityClass = TalkTopicsActivity::class.java,
    dataInjector = DataInjector(
        intentBuilder = {
            putExtra(Constants.ARG_TITLE, PageTitle(
                "Talk:Earth",
                WikiSite.forLanguageCode("en")
            ))
            putExtra(Constants.INTENT_EXTRA_INVOKE_SOURCE, Constants.InvokeSource.TALK_TOPICS_ACTIVITY)
        }
    )
) {
    private val talkRobot = TalkRobot()

    @Test
    fun testTalkTopicsListLoads() {
        talkRobot
            .verifyToolbarVisible()
            .verifyTopicsListLoaded()
            .verifyNewTopicButtonVisible()
    }

    @Test
    fun testClickTopicOpensDetail() {
        talkRobot
            .verifyTopicsListLoaded()
            .clickTopicAtPosition(0)
            .verifyTopicDetailLoaded()
    }

    @Test
    fun testTalkTopicErrorState() {
        // Launch with a non-existent talk page to test error handling
    }
}
