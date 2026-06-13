package org.wikipedia.page

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.auth.AccountUtil
import org.wikipedia.dataclient.page.Protection

@RunWith(RobolectricTestRunner::class)
class PagePropertiesTest {

    @Before
    fun setUp() {
        mockkObject(AccountUtil)
    }

    @After
    fun tearDown() {
        unmockkObject(AccountUtil)
    }

    // ---- Default values ----

    @Test
    fun testDefaultValues() {
        val props = PageProperties(namespace = Namespace.MAIN)
        assertEquals(0, props.pageId)
        assertEquals(0, props.revisionId)
        assertEquals("", props.displayTitle)
        assertFalse(props.isMainPage)
        assertNull(props.leadImageUrl)
        assertNull(props.leadImageName)
        assertEquals(0, props.leadImageWidth)
        assertEquals(0, props.leadImageHeight)
        assertNull(props.geo)
        assertNull(props.wikiBaseItem)
        assertNull(props.descriptionSource)
        assertFalse(props.canEdit)
    }

    @Test
    fun testCustomProperties() {
        val props = PageProperties(pageId = 123, namespace = Namespace.MAIN,
            revisionId = 456, displayTitle = "Test", isMainPage = false,
            leadImageUrl = "example.com/img.jpg", leadImageName = "img.jpg",
            leadImageWidth = 800, leadImageHeight = 600,
            canEdit = true)
        assertEquals(123, props.pageId)
        assertEquals(456, props.revisionId)
        assertEquals("Test", props.displayTitle)
        assertEquals("example.com/img.jpg", props.leadImageUrl)
        assertEquals("img.jpg", props.leadImageName)
        assertEquals(800, props.leadImageWidth)
        assertEquals(600, props.leadImageHeight)
        assertTrue(props.canEdit)
    }

    @Test
    fun testNamespaceProperty() {
        val props = PageProperties(namespace = Namespace.FILE)
        assertEquals(Namespace.FILE, props.namespace)
    }

    @Test
    fun testMainPageProperty() {
        val props = PageProperties(namespace = Namespace.MAIN, isMainPage = true)
        assertTrue(props.isMainPage)
    }

    // ---- protection setter ----

    @Test
    fun testProtectionSetToNull() {
        val props = PageProperties(namespace = Namespace.MAIN, canEdit = true)
        props.protection = null
        assertNull(props.protection)
        // When protection is null, editProtectionStatus becomes "" which isEmpty() == true, so canEdit = true
        assertTrue(props.canEdit)
    }

    @Test
    fun testProtectionWithEmptyEditRolesMakesEditable() {
        every { AccountUtil.isMemberOf(any()) } returns false
        val props = PageProperties(namespace = Namespace.MAIN, canEdit = false)

        val protection = mockk<Protection> {
            every { firstAllowedEditorRole } returns ""
            every { editRoles } returns emptySet()
        }
        props.protection = protection

        assertTrue(props.canEdit)
    }

    @Test
    fun testProtectionWithRolesAndUserNotMember() {
        every { AccountUtil.isMemberOf(any()) } returns false
        val props = PageProperties(namespace = Namespace.MAIN, canEdit = true)

        val protection = mockk<Protection> {
            every { firstAllowedEditorRole } returns "sysop"
            every { editRoles } returns setOf("sysop")
        }
        props.protection = protection

        assertFalse(props.canEdit)
    }

    @Test
    fun testProtectionWithRolesAndUserIsMember() {
        every { AccountUtil.isMemberOf(any()) } returns true
        val props = PageProperties(namespace = Namespace.MAIN, canEdit = false)

        val protection = mockk<Protection> {
            every { firstAllowedEditorRole } returns "sysop"
            every { editRoles } returns setOf("sysop")
        }
        props.protection = protection

        assertTrue(props.canEdit)
    }

    @Test
    fun testProtectionSettingUpdatesEditProtectionStatus() {
        every { AccountUtil.isMemberOf(any()) } returns true
        val props = PageProperties(namespace = Namespace.MAIN, canEdit = false)

        val protection = mockk<Protection> {
            every { firstAllowedEditorRole } returns "extendedconfirmed"
            every { editRoles } returns setOf("extendedconfirmed")
        }
        props.protection = protection

        assertTrue(props.canEdit)
    }

    @Test
    fun testProtectionSetToNullThenAssign() {
        every { AccountUtil.isMemberOf(any()) } returns true
        val props = PageProperties(namespace = Namespace.MAIN, canEdit = false)
        props.protection = null
        // When protection is null, editProtectionStatus becomes "" -> isEmpty() == true, so canEdit = true
        assertTrue(props.canEdit)

        val protection = mockk<Protection> {
            every { firstAllowedEditorRole } returns "autoconfirmed"
            every { editRoles } returns setOf("autoconfirmed")
        }
        props.protection = protection
        assertTrue(props.canEdit)
    }

    @Test
    fun testMultipleProtectionAssignments() {
        every { AccountUtil.isMemberOf(any()) } returns true
        val props = PageProperties(namespace = Namespace.MAIN)

        // First assignment
        val p1 = mockk<Protection> {
            every { firstAllowedEditorRole } returns "sysop"
            every { editRoles } returns setOf("sysop")
        }
        props.protection = p1
        assertTrue(props.canEdit)

        // User is not member of second role
        every { AccountUtil.isMemberOf(any()) } returns false
        val p2 = mockk<Protection> {
            every { firstAllowedEditorRole } returns "bureaucrat"
            every { editRoles } returns setOf("bureaucrat")
        }
        props.protection = p2
        assertFalse(props.canEdit)
    }
}
