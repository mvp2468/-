package org.wikipedia.analytics.eventplatform

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.wikimedia.testkitchen.config.StreamConfig
import org.wikimedia.testkitchen.config.sampling.SampleConfig
import org.wikipedia.settings.Prefs

@RunWith(RobolectricTestRunner::class)
class EventPlatformClientSubmitTest {

    @Before
    fun setUp() {
        EventPlatformClient.AssociationController.beginNewSession()
    }

    @Test
    fun testSubmitToInitialQueueWhenNoStreamConfigs() {
        EventPlatformClient.setStreamConfig(StreamConfig().also { it.streamName = "dummy" })
        val event = TestEvent("test_stream")
        EventPlatformClient.submit(event)
    }

    @Test
    fun testFlushCachedEventsWithEmptyQueue() {
        EventPlatformClient.flushCachedEvents()
    }

    @Test
    fun testSetEnabledTrue() {
        EventPlatformClient.setEnabled(true)
    }

    @Test
    fun testSetEnabledFalse() {
        EventPlatformClient.setEnabled(false)
    }

    @Test
    fun testGetStreamConfigReturnsNullForUnknownStream() {
        assertNull(EventPlatformClient.getStreamConfig("nonexistent_stream_12345"))
    }

    @Test
    fun testSetAndGetStreamConfig() {
        val config = StreamConfig().also { it.streamName = "test_config_stream" }
        EventPlatformClient.setStreamConfig(config)
        assertNotNull(EventPlatformClient.getStreamConfig("test_config_stream"))
        assertEquals("test_config_stream", EventPlatformClient.getStreamConfig("test_config_stream")!!.streamName)
    }

    @Test
    fun testSamplingControllerIsInSampleWithNoConfig() {
        val event = TestEvent("unconfigured_stream")
        assertFalse(EventPlatformClient.SamplingController.isInSample(event))
    }

    @Test
    fun testSamplingControllerWithFullRate() {
        EventPlatformClient.setStreamConfig(StreamConfig().also {
            it.streamName = "full_rate_stream"
            it.sampleConfig = SampleConfig(1.0)
        })
        assertTrue(EventPlatformClient.SamplingController.isInSample(TestEvent("full_rate_stream")))
    }

    @Test
    fun testSamplingControllerWithZeroRate() {
        EventPlatformClient.setStreamConfig(StreamConfig().also {
            it.streamName = "zero_rate_stream"
            it.sampleConfig = SampleConfig(0.0)
        })
        assertFalse(EventPlatformClient.SamplingController.isInSample(TestEvent("zero_rate_stream")))
    }

    @Test
    fun testSamplingControllerWithNoSamplingConfig() {
        EventPlatformClient.setStreamConfig(StreamConfig().also {
            it.streamName = "no_sampling_config_stream"
        })
        assertTrue(EventPlatformClient.SamplingController.isInSample(TestEvent("no_sampling_config_stream")))
    }

    @Test
    fun testSamplingControllerCaching() {
        EventPlatformClient.setStreamConfig(StreamConfig().also {
            it.streamName = "cache_test_stream"
            it.sampleConfig = SampleConfig(1.0)
        })
        val result1 = EventPlatformClient.SamplingController.isInSample(TestEvent("cache_test_stream"))
        val result2 = EventPlatformClient.SamplingController.isInSample(TestEvent("cache_test_stream"))
        assertEquals(result1, result2)
    }

    @Test
    fun testAssociationControllerPageViewIdLength() {
        val pageViewId = EventPlatformClient.AssociationController.pageViewId
        assertEquals(20, pageViewId.length)
    }

    @Test
    fun testAssociationControllerSessionIdLength() {
        val sessionId = EventPlatformClient.AssociationController.sessionId
        assertEquals(20, sessionId.length)
    }

    @Test
    fun testAssociationControllerBeginNewPageView() {
        EventPlatformClient.AssociationController.pageViewId
        EventPlatformClient.AssociationController.beginNewPageView()
        val newId = EventPlatformClient.AssociationController.pageViewId
        assertNotNull(newId)
        assertEquals(20, newId.length)
    }

    @Test
    fun testAssociationControllerBeginNewSession() {
        EventPlatformClient.AssociationController.beginNewSession()
        assertNull(Prefs.eventPlatformSessionId)
        val newSessionId = EventPlatformClient.AssociationController.sessionId
        assertNotNull(newSessionId)
        assertEquals(20, newSessionId.length)
    }

    @Test
    fun testSamplingControllerGetSamplingValueDevice() {
        val value = EventPlatformClient.SamplingController.getSamplingValue(SampleConfig.UNIT_DEVICE)
        assertTrue(value >= 0.0)
        assertTrue(value <= 1.0)
    }

    @Test
    fun testSamplingControllerGetSamplingValuePageview() {
        val value = EventPlatformClient.SamplingController.getSamplingValue(SampleConfig.UNIT_PAGEVIEW)
        assertTrue(value >= 0.0)
        assertTrue(value <= 1.0)
    }

    @Test
    fun testSamplingControllerGetSamplingValueSession() {
        val value = EventPlatformClient.SamplingController.getSamplingValue(SampleConfig.UNIT_SESSION)
        assertTrue(value >= 0.0)
        assertTrue(value <= 1.0)
    }

    @Test
    fun testSamplingControllerGetSamplingIdDevice() {
        val id = EventPlatformClient.SamplingController.getSamplingId(SampleConfig.UNIT_DEVICE)
        assertNotNull(id)
        assertTrue(id.isNotEmpty())
    }

    @Test
    fun testSamplingControllerGetSamplingIdPageview() {
        val id = EventPlatformClient.SamplingController.getSamplingId(SampleConfig.UNIT_PAGEVIEW)
        assertNotNull(id)
        assertEquals(20, id.length)
    }

    @Test
    fun testSamplingControllerGetSamplingIdSession() {
        val id = EventPlatformClient.SamplingController.getSamplingId(SampleConfig.UNIT_SESSION)
        assertNotNull(id)
        assertEquals(20, id.length)
    }
}
