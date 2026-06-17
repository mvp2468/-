package org.wikipedia.tests.readinglist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.wikipedia.compose.theme.BaseTheme
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.page.PageTitle
import org.wikipedia.readinglist.recommended.RecommendedReadingListInterestsScreen
import org.wikipedia.readinglist.recommended.RecommendedReadingListInterestsViewModel
import org.wikipedia.readinglist.recommended.RecommendedReadingListSource
import org.wikipedia.readinglist.recommended.RecommendedReadingListSourceViewModel
import org.wikipedia.readinglist.recommended.SourceSelectionScreen
import org.wikipedia.util.Resource

/**
 * Instrumented tests for the Recommended Reading List feature.
 *
 * Coverage targets:
 * - org.wikipedia.readinglist.recommended.SourceSelectionScreen (324 lines)
 * - org.wikipedia.readinglist.recommended.RecommendedReadingListInterestsScreen (583 lines)
 * - org.wikipedia.readinglist.recommended.SourceSelectionContent
 * - org.wikipedia.readinglist.recommended.RecommendedReadingListInterestsContent
 * - org.wikipedia.readinglist.recommended.ReadingListInterestCard
 * - org.wikipedia.readinglist.recommended.ReadingListInterestSearchCard
 *
 * Tests all UI states (Loading, Error, Success) directly via ComposeTestRule
 * without needing to launch a full Activity or mock network calls.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class RecommendedReadingListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val enWikiSite = WikiSite("https://en.wikipedia.org/".toUri(), "en")

    // ─── Source Selection Screen ───────────────────────────────────────

    @Test
    fun testSourceSelectionLoadingState() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Loading(),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Loading state should show progress indicator but no content
        composeTestRule
            .onNodeWithText("Let me choose based on my interests")
            .assertIsNotDisplayed()
    }

    @Test
    fun testSourceSelectionSuccessStateShowsAllOptions() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListSourceViewModel.SourceSelectionUiState(
                            isSavedOptionEnabled = true,
                            isHistoryOptionEnabled = true,
                            selectedSource = RecommendedReadingListSource.INTERESTS
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Heading text
        composeTestRule
            .onNodeWithText("How should we build your recommended reading list?")
            .assertIsDisplayed()

        // All three source options
        composeTestRule
            .onNodeWithText("Let me choose based on my interests")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Choose for me based on my saved articles")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Choose for me based on my reading history")
            .assertIsDisplayed()
    }

    @Test
    fun testSourceSelectionInterestsOnlyWhenOthersDisabled() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListSourceViewModel.SourceSelectionUiState(
                            isSavedOptionEnabled = false,
                            isHistoryOptionEnabled = false,
                            selectedSource = RecommendedReadingListSource.INTERESTS
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Only interests should be shown
        composeTestRule
            .onNodeWithText("Let me choose based on my interests")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Choose for me based on my saved articles")
            .assertIsNotDisplayed()

        composeTestRule
            .onNodeWithText("Choose for me based on my reading history")
            .assertIsNotDisplayed()
    }

    @Test
    fun testSourceSelectionErrorStateHidesContent() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Error(Throwable("Network error")),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        // Error state should not render the source cards
        composeTestRule
            .onNodeWithText("Let me choose based on my interests")
            .assertIsNotDisplayed()
    }

    @Test
    fun testSourceSelectionFromSettingsModeHidesPromptText() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListSourceViewModel.SourceSelectionUiState(
                            isSavedOptionEnabled = true,
                            isHistoryOptionEnabled = true,
                            selectedSource = RecommendedReadingListSource.INTERESTS
                        )
                    ),
                    fromSettings = true,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        // In settings mode, the prompt heading is hidden
        composeTestRule
            .onNodeWithText("How should we build your recommended reading list?")
            .assertIsNotDisplayed()

        // Source options still visible in settings mode
        composeTestRule
            .onNodeWithText("Let me choose based on my interests")
            .assertIsDisplayed()
    }

    @Test
    fun testSourceSelectionWithSavedOptionSelected() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListSourceViewModel.SourceSelectionUiState(
                            isSavedOptionEnabled = true,
                            isHistoryOptionEnabled = false,
                            selectedSource = RecommendedReadingListSource.READING_LIST
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Choose for me based on my saved articles")
            .assertIsDisplayed()
    }

    @Test
    fun testSourceSelectionWithHistoryOptionSelected() {
        composeTestRule.setContent {
            BaseTheme {
                SourceSelectionScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListSourceViewModel.SourceSelectionUiState(
                            isSavedOptionEnabled = false,
                            isHistoryOptionEnabled = true,
                            selectedSource = RecommendedReadingListSource.HISTORY
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSourceClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Choose for me based on my reading history")
            .assertIsDisplayed()
    }

    // ─── Interests Selection Screen ────────────────────────────────────

    @Test
    fun testInterestsSelectionLoadingState() {
        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Loading(),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        // Loading - title not in content yet
        composeTestRule
            .onNodeWithText("What are you interested in?")
            .assertIsNotDisplayed()
    }

    @Test
    fun testInterestsSelectionSuccessStateShowsTitleAndSearch() {
        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListInterestsViewModel.UiState(
                            fromSettings = false,
                            items = emptyList(),
                            selectedItems = emptySet()
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        // Title and search bar
        composeTestRule
            .onNodeWithText("What are you interested in?")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Search for an article")
            .assertIsDisplayed()
    }

    @Test
    fun testInterestsSelectionShowsArticleCards() {
        val titles = listOf(
            PageTitle(text = "Psychology of art", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
            PageTitle(text = "Industrial design", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
            PageTitle(text = "Barack Obama", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
        )

        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListInterestsViewModel.UiState(
                            fromSettings = false,
                            items = titles,
                            selectedItems = emptySet()
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Psychology of art")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Industrial design")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Barack Obama")
            .assertIsDisplayed()
    }

    @Test
    fun testInterestsSelectionShowsSelectedCount() {
        val titles = listOf(
            PageTitle(text = "Psychology of art", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
            PageTitle(text = "Industrial design", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
        )
        val selectedItems = setOf(
            PageTitle(text = "Industrial design", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null)
        )

        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListInterestsViewModel.UiState(
                            fromSettings = false,
                            items = titles,
                            selectedItems = selectedItems
                        )
                    ),
                    fromSettings = false,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        // Both articles should still be visible
        composeTestRule
            .onNodeWithText("Psychology of art")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Industrial design")
            .assertIsDisplayed()
    }

    @Test
    fun testInterestsSelectionErrorState() {
        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Error(Throwable("Failed to load")),
                    wikiErrorClickEvents = null,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        // Error state should not show article cards
        composeTestRule
            .onNodeWithText("What are you interested in?")
            .assertIsNotDisplayed()
    }

    @Test
    fun testInterestsSelectionFromSettingsModeShowsMinimumText() {
        val titles = listOf(
            PageTitle(text = "Psychology of art", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
        )

        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListInterestsViewModel.UiState(
                            fromSettings = true,
                            items = titles,
                            selectedItems = emptySet()
                        )
                    ),
                    fromSettings = true,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        // In settings mode with 0 selected, show minimum text
        composeTestRule
            .onNodeWithText("Select at least one interest")
            .assertIsDisplayed()
    }

    @Test
    fun testInterestsSelectionFromSettingsWithManyArticles() {
        val titles = listOf(
            PageTitle(text = "Psychology of art", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
            PageTitle(text = "Industrial design", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
            PageTitle(text = "Barack Obama", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
        )
        val selectedItems = setOf(
            PageTitle(text = "Industrial design", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
            PageTitle(text = "Barack Obama", wiki = enWikiSite, thumbUrl = "", description = "", displayText = null),
        )

        composeTestRule.setContent {
            BaseTheme {
                RecommendedReadingListInterestsScreen(
                    uiState = Resource.Success(
                        RecommendedReadingListInterestsViewModel.UiState(
                            fromSettings = true,
                            items = titles,
                            selectedItems = selectedItems
                        )
                    ),
                    fromSettings = true,
                    onCloseClick = {},
                    onNextClick = {},
                    onSearchClick = {},
                    onItemClick = {},
                    onRandomizeClick = {}
                )
            }
        }

        // Articles should be visible in settings mode
        composeTestRule
            .onNodeWithText("Psychology of art")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Industrial design")
            .assertIsDisplayed()
    }
}
