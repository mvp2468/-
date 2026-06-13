package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MathUtilTest {

    @Test
    fun testPercentageBasic() {
        val result = MathUtil.percentage(50f, 100f)
        assertEquals(50f, result)
    }

    @Test
    fun testPercentageZeroNumerator() {
        val result = MathUtil.percentage(0f, 100f)
        assertEquals(0f, result)
    }

    @Test
    fun testPercentageFullFraction() {
        val result = MathUtil.percentage(25f, 200f)
        assertEquals(12.5f, result)
    }

    @Test
    fun testAveragedEmptyReturnsZero() {
        val averaged = MathUtil.Averaged<Int>()
        assertEquals(0.0, averaged.average, 0.0)
    }

    @Test
    fun testAveragedSingleSample() {
        val averaged = MathUtil.Averaged<Int>()
        averaged.addSample(10)
        assertEquals(10.0, averaged.average, 0.0)
    }

    @Test
    fun testAveragedMultipleSamples() {
        val averaged = MathUtil.Averaged<Int>()
        averaged.addSample(10)
        averaged.addSample(20)
        averaged.addSample(30)
        assertEquals(20.0, averaged.average, 0.0)
    }

    @Test
    fun testAveragedWithDoubleSamples() {
        val averaged = MathUtil.Averaged<Double>()
        averaged.addSample(1.5)
        averaged.addSample(2.5)
        assertEquals(2.0, averaged.average, 0.0)
    }

    @Test
    fun testAveragedReset() {
        val averaged = MathUtil.Averaged<Int>()
        averaged.addSample(100)
        averaged.reset()
        assertEquals(0.0, averaged.average, 0.0)
    }

    @Test
    fun testAveragedAfterResetAllowsNewSamples() {
        val averaged = MathUtil.Averaged<Int>()
        averaged.addSample(100)
        averaged.reset()
        averaged.addSample(50)
        assertEquals(50.0, averaged.average, 0.0)
    }

    @Test
    fun testAveragedWithNullSamplesThrows() {
        val averaged = MathUtil.Averaged<Int?>()
        try {
            averaged.addSample(null)
            fail("Expected NullPointerException")
        } catch (e: NullPointerException) {
            // expected
        }
    }
}
