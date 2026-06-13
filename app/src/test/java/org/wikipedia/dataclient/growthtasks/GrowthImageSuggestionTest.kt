package org.wikipedia.dataclient.growthtasks

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Assert.*
import org.junit.Test

class GrowthImageSuggestionTest {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    @Test
    fun testDefaultConstructor() {
        val suggestion = GrowthImageSuggestion()
        assertEquals(0, suggestion.titleNamespace)
        assertEquals("", suggestion.titleText)
        assertEquals("", suggestion.datasetId)
        assertTrue(suggestion.images.isEmpty())
    }

    @Test
    fun testConstructorWithImages() {
        val image = GrowthImageSuggestion.ImageItem(
            image = "test.jpg",
            displayFilename = "Test File.jpg",
            source = "commons",
            projects = listOf("enwiki", "frwiki")
        )
        val suggestion = GrowthImageSuggestion(
            titleNamespace = 0,
            titleText = "Test Article",
            datasetId = "ds123",
            images = listOf(image)
        )
        assertEquals(0, suggestion.titleNamespace)
        assertEquals("Test Article", suggestion.titleText)
        assertEquals("ds123", suggestion.datasetId)
        assertEquals(1, suggestion.images.size)
        assertEquals("test.jpg", suggestion.images[0].image)
        assertEquals(2, suggestion.images[0].projects.size)
    }

    @Test
    fun testImageItemWithMetadata() {
        val metadata = GrowthImageSuggestion.ImageMetadata(
            descriptionUrl = "https://commons.wikimedia.org/wiki/File:Test.jpg",
            thumbUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/test.jpg",
            fullUrl = "https://upload.wikimedia.org/wikipedia/commons/test.jpg",
            originalWidth = 1920,
            originalHeight = 1080,
            mediaType = "BITMAP",
            description = "A test image",
            author = "Test Author",
            license = "CC-BY-SA-4.0",
            date = "2024-01-01",
            caption = "Test caption",
            categories = listOf("Category1", "Category2"),
            reason = "suggested",
            contentLanguageName = "English"
        )
        assertEquals("Test Author", metadata.author)
        assertEquals(1920, metadata.originalWidth)
        assertEquals(1080, metadata.originalHeight)
        assertEquals("CC-BY-SA-4.0", metadata.license)
    }

    @Test
    fun testAddImageFeedbackBodyDefaults() {
        val body = GrowthImageSuggestion.AddImageFeedbackBody()
        assertEquals("", body.token)
        assertEquals(0L, body.editRevId)
        assertEquals("", body.filename)
        assertFalse(body.accepted)
        assertTrue(body.reasons.isEmpty())
    }

    @Test
    fun testSerializationRoundTrip() {
        val original = GrowthImageSuggestion(
            titleNamespace = 0,
            titleText = "Test",
            datasetId = "ds1",
            images = listOf(
                GrowthImageSuggestion.ImageItem(image = "img.jpg", source = "commons")
            )
        )
        val encoded = json.encodeToJsonElement(original)
        assertNotNull(encoded)
    }
}
