package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class MetricsTest {

    @Test
    fun testItemsDefaults() {
        val item = Metrics.Items()
        assertEquals("", item.project)
        assertEquals("", item.editorType)
        assertEquals("", item.pageTitle)
        assertEquals("", item.granularity)
        assertTrue(item.results.isEmpty())
    }

    @Test
    fun testResultsDefaults() {
        val result = Metrics.Results()
        assertEquals("", result.timestamp)
        assertEquals(0, result.edits)
    }
}
