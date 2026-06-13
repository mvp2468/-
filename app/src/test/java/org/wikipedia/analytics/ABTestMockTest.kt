package org.wikipedia.analytics

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.settings.PrefsIoUtil

@RunWith(RobolectricTestRunner::class)
class ABTestMockTest {

    private class TestABTest(name: String, groupCount: Int) : ABTest(name, groupCount) {
        override fun getGroupName(): String = "Group$testGroup"
        override fun assignGroup() {
            testGroup = 0 // deterministic assignment for testing
        }
    }

    @Before
    fun setup() {
        mockkObject(PrefsIoUtil)
    }

    @After
    fun tearDown() {
        unmockkObject(PrefsIoUtil)
    }

    @Test
    fun testName() {
        val test = TestABTest("my_test", ABTest.GROUP_SIZE_2)
        assertEquals("my_test", test.name)
    }

    @Test
    fun testGroupAssignedWhenNotPreviouslySet() {
        every { PrefsIoUtil.getInt("ab_test_new_test", -1) } returns -1
        every { PrefsIoUtil.setInt("ab_test_new_test", any()) } returns Unit

        val test = TestABTest("new_test", ABTest.GROUP_SIZE_2)
        val group = test.group
        assertEquals(0, group) // our overridden assignGroup returns 0
    }

    @Test
    fun testGroupRetrievedFromPrefs() {
        every { PrefsIoUtil.getInt("ab_test_cached_test", -1) } returns 1

        val test = TestABTest("cached_test", ABTest.GROUP_SIZE_3)
        assertEquals(1, test.group)
    }

    @Test
    fun testGroupCachedAfterFirstRead() {
        every { PrefsIoUtil.getInt("ab_test_cache_test", -1) } returnsMany listOf(-1, 0)
        every { PrefsIoUtil.setInt("ab_test_cache_test", any()) } returns Unit

        val test = TestABTest("cache_test", ABTest.GROUP_SIZE_2)
        val firstRead = test.group
        val secondRead = test.group
        assertEquals(firstRead, secondRead)
    }

    @Test
    fun testGroupSize2() {
        assertEquals(2, ABTest.GROUP_SIZE_2)
    }

    @Test
    fun testGroupSize3() {
        assertEquals(3, ABTest.GROUP_SIZE_3)
    }

    @Test
    fun testGroupConstants() {
        assertEquals(0, ABTest.GROUP_1)
        assertEquals(1, ABTest.GROUP_2)
        assertEquals(2, ABTest.GROUP_3)
    }

    @Test
    fun testShouldInstrumentDefaultTrue() {
        every { PrefsIoUtil.getInt("ab_test_instrument_test", -1) } returns 0

        val test = TestABTest("instrument_test", ABTest.GROUP_SIZE_2)
        assertTrue(test.shouldInstrument())
    }

    @Test
    fun testGroupNotPreviouslySetTriggersAssign() {
        every { PrefsIoUtil.getInt("ab_test_assign_test", -1) } returns -1
        every { PrefsIoUtil.setInt("ab_test_assign_test", 0) } returns Unit

        val test = TestABTest("assign_test", ABTest.GROUP_SIZE_3)
        assertEquals(0, test.group)
        // The setInt should have been called with the assigned group
    }

    @Test
    fun testGroupNotPreviouslySetButAssignToOtherThanZero() {
        // Override with a class that assigns group 1
        val test = object : ABTest("alt_test", ABTest.GROUP_SIZE_3) {
            override fun getGroupName(): String = "Group$testGroup"
            override fun assignGroup() {
                testGroup = 2
            }
        }
        every { PrefsIoUtil.getInt("ab_test_alt_test", -1) } returns -1
        every { PrefsIoUtil.setInt("ab_test_alt_test", any()) } returns Unit

        assertEquals(2, test.group)
    }
}
