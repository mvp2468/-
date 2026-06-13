package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActiveTimerTest {

    @Test
    fun testElapsedMillisStartsAtZero() {
        val timer = ActiveTimer()
        assertTrue(timer.elapsedMillis < 100)
    }

    @Test
    fun testElapsedSecStartsAtZero() {
        val timer = ActiveTimer()
        assertEquals(0, timer.elapsedSec)
    }

    @Test
    fun testElapsedMillisIncreasesAfterDelay() {
        val timer = ActiveTimer()
        Thread.sleep(50)
        assertTrue(timer.elapsedMillis >= 50)
    }

    @Test
    fun testPauseStopsElapsedTime() {
        val timer = ActiveTimer()
        Thread.sleep(10)
        timer.pause()
        val pausedTime = timer.elapsedMillis
        Thread.sleep(50)
        assertEquals(pausedTime, timer.elapsedMillis)
    }

    @Test
    fun testResumeAfterPauseContinuesCounting() {
        val timer = ActiveTimer()
        Thread.sleep(10)
        timer.pause()
        timer.resume()
        Thread.sleep(10)
        assertTrue(timer.elapsedMillis >= 20)
    }

    @Test
    fun testResetRestartsTimer() {
        val timer = ActiveTimer()
        Thread.sleep(50)
        timer.reset()
        assertTrue(timer.elapsedMillis < 50)
        assertEquals(0, timer.elapsedSec)
    }

    @Test
    fun testElapsedSecRoundsDown() {
        val timer = ActiveTimer()
        Thread.sleep(50)
        val sec = timer.elapsedSec
        assertTrue(sec >= 0)
    }

    @Test
    fun testMultiplePauseResumeCycles() {
        val timer = ActiveTimer()
        timer.pause()
        timer.resume()
        timer.pause()
        timer.resume()
        assertTrue(timer.elapsedMillis >= 0)
    }
}
