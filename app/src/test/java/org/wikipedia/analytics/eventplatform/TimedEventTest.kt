package org.wikipedia.analytics.eventplatform

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TimedEventTest {

    private class TestTimedEvent : TimedEvent()

    @Test
    fun testDurationStartsAtZero() {
        val event = TestTimedEvent()
        assertTrue(event.duration < 100)
    }

    @Test
    fun testDurationIncreasesWithTime() {
        val event = TestTimedEvent()
        Thread.sleep(50)
        assertTrue(event.duration > 0)
    }

    @Test
    fun testPauseAndResumeExcludesPausedTime() {
        val event = TestTimedEvent()
        Thread.sleep(10)
        val beforePause = event.duration
        event.pause()
        Thread.sleep(50)
        event.resume()
        // After resume, the duration should be close to the pre-pause duration
        // (the paused time has been excluded from startTime)
        val afterResume = event.duration
        assertTrue(Math.abs(afterResume - beforePause) < 50)
    }

    @Test
    fun testResumeContinuesDuration() {
        val event = TestTimedEvent()
        Thread.sleep(10)
        event.pause()
        event.resume()
        Thread.sleep(10)
        assertTrue(event.duration >= 20)
    }

    @Test
    fun testResetRestartsDuration() {
        val event = TestTimedEvent()
        Thread.sleep(50)
        event.reset()
        assertTrue(event.duration < 50)
    }

    @Test
    fun testResumeWithoutPauseDoesNothing() {
        val event = TestTimedEvent()
        Thread.sleep(10)
        val beforeDuration = event.duration
        event.resume()
        assertTrue(event.duration >= beforeDuration)
    }

    @Test
    fun testMultiplePauseResumeCycles() {
        val event = TestTimedEvent()
        event.pause()
        event.resume()
        event.pause()
        event.resume()
        assertTrue(event.duration >= 0)
    }
}
