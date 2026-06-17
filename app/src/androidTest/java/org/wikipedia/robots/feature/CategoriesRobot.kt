package org.wikipedia.robots.feature

import BaseRobot
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.wikipedia.R
import org.wikipedia.base.TestConfig

class CategoriesRobot : BaseRobot() {

    fun verifyActivityVisible() = apply {
        waitForViewDisplayed(withId(R.id.category_recycler), timeoutSeconds = 30)
        verify.viewExists(R.id.category_recycler)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyToolbarTitleVisible() = apply {
        verify.viewExists(R.id.toolbar_title)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyTabLayoutVisible() = apply {
        verify.viewExists(R.id.category_tab_layout)
        delay(TestConfig.DELAY_SHORT)
    }

    fun verifyArticlesTabSelected() = apply {
        waitForViewDisplayed(withId(R.id.category_recycler), timeoutSeconds = 30)
        delay(TestConfig.DELAY_MEDIUM)
    }

    fun clickSubcategoriesTab() = apply {
        click.onViewWithText("Subcategories")
        delay(TestConfig.DELAY_MEDIUM)
        verify.viewExists(R.id.category_recycler)
    }

    fun pressBack() = apply {
        goBack()
        delay(TestConfig.DELAY_SHORT)
    }
}
