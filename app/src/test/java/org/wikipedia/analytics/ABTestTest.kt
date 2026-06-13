package org.wikipedia.analytics

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikipedia.settings.PrefsIoUtil

@RunWith(RobolectricTestRunner::class)
class ABTestTest {

    private class TestABTest : ABTest("test_experiment", ABTest.GROUP_SIZE_2) {
        fun setGroup(group: Int) {
            testGroup = group
            PrefsIoUtil.setInt("ab_test_test_experiment", group)
        }

        override fun getGroupName(): String {
            return when (group) {
                ABTest.GROUP_2 -> "variant"
                else -> "control"
            }
        }
    }

    private class TestABTest3Groups : ABTest("test_3group_experiment", ABTest.GROUP_SIZE_3) {
        fun setGroup(group: Int) {
            testGroup = group
            PrefsIoUtil.setInt("ab_test_test_3group_experiment", group)
        }

        override fun getGroupName(): String {
            return when (group) {
                ABTest.GROUP_2 -> "variant_a"
                ABTest.GROUP_3 -> "variant_b"
                else -> "control"
            }
        }
    }

    @Test
    fun testGroupNameForGroup1() {
        val test = TestABTest()
        test.setGroup(ABTest.GROUP_1)
        assertEquals("control", test.getGroupName())
    }

    @Test
    fun testGroupNameForGroup2() {
        val test = TestABTest()
        test.setGroup(ABTest.GROUP_2)
        assertEquals("variant", test.getGroupName())
    }

    @Test
    fun test3GroupExperimentGroup1() {
        val test = TestABTest3Groups()
        test.setGroup(ABTest.GROUP_1)
        assertEquals("control", test.getGroupName())
    }

    @Test
    fun test3GroupExperimentGroup2() {
        val test = TestABTest3Groups()
        test.setGroup(ABTest.GROUP_2)
        assertEquals("variant_a", test.getGroupName())
    }

    @Test
    fun test3GroupExperimentGroup3() {
        val test = TestABTest3Groups()
        test.setGroup(ABTest.GROUP_3)
        assertEquals("variant_b", test.getGroupName())
    }

    @Test
    fun testGroupNameProperty() {
        val test = TestABTest()
        assertEquals("test_experiment", test.name)
    }

    @Test
    fun testShouldInstrumentDefaultTrue() {
        val test = TestABTest()
        assertTrue(test.shouldInstrument())
    }

    @Test
    fun testCompanionConstants() {
        assertEquals(2, ABTest.GROUP_SIZE_2)
        assertEquals(3, ABTest.GROUP_SIZE_3)
        assertEquals(0, ABTest.GROUP_1)
        assertEquals(1, ABTest.GROUP_2)
        assertEquals(2, ABTest.GROUP_3)
    }

    @Test
    fun testGroupAssignmentFromPrefs() {
        PrefsIoUtil.setInt("ab_test_test_assignment_pref", -1)
        val test = TestABTest()
        val group = test.group
        assertTrue(group in 0..1)
    }
}
