package org.wikipedia.page

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NamespaceExtTest {

    @Test
    fun testMainNamespaceCode() {
        assertEquals(0, Namespace.MAIN.code())
    }

    @Test
    fun testTalkNamespaceCode() {
        assertEquals(1, Namespace.TALK.code())
    }

    @Test
    fun testUserNamespaceCode() {
        assertEquals(2, Namespace.USER.code())
    }

    @Test
    fun testUserTalkNamespaceCode() {
        assertEquals(3, Namespace.USER_TALK.code())
    }

    @Test
    fun testOfMainNamespace() {
        val ns = Namespace.of(0)
        assertEquals(Namespace.MAIN, ns)
    }

    @Test
    fun testOfTalkNamespace() {
        val ns = Namespace.of(1)
        assertEquals(Namespace.TALK, ns)
    }

    @Test
    fun testOfSpecialNamespace() {
        val ns = Namespace.of(-1)
        assertEquals(Namespace.SPECIAL, ns)
    }

    @Test
    fun testOfFileNamespace() {
        val ns = Namespace.of(6)
        assertEquals(Namespace.FILE, ns)
    }

    @Test
    fun testOfFileTalkNamespace() {
        val ns = Namespace.of(7)
        assertEquals(Namespace.FILE_TALK, ns)
    }

    @Test
    fun testOfTemplateNamespace() {
        val ns = Namespace.of(10)
        assertEquals(Namespace.TEMPLATE, ns)
    }

    @Test
    fun testOfCategoryNamespace() {
        val ns = Namespace.of(14)
        assertEquals(Namespace.CATEGORY, ns)
    }

    @Test
    fun testNamespaceValuesNotEmpty() {
        assertTrue(Namespace.entries.isNotEmpty())
    }

    @Test
    fun testIsMainNamespace() {
        assertTrue(Namespace.MAIN.main())
    }

    @Test
    fun testTalkIsNotMainNamespace() {
        assertFalse(Namespace.TALK.main())
    }

    @Test
    fun testSpecialIsNotMainNamespace() {
        assertFalse(Namespace.SPECIAL.main())
    }

    @Test
    fun testNamespaceIsTalk() {
        assertTrue(Namespace.TALK.talk())
    }

    @Test
    fun testMainIsNotTalk() {
        assertFalse(Namespace.MAIN.talk())
    }

    @Test
    fun testSpecialIsNotTalk() {
        assertFalse(Namespace.SPECIAL.talk())
    }
}
