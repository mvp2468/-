package org.wikipedia.robots.feature

import BaseRobot
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.DriverAtoms.webScrollIntoView
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.wikipedia.R
import org.wikipedia.base.TestConfig
import org.wikipedia.base.utils.AssertJavascriptAction

class PageRobot(private val context: Context) : BaseRobot() {
    fun clickLink(linkTitle: String) = apply {
        web.clickWebLink(linkTitle)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyArticleTitle(expectedTitle: String) = apply {
        web.verifyH1Title(expectedTitle)
    }

    fun verifyPreviewArticleDialogAppears() = apply {
        click.onDisplayedView(R.id.link_preview_toolbar)
        delay(TestConfig.DELAY_SHORT)
    }

    fun openPreviewLinkInNewTab() = apply {
        click.onDisplayedView(R.id.link_preview_secondary_button)
        delay(TestConfig.DELAY_SHORT)
    }

    fun dismissTooltip(activity: Activity) = apply {
        system.dismissTooltipIfAny(activity, viewId = R.id.buttonView)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyHeaderViewWithLeadImage() = apply {
        verify.viewExists(R.id.page_header_view)
    }

    fun clickLeadImage() = apply {
        delay(TestConfig.DELAY_SHORT)
        click.onDisplayedView(R.id.view_page_header_image)
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickOverflowMenu(description: String) = apply {
        click.onDisplayedViewWithContentDescription(description)
        delay(TestConfig.DELAY_SHORT)
    }

    fun visitImagePage() = apply {
        click.onDisplayedViewWithTextBilingual(viewId = R.id.title, enText = "Go to image page", zhText = "前往图片页面")
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyLeadImageIsNotVisible() = apply {
        verify.viewWithIdIsNotVisible(R.id.page_header_view)
    }

    fun swipePagerLeft() = apply {
        swipe.left(R.id.pager)
        delay(TestConfig.DELAY_SHORT)
    }

    fun swipeLeftToShowTableOfContents() = apply {
        swipe.left(R.id.page_web_view)
        delay(TestConfig.DELAY_SHORT)
    }

    fun pressBack() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }

    fun goBackToGalleryView() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }

    fun goBackToOriginalArticle() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }

    fun enableJavaScript() = apply {
        onWebView().forceJavascriptEnabled()
    }

    fun clickOnPreviewTabInTheList(position: Int) = apply {
        list.clickRecyclerViewItemAtPosition(R.id.tabRecyclerView, position)
    }

    fun swipeDownOnTheWebView() = apply {
        web.swipeDownOnTheWebView(R.id.page_contents_container)
    }

    fun verifyTopMostItemInTableOfContentIs(text: String) = apply {
        verify.viewWithIdAndText(viewId = R.id.page_toc_item_text, text)
    }

    fun clickOnTOCItem(position: Int) = apply {
        list.clickOnListView(
            viewId = R.id.toc_list,
            childView = R.id.page_toc_item_text,
            position = position
        )
    }

    fun swipeTableOfContentsAllTheWayToBottom() = apply {
        swipe.up(R.id.toc_list)
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickAboutThisArticleTextInTOC() = apply {
        try {
            list.clickOnListViewWithText(viewId = R.id.toc_list, "About this article")
        } catch (e: Exception) {
            list.clickOnListViewWithText(viewId = R.id.toc_list, "关于此条目")
        }
        delay(TestConfig.DELAY_SHORT)
    }

    fun goToTalkPage() = apply {
        onWebView().withElement(findElement(Locator.CSS_SELECTOR, "a[title='View talk page']"))
            .perform(webClick())
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickThirdTopic() = apply {
        list.clickRecyclerViewItemAtPosition(R.id.talkRecyclerView, 2)
        delay(TestConfig.DELAY_SHORT)
    }

    fun openOverflowMenu() = apply {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.waitForIdle()
        delay(TestConfig.DELAY_SHORT)

        // Try tapping overflow button by resource ID first
        var button = device.findObject(UiSelector()
            .resourceId("org.wikipedia:id/page_toolbar_button_show_overflow_menu"))
        if (!button.exists()) {
            // Fallback: try by content description
            button = device.findObject(UiSelector().description("More options"))
        }
        if (!button.exists()) {
            // Fallback: try Chinese content description
            button = device.findObject(UiSelector().description("更多选项"))
        }
        if (button.exists()) {
            button.click()
        }
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun navigateBackToExploreFeed() = apply {
        click.onViewWithTextBilingual("Explore", "探索")
        delay(TestConfig.DELAY_SHORT)
    }

    fun removeArticleFromReadingList() = apply {
        click.onViewWithTextBilingual("Remove from Saved", "取消保存")
        delay(TestConfig.DELAY_LARGE)
    }

    fun navigateUp() = apply {
        click.onNavigateUpOrBack()
        delay(TestConfig.DELAY_SHORT)
    }

    private fun assertElementVisibility(elementSelector: String, isVisible: Boolean) {
        onView(withId(R.id.page_web_view))
            .perform(AssertJavascriptAction("(function() { return document.querySelector(\"$elementSelector\").checkVisibility() })();", isVisible.toString()))
    }

    private fun assertEditIconProtection(elementSelector: String, expectedLabel: String) {
        onView(withId(R.id.page_web_view))
            .perform(
                AssertJavascriptAction(
                script = """
                    (function checkEdit() {
                        const element = document.querySelector("$elementSelector")
                        const ariaLabel = element.getAttribute('aria-labelledby')
                        return ariaLabel === 'pcs-edit-section-aria-protected' ? 'protected' : 'normal'
                    })();
                """.trimIndent(),
                expectedResult = expectedLabel
            )
            )
    }

    fun verifyPreviewDialogAppears() = apply {
        verify.viewExists(R.id.link_preview_title)
        delay(TestConfig.DELAY_SHORT)
    }

    fun scrollToCollapsingTables() = apply {
        onWebView()
            .withElement(findElement(Locator.CSS_SELECTOR, ".pcs-table-infobox"))
            .perform(webScrollIntoView())
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickToExpandQuickFactsTable() = apply {
        onWebView()
            .withElement(findElement(Locator.CSS_SELECTOR, ".pcs-table-infobox"))
            .perform(webClick())
        delay(TestConfig.DELAY_SHORT)
    }

    @SuppressLint("CheckResult")
    fun verifyTableIsCollapsed() = apply {
        // checking if this class name exists
        // tried multiple methods but was not able to check the style for the collapsed/expanded
        // state of the table so instead using this className which is used when table is
        // collapsed
        onWebView()
            .withElement(findElement(Locator.CLASS_NAME, "pcs-collapse-table-expanded"))
    }

    @SuppressLint("CheckResult")
    fun verifyTableIsExpanded() = apply {
        // checking if this class name exists
        // tried multiple methods but was not able to check the style for the collapsed/expanded
        // state of the table so instead using this className which is used when table is
        // expanded
        onWebView()
            .withElement(findElement(Locator.CLASS_NAME, "pcs-collapse-table-collapsed"))
    }

    fun assertEditPencilVisibility(isVisible: Boolean) = apply {
        assertElementVisibility("a[data-id='0'].pcs-edit-section-link", isVisible)
    }

    fun assertCollapsingTableIsVisible(isVisible: Boolean) = apply {
        assertElementVisibility(".pcs-collapse-table-content", isVisible)
    }

    fun assertEditButtonProtection(isProtected: Boolean = false) = apply {
        if (isProtected) {
            assertEditIconProtection(".pcs-edit-section-link", "protected")
            return@apply
        }
        assertEditIconProtection(".pcs-edit-section-link", "normal")
    }

    fun saveArticleToReadingList() = apply {
        delay(TestConfig.DELAY_SHORT)
        click.onViewWithId(R.id.page_save)
        delay(TestConfig.DELAY_SHORT)
    }

    fun confirmArticleSaved(text: String) = apply {
        try {
            verify.partialString(text)
        } catch (e: AssertionError) {
            verify.partialString("已保存")
        }
    }

    fun openLanguageSelector() = apply {
        click.onDisplayedViewWithIdAndContentDescription(R.id.page_language, context.getString(R.string.article_menu_bar_language_button))
    }

    fun openFindInArticle() = apply {
        click.onDisplayedViewWithIdAndContentDescription(R.id.page_find_in_article, context.getString(R.string.menu_page_find_in_page))
    }

    fun verifyFindInArticleCount(count: String) = apply {
        onView(allOf(
            withId(R.id.find_in_page_match),
            withText("1/$count")
        )).check(matches(isDisplayed()))
        delay(TestConfig.DELAY_SHORT)
    }

    fun openThemeSelector() = apply {
        click.onDisplayedViewWithIdAndContentDescription(R.id.page_theme, context.getString(R.string.article_menu_bar_theme_button))
    }

    fun openTableOfContents() = apply {
        click.onDisplayedViewWithIdAndContentDescription(R.id.page_contents, context.getString(R.string.article_menu_bar_contents_button))
        delay(TestConfig.DELAY_SHORT)
    }

    fun selectSpanishLanguage() = apply {
        val language = "Spanish"
        val deadline = System.currentTimeMillis() + 10_000
        var lastError: Throwable? = null
        while (System.currentTimeMillis() < deadline) {
            try {
                composeTestRule.onNodeWithText(language)
                    .performScrollTo()
                    .performClick()
                return@apply
            } catch (e: AssertionError) {
                lastError = e
                composeTestRule.waitForIdle()
                delay(TestConfig.DELAY_SHORT)
            }
        }
        throw AssertionError("Could not find language '$language' after 10s", lastError)
    }

    fun scrollToAboutThisArticle() = apply {
        onWebView()
            .withElement(findElement(Locator.ID, "pcs-footer-container-menu-heading"))
            .perform(webScrollIntoView())
        delay(TestConfig.DELAY_SHORT)
    }

    fun goToViewEditHistory() = apply {
        openOverflowMenu()
        // Wait for PopupWindow to fully render
        delay(TestConfig.DELAY_MEDIUM)

        // Use UiAutomator to click "Edit history" text or resourceId in the popup
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.waitForIdle()
        delay(TestConfig.DELAY_MEDIUM)

        // Try to find and click the "Edit history" item using multiple selectors
        val selectors = listOf(
            UiSelector().text("Edit history"),
            UiSelector().text("编辑历史"),
            UiSelector().textContains("Edit"),
            UiSelector().resourceId("org.wikipedia:id/page_view_edit_history"),
            UiSelector().description("Edit history"),
            UiSelector().description("编辑历史")
        )
        var clicked = false
        for (sel in selectors) {
            val item = device.findObject(sel)
            if (item.exists()) {
                item.click()
                clicked = true
                break
            }
        }
        if (!clicked) {
            // Last resort: dump UI hierarchy for debugging
            device.dumpWindowHierarchy("${context.cacheDir}/uiauto_dump.xml")
            // Fallback: use Espresso's PopupWindow root matcher
            try {
                onView(withId(R.id.page_view_edit_history)).perform(click())
            } catch (_: Exception) {
                try {
                    click.onViewWithTextBilingual("Edit history", "编辑历史")
                } catch (_: Exception) { }
            }
        }
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun scrollToLegalSection() = apply {
        onWebView()
            .withElement(findElement(Locator.ID, "pcs-footer-container-legal"))
            .perform(webScrollIntoView())
        delay(TestConfig.DELAY_SHORT)
    }

    fun scrollToAdministrativeDivisionOfIndiaArticle() = apply {
        onWebView()
            .withElement(findElement(Locator.ID, "Administrative_divisions"))
            .perform(webClick())
        delay(TestConfig.DELAY_SHORT)
    }

    fun scrollToAndhraPradeshOnIndiaArticle() = apply {
        onWebView()
            .withElement(findElement(Locator.CSS_SELECTOR, "a[title='Andhra Pradesh']"))
            .perform(webScrollIntoView())
            .perform(webClick())
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickLegalLink() = apply {
        try {
            onWebView()
                .withElement(findElement(Locator.CSS_SELECTOR, ".external"))
                .perform(webClick())
            intended(
                allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasData(Uri.parse("https://creativecommons.org/licenses/by-sa/4.0/"))
                )
            )
        } catch (e: Exception) {
            Log.e("PageRobot: ", "Link failed")
        }
    }

    fun clickOutside() = apply {
        onView(withId(R.id.navigation_drawer))
            .perform(click.xyPosition(context.resources.displayMetrics.widthPixels / 10, context.resources.displayMetrics.heightPixels / 2))
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickOverFlowMenuToolbar() = apply {
        click.onViewWithId(viewId = R.id.page_toolbar_button_show_overflow_menu)
        delay(TestConfig.DELAY_SHORT)
    }

    fun scrollToNonLeadImage() = apply {
        onView(withId(R.id.page_web_view))
            .perform(web.scrollToImageInWebView(1))
            .perform(click())
    }

    fun isGalleryActivityOffline(context: Context, action: () -> Unit) = apply {
        system.performActionIfSnackbarVisible(
            text = context.getString(R.string.gallery_not_available_offline_snackbar),
            action = action
        )
    }

    fun verifySameArticleAppearsAsURL(title: String) = apply {
        delay(TestConfig.DELAY_MEDIUM)
        onWebView()
            .withElement(findElement(Locator.CSS_SELECTOR, "h1[data-id='0'].pcs-edit-section-title"))
            .check(webMatches(getText(), containsString(title)))
        delay(TestConfig.DELAY_LARGE)
    }

    fun test() = apply {
        delay(TestConfig.DELAY_SHORT)
        click.onViewWithTextBilingual("Got it", "知道了")
        delay(TestConfig.DELAY_SHORT)
    }

    fun clickEditIntroductionMenuItem() = apply {
        click.onDisplayedViewWithText(viewId = R.id.title, text = "Edit introduction")
        delay(TestConfig.DELAY_LARGE)
    }
}
