package org.wikipedia.page

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.dataclient.WikiSite

@RunWith(RobolectricTestRunner::class)
class PageTest {

    private val enwiki = WikiSite.forLanguageCode("en")

    private fun mockPageProperties(isMain: Boolean, namespace: Namespace, canEdit: Boolean): PageProperties {
        return mockk<PageProperties>(relaxed = true) {
            every { isMainPage } returns isMain
            every { this@mockk.namespace } returns namespace
            every { this@mockk.canEdit } returns canEdit
            every { displayTitle } returns "Mock Title"
        }
    }

    private fun mockPageTitle(): PageTitle {
        return PageTitle("Test", enwiki)
    }

    @Test
    fun testIsArticleTrueForMainNamespaceNonMainPage() {
        val props = mockPageProperties(isMain = false, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertTrue(page.isArticle)
    }

    @Test
    fun testIsArticleFalseForMainPage() {
        val props = mockPageProperties(isMain = true, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertFalse(page.isArticle)
    }

    @Test
    fun testIsArticleFalseForNonMainNamespace() {
        val props = mockPageProperties(isMain = false, namespace = Namespace.TALK, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertFalse(page.isArticle)
    }

    @Test
    fun testIsProtectedTrueWhenCannotEdit() {
        val props = mockPageProperties(isMain = false, namespace = Namespace.MAIN, canEdit = false)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertTrue(page.isProtected)
    }

    @Test
    fun testIsProtectedFalseWhenCanEdit() {
        val props = mockPageProperties(isMain = false, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertFalse(page.isProtected)
    }

    @Test
    fun testIsMainPageDelegatesToProperties() {
        val props = mockPageProperties(isMain = true, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertTrue(page.isMainPage)
    }

    @Test
    fun testIsMainPageFalseWhenNotMain() {
        val props = mockPageProperties(isMain = false, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertFalse(page.isMainPage)
    }

    @Test
    fun testDisplayTitleDelegatesToProperties() {
        val props = mockPageProperties(isMain = false, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertEquals("Mock Title", page.displayTitle)
    }

    @Test
    fun testIsArticleFalseWhenMainPageInMainNamespace() {
        // Main page in NS_MAIN: still not an article
        val props = mockPageProperties(isMain = true, namespace = Namespace.MAIN, canEdit = true)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertFalse(page.isArticle)
    }

    @Test
    fun testIsProtectedTrueForProtectedMainPage() {
        val props = mockPageProperties(isMain = true, namespace = Namespace.MAIN, canEdit = false)
        val page = Page(mockPageTitle(), pageProperties = props)
        assertTrue(page.isProtected)
    }
}
