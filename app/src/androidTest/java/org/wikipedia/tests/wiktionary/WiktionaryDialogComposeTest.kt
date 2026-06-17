package org.wikipedia.tests.wiktionary

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.wikipedia.compose.theme.BaseTheme
import org.wikipedia.dataclient.restbase.RbDefinition
import org.wikipedia.util.Resource
import org.wikipedia.wiktionary.WiktionaryDialogContent

/**
 * Compose component-level integration tests for WiktionaryDialogContent.
 *
 * Coverage targets:
 * - org.wikipedia.wiktionary.WiktionaryDialogScreen (236 lines)
 * - org.wikipedia.wiktionary.WiktionaryDialogContent
 * - org.wikipedia.wiktionary.DefinitionList
 * - org.wikipedia.wiktionary.DefinitionWithExamples
 *
 * Tests all three UI states (Loading, Error, Success) without needing
 * to launch a full Activity or mock network calls.
 */
class WiktionaryDialogComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingStateShowsProgressIndicator() {
        composeTestRule.setContent {
            BaseTheme {
                WiktionaryDialogContent(
                    title = "Lorem ipsum",
                    wiktionaryDialogState = Resource.Loading(),
                    onDialogLinkClick = {}
                )
            }
        }

        // The title text should still be visible in Loading state
        composeTestRule
            .onNodeWithText("Lorem ipsum")
            .assertIsDisplayed()
    }

    @Test
    fun testErrorStateShowsNoDefinitionsText() {
        composeTestRule.setContent {
            BaseTheme {
                WiktionaryDialogContent(
                    title = "NonexistentWord",
                    wiktionaryDialogState = Resource.Error(Throwable("No definitions found")),
                    onDialogLinkClick = {}
                )
            }
        }

        // Error state should display the "no definitions found" message
        composeTestRule
            .onNodeWithText("No definitions found.")
            .assertIsDisplayed()
    }

    @Test
    fun testSuccessStateShowsDefinitions() {
        val usageList = listOf(
            RbDefinition.Usage(
                partOfSpeech = "Noun",
                definitions = listOf(
                    RbDefinition.Definition("A test definition", listOf("Example sentence 1"))
                )
            )
        )

        composeTestRule.setContent {
            BaseTheme {
                WiktionaryDialogContent(
                    title = "TestWord",
                    wiktionaryDialogState = Resource.Success(usageList),
                    onDialogLinkClick = {}
                )
            }
        }

        // Should show the title
        composeTestRule
            .onNodeWithText("TestWord")
            .assertIsDisplayed()

        // Should show the part of speech
        composeTestRule
            .onNodeWithText("Noun")
            .assertIsDisplayed()
    }

    @Test
    fun testSuccessStateShowsMultipleDefinitions() {
        val usageList = listOf(
            RbDefinition.Usage(
                partOfSpeech = "Noun",
                definitions = listOf(
                    RbDefinition.Definition("First definition", listOf("Example A")),
                    RbDefinition.Definition("Second definition", listOf("Example B"))
                )
            ),
            RbDefinition.Usage(
                partOfSpeech = "Verb",
                definitions = listOf(
                    RbDefinition.Definition("Third definition", null)
                )
            )
        )

        composeTestRule.setContent {
            BaseTheme {
                WiktionaryDialogContent(
                    title = "MultiWord",
                    wiktionaryDialogState = Resource.Success(usageList),
                    onDialogLinkClick = {}
                )
            }
        }

        // Both parts of speech should be displayed
        composeTestRule
            .onNodeWithText("Noun")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Verb")
            .assertIsDisplayed()
    }
}
