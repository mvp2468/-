package org.wikipedia.dataclient.restbase

import org.junit.Assert.*
import org.junit.Test

class RbDefinitionTest {

    @Test
    fun testUsageDefaults() {
        val usage = RbDefinition.Usage(partOfSpeech = "noun", definitions = emptyList())
        assertEquals("noun", usage.partOfSpeech)
        assertTrue(usage.definitions.isEmpty())
    }

    @Test
    fun testDefinitionDefaults() {
        val def = RbDefinition.Definition(definition = "A test")
        assertEquals("A test", def.definition)
        assertNull(def.examples)
    }

    @Test
    fun testDefinitionWithExamples() {
        val def = RbDefinition.Definition(
            definition = "A test with examples",
            examples = listOf("Example 1", "Example 2")
        )
        assertEquals(2, def.examples!!.size)
        assertEquals("Example 1", def.examples!![0])
    }
}
