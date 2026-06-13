package org.wikipedia.edit

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EditTagsTest {

    @Test
    fun testSuggestedEditTag() {
        assertEquals("app-suggestededit", EditTags.APP_SUGGESTED_EDIT)
    }

    @Test
    fun testDescriptionAddTag() {
        assertEquals("app-description-add", EditTags.APP_DESCRIPTION_ADD)
    }

    @Test
    fun testDescriptionChangeTag() {
        assertEquals("app-description-change", EditTags.APP_DESCRIPTION_CHANGE)
    }

    @Test
    fun testRollbackTag() {
        assertEquals("app-rollback", EditTags.APP_ROLLBACK)
    }

    @Test
    fun testUndoTag() {
        assertEquals("app-undo", EditTags.APP_UNDO)
    }

    @Test
    fun testSectionSourceTag() {
        assertEquals("app-section-source", EditTags.APP_SECTION_SOURCE)
    }

    @Test
    fun testFullSourceTag() {
        assertEquals("app-full-source", EditTags.APP_FULL_SOURCE)
    }

    @Test
    fun testTalkTopicTag() {
        assertEquals("app-talk-topic", EditTags.APP_TALK_TOPIC)
    }

    @Test
    fun testImageCaptionAddTag() {
        assertEquals("app-image-caption-add", EditTags.APP_IMAGE_CAPTION_ADD)
    }

    @Test
    fun testImageTagAddTag() {
        assertEquals("app-image-tag-add", EditTags.APP_IMAGE_TAG_ADD)
    }

    @Test
    fun testAiAssistTag() {
        assertEquals("app-ai-assist", EditTags.APP_AI_ASSIST)
    }

    @Test
    fun testAllTagsAreUnique() {
        val tags = listOf(
            EditTags.APP_SUGGESTED_EDIT,
            EditTags.APP_DESCRIPTION_ADD,
            EditTags.APP_DESCRIPTION_CHANGE,
            EditTags.APP_DESCRIPTION_TRANSLATE,
            EditTags.APP_ROLLBACK,
            EditTags.APP_UNDO,
            EditTags.APP_SECTION_SOURCE,
            EditTags.APP_FULL_SOURCE,
            EditTags.APP_SELECT_SOURCE,
            EditTags.APP_TALK_SOURCE,
            EditTags.APP_TALK_REPLY,
            EditTags.APP_TALK_TOPIC,
            EditTags.APP_IMAGE_CAPTION_ADD,
            EditTags.APP_IMAGE_CAPTION_TRANSLATE,
            EditTags.APP_IMAGE_TAG_ADD,
            EditTags.APP_IMAGE_ADD_TOP,
            EditTags.APP_IMAGE_ADD_INFOBOX,
            EditTags.APP_AI_ASSIST
        )
        assertEquals(tags.size, tags.distinct().size)
    }
}
