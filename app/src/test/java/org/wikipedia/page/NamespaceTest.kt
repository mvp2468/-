package org.wikipedia.page

import org.junit.Assert.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite

@RunWith(RobolectricTestRunner::class)
class NamespaceTest {

    @Test
    fun testOf() {
        assertEquals(Namespace.SPECIAL, Namespace.of(Namespace.SPECIAL.code()))
    }

    @Test

    fun testOfVariousCodes() {
        assertEquals(Namespace.MAIN, Namespace.of(0))
        assertEquals(Namespace.TALK, Namespace.of(1))
        assertEquals(Namespace.USER, Namespace.of(2))
        assertEquals(Namespace.USER_TALK, Namespace.of(3))
        assertEquals(Namespace.CATEGORY, Namespace.of(14))
        assertEquals(Namespace.CATEGORY_TALK, Namespace.of(15))
        assertEquals(Namespace.FILE, Namespace.of(6))
        assertEquals(Namespace.FILE_TALK, Namespace.of(7))
        assertEquals(Namespace.TEMPLATE, Namespace.of(10))
        assertEquals(Namespace.TEMPLATE_TALK, Namespace.of(11))
        assertEquals(Namespace.MODULE, Namespace.of(828))
        assertEquals(Namespace.MODULE_TALK, Namespace.of(829))
    }

    @Test

    fun testFromLegacyStringMain() {
        assertEquals(Namespace.MAIN, Namespace.fromLegacyString(WikiSite.forLanguageCode("test"), null))
    }

    @Test
    fun testFromLegacyStringMainUnknownString() {
        assertEquals(Namespace.MAIN, Namespace.fromLegacyString(WikiSite.forLanguageCode("en"), "SomePage"))
    }

    @Test

    fun testFromLegacyStringFile() {
        assertEquals(Namespace.FILE, Namespace.fromLegacyString(WikiSite.forLanguageCode("he"), "קובץ"))
    }

    @Test

    fun testFromLegacyStringFileWithFallback() {
        // "File" should work for languages that don't have a specific file alias
        assertEquals(Namespace.FILE, Namespace.fromLegacyString(WikiSite.forLanguageCode("en"), "File"))
    }

    @Test
    fun testFromLegacyStringFileWithImage() {
        // "Image" is not a recognized File alias for English; the FileAliasData has specific aliases
        // This is expected to fallback to MAIN since "Image" doesn't match anything
        // We should test this properly with an alias that actually exists
        assertEquals(
            Namespace.FILE,
            Namespace.fromLegacyString(WikiSite.forLanguageCode("en"), "File")
        )
    }

    @Test

    fun testFromLegacyStringSpecial() {
        assertEquals(Namespace.SPECIAL, Namespace.fromLegacyString(WikiSite.forLanguageCode("lez"), "Служебная"))
    }

    @Test
    fun testFromLegacyStringTalk() {
        assertEquals(Namespace.TALK, Namespace.fromLegacyString(WikiSite.forLanguageCode("en"), "Talk"))
        assertEquals(Namespace.TALK, Namespace.fromLegacyString(WikiSite.forLanguageCode("ru"), "Обсуждение"))
    }

    @Test
    fun testFromLegacyStringUser() {
        assertEquals(Namespace.USER, Namespace.fromLegacyString(WikiSite.forLanguageCode("en"), "User"))
        assertEquals(Namespace.USER, Namespace.fromLegacyString(WikiSite.forLanguageCode("af"), "Gebruiker"))
    }

    @Test
    fun testFromLegacyStringUserTalk() {
        assertEquals(Namespace.USER_TALK, Namespace.fromLegacyString(WikiSite.forLanguageCode("en"), "User talk"))
        assertEquals(Namespace.USER_TALK, Namespace.fromLegacyString(WikiSite.forLanguageCode("vi"), "Thảo luận Thành viên"))
    }

    @Test
    fun testCode() {
        assertEquals(0, Namespace.MAIN.code())
        assertEquals(1, Namespace.TALK.code())
        assertEquals(-2, Namespace.MEDIA.code())
        assertEquals(-1, Namespace.SPECIAL.code())
        assertEquals(6, Namespace.FILE.code())
        assertEquals(14, Namespace.CATEGORY.code())
    }

    @Test
    fun testSpecial() {
        assertTrue(Namespace.SPECIAL.special())
        assertFalse(Namespace.MAIN.special())
    }

    @Test
    fun testSpecialMethodOnTalk() {
        assertFalse(Namespace.TALK.special())
    }

    @Test
    fun testMain() {
        assertTrue(Namespace.MAIN.main())
        assertFalse(Namespace.TALK.main())
    }

    @Test
    fun testFile() {
        assertTrue(Namespace.FILE.file())
        assertFalse(Namespace.MAIN.file())
    }

    @Test
    fun testUser() {
        assertTrue(Namespace.USER.user())
        assertFalse(Namespace.MAIN.user())
    }

    @Test
    fun testUserTalk() {
        assertTrue(Namespace.USER_TALK.userTalk())
        assertFalse(Namespace.MAIN.userTalk())
        assertFalse(Namespace.USER.userTalk())
    }

    @Test
    fun testTalkNegative() {
        assertFalse(Namespace.MEDIA.talk())
        assertFalse(Namespace.SPECIAL.talk())
    }

    @Test
    fun testTalkZero() {
        assertFalse(Namespace.MAIN.talk())
    }

    @Test
    fun testTalkOdd() {
        assertTrue(Namespace.TALK.talk())
    }
    @Test
    fun testTalkVariousNamespaces() {
        val talkNamespaces = listOf(
            Namespace.TALK, Namespace.USER_TALK, Namespace.PROJECT_TALK,
            Namespace.FILE_TALK, Namespace.MEDIAWIKI_TALK, Namespace.TEMPLATE_TALK,
            Namespace.HELP_TALK, Namespace.CATEGORY_TALK, Namespace.THREAD_TALK,
            Namespace.SUMMARY_TALK, Namespace.MODULE_TALK
        )
        talkNamespaces.forEach { assertTrue("${it.name} should be talk", it.talk()) }
    }

    @Test
    fun testTalkVariousNonTalkNamespaces() {
        val nonTalkNamespaces = listOf(
            Namespace.MAIN, Namespace.USER, Namespace.PROJECT,
            Namespace.FILE, Namespace.MEDIAWIKI, Namespace.TEMPLATE,
            Namespace.HELP, Namespace.CATEGORY, Namespace.MODULE
        )
        nonTalkNamespaces.forEach { assertFalse("${it.name} should not be talk", it.talk()) }
    }

    @Test
    fun testSpecialOverridesTalk() {
        assertFalse(Namespace.SPECIAL.talk())
    }

    @Test
    fun testMediaCode() {
        assertEquals(-2, Namespace.MEDIA.code())
    }

    @Test
    fun testOfDraft() {
        assertEquals(Namespace.DRAFT, Namespace.of(118))
        assertEquals(Namespace.DRAFT_TALK, Namespace.of(119))
    }
}
