package org.wikipedia.robots.feature

import BaseRobot
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.wikipedia.R
import org.wikipedia.TestUtil
import org.wikipedia.base.TestConfig

class ReadingListRobot : BaseRobot() {

    fun clickOnReadingLists(position: Int) = apply {
        list.clickOnItemInList(
            listId = R.id.recycler_view,
            position
        )
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickOnReadingLists(title: String) = apply {
        // Pre-created "Saved" list may have a localized name in the database
        // (e.g. "已保存的条目" on Chinese devices), so use position-based click.
        if (title == READING_LIST_SAVED_EN) {
            clickOnReadingLists(0)
        } else {
            list.scrollToRecyclerView(
                recyclerViewId = R.id.recycler_view,
                title = title,
                textViewId = R.id.item_title,
                action = {
                    click.onDisplayedViewWithText(
                        viewId = R.id.item_title,
                        text = title
                    )
                }
            )
            delay(TestConfig.DELAY_SHORT)
        }
    }

    companion object {
        private const val READING_LIST_SAVED_EN = "Saved"
    }

    fun clickOnReadingListItem(position: Int) = apply {
        list.clickOnItemInList(
            listId = R.id.reading_list_recycler_view,
            position = position
        )
        // Wait for the article page to fully load (consistent with SearchRobot.clickOnItemFromSearchList).
        delay(TestConfig.DELAY_LARGE)
    }

    /**
     * Dismiss the article [Table of Contents](app:id/toc_list) overlay if visible,
     * then reveal the toolbar. When Orchestrator is disabled, the TOC can persist
     * from a previous test, and the article page may load scrolled to a saved
     * position with a collapsed AppBar — both of which hide [R.id.page_save].
     */
    fun dismissTocIfVisible() = apply {
        try {
            onView(withId(R.id.toc_list)).check(matches(isDisplayed()))
            goBack()
        } catch (_: Exception) { }
        delay(TestConfig.DELAY_LARGE)
        // Expanding a collapsed AppBar requires scrolling the WebView content
        // downward to trigger the AppBarLayout behavior. Swipe on root to ensure
        // the gesture reaches the WebView regardless of its container ID.
        try {
            repeat(3) {
                onView(isRoot()).perform(TestUtil.swipeDownWebView())
                delay(TestConfig.DELAY_MEDIUM)
            }
        } catch (_: Exception) { }
        delay(TestConfig.DELAY_SHORT)
    }

    fun longClickReadingLists(position: Int) = apply {
        list.longClickOnItemInList(
            listId = R.id.recycler_view,
            position = position
        )
    }

    fun deleteList(context: Context) {
        click.onViewWithText(context.getString(R.string.reading_list_menu_delete))
    }

    fun removeArticleList(listName: String) = apply {
        click.onViewWithText("Remove from $listName")
        delay(TestConfig.DELAY_LARGE)
    }

    fun saveArticleToReadingList() = apply {
        delay(TestConfig.DELAY_SHORT)
        for (attempt in 1..2) {
            try {
                click.onViewWithId(R.id.page_save)
                break
            } catch (_: Exception) {
                // The toolbar may still be collapsed (AppBar hidden) despite
                // dismissTocIfVisible(). Swipe again and retry once.
                try {
                    onView(isRoot()).perform(TestUtil.swipeDownWebView())
                    delay(TestConfig.DELAY_LARGE)
                } catch (_: Exception) { }
                if (attempt >= 2) {
                    click.onViewWithId(R.id.page_save)
                }
            }
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun addToReadingList(context: Context) = apply {
        click.onViewWithText(context.getString(R.string.reading_list_add_to_list_button))
        delay(TestConfig.DELAY_SHORT)
    }

    fun typeNameOfTheList(title: String, context: Context) = apply {
        input.replaceTextInView(viewId = R.id.text_input, title)
        delay(TestConfig.DELAY_SHORT)
        if (verify.isViewWithTextVisible(context.getString(R.string.reading_list_title_exists, title))) {
            input.replaceTextInView(viewId = R.id.text_input, "$title${Math.random()}")
        }
    }

    fun saveTheList(context: Context) = apply {
        click.onViewWithText(context.getString(R.string.text_input_dialog_ok_button_text))
        delay(TestConfig.DELAY_SHORT)
    }

    fun viewTheList(context: Context) = apply {
        click.onViewWithText(context.getString(R.string.reading_list_added_view_button))
        delay(TestConfig.DELAY_SHORT)
    }

    fun dismissTooltip(activity: Activity) = apply {
        system.dismissTooltipIfAny(activity, viewId = R.id.buttonView)
    }

    fun clickOnGotIt() = apply {
        try {
            click.onViewWithTextBilingual("Got it", "知道了")
            delay(TestConfig.DELAY_SHORT)
        } catch (e: Exception) {
            Log.e("ReadingListRobot:", "Text does not exist.")
        }
    }

    fun verifySavedArticleExists(title: String) = apply {
        verify.viewWithTextDisplayed(title)
    }

    fun verifySavedArticleDoesNotExists(title: String) = apply {
        verify.viewWithTextDoesNotExist(title)
    }

    fun verifyListDoesNotExist(title: String) = apply {
        verify.viewWithTextDoesNotExist(title)
    }

    fun verifyArticleHasNotDownloaded() = apply {
        delay(TestConfig.DELAY_SHORT)
        onView(withId(R.id.reading_list_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(
                matches(
                    atPosition(
                        1,
                        hasDescendant(
                            allOf(
                                withId(R.id.page_list_item_action),
                                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                            )
                        )
                    )
                )
            )
    }

    fun verifyArticleHasDownloaded() = apply {
        delay(TestConfig.DELAY_SHORT)
        onView(withId(R.id.reading_list_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(
                matches(
                    atPosition(
                        1,
                        hasDescendant(
                            allOf(
                                withId(R.id.page_list_item_action),
                                withEffectiveVisibility(ViewMatchers.Visibility.GONE)
                            )
                        )
                    )
                )
            )
    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(item: RecyclerView): Boolean {
                val viewHolder = item.findViewHolderForAdapterPosition(position) ?: return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun navigateUp() = apply {
        click.onNavigateUpOrBack()
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickNoThanks(context: Context) = apply {
        try {
            click.onViewWithText(context.getString(R.string.reading_list_prompt_turned_sync_on_dialog_no_thanks))
            delay(TestConfig.DELAY_SHORT)
        } catch (e: Exception) {
            Log.e("ReadingListRobot: ", "${e.message}")
        }
    }

    fun clickCreateList() = apply {
        click.onViewWithId(R.id.create_button)
        delay(TestConfig.DELAY_SHORT)
    }

    fun pressBack() = apply {
        delay(TestConfig.DELAY_SHORT)
        goBack()
    }
}
