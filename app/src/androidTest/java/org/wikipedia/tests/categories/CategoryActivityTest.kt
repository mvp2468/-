package org.wikipedia.tests.categories

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.wikipedia.Constants
import org.wikipedia.base.BaseTest
import org.wikipedia.base.DataInjector
import org.wikipedia.categories.CategoryActivity
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.PageTitle
import org.wikipedia.robots.feature.CategoriesRobot

/**
 * Tests for the Category list feature.
 *
 * Coverage targets:
 * - org.wikipedia.categories.CategoryActivity (249 lines)
 * - org.wikipedia.categories.CategoryActivityViewModel (52 lines)
 * - org.wikipedia.categories.CategoryDialog (129 lines)
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class CategoryActivityTest : BaseTest<CategoryActivity>(
    activityClass = CategoryActivity::class.java,
    dataInjector = DataInjector(
        intentBuilder = {
            putExtra(Constants.ARG_TITLE, PageTitle(
                "Category:Wikipedia_categories",
                WikiSite.forLanguageCode("en")
            ))
        }
    )
) {
    private val categoriesRobot = CategoriesRobot()

    @Test
    fun testCategoryActivityLaunchesWithTabs() {
        categoriesRobot
            .verifyActivityVisible()
            .verifyToolbarTitleVisible()
            .verifyTabLayoutVisible()
            .verifyArticlesTabSelected()
    }

    @Test
    fun testSwitchToSubcategoriesTab() {
        categoriesRobot
            .verifyActivityVisible()
            .clickSubcategoriesTab()
    }
}
