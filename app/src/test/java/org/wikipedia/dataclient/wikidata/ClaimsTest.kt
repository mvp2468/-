package org.wikipedia.dataclient.wikidata

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ClaimsTest {

    @Test
    fun testEntityIdValueDefaults() {
        val entity = Claims.EntityIdValue()
        assertEquals("", entity.id)
    }

    @Test
    fun testTimeValueDefaults() {
        val timeValue = Claims.TimeValue()
        assertEquals("", timeValue.time)
    }

    @Test
    fun testMonolingualTextValueDefaults() {
        val textValue = Claims.MonolingualTextValue()
        assertEquals("", textValue.text)
    }

    @Test
    fun testGlobeCoordinateValueLocationDefaults() {
        val coord = Claims.GlobeCoordinateValue()
        val location = coord.location
        assertEquals(0.0, location.latitude, 0.001)
        assertEquals(0.0, location.longitude, 0.001)
        assertEquals(0.0, location.altitude, 0.001)
    }

    @Test
    fun testGlobeCoordinateValueLocationProvider() {
        val coord = Claims.GlobeCoordinateValue()
        // Location provider should be empty string since it's hardcoded
        assertEquals("", coord.location.provider)
    }

    @Test
    fun testDataValueStringType() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":"Q12345","type":"string"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("\"Q12345\"", dataValue.value())
    }

    @Test
    fun testDataValueWikibaseEntityIdType() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":{"entity-type":"item","id":"Q42","numeric-id":42},"type":"wikibase-entityid"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("Q42", dataValue.value())
    }

    @Test
    fun testDataValueTimeType() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":{"time":"+1879-03-14T00:00:00Z","timezone":0,"before":0,"after":0,"precision":11,"calendarmodel":"http://www.wikidata.org/entity/Q1985727"},"type":"time"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("+1879-03-14T00:00:00Z", dataValue.value())
    }

    @Test
    fun testDataValueMonolingualTextType() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":{"text":"Example text","language":"en"},"type":"monolingualtext"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("Example text", dataValue.value())
    }

    @Test
    fun testDataValueGlobeCoordinateType() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":{"latitude":52.373,"longitude":4.893,"altitude":0.0,"precision":0.001,"globe":"http://www.wikidata.org/entity/Q2"},"type":"globecoordinate"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertTrue(dataValue.value().contains("52"))
        assertTrue(dataValue.value().contains("4"))
    }

    @Test
    fun testDataValueNullValueReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":null,"type":"string"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("", dataValue.value())
    }

    @Test
    fun testDataValueUnknownTypeReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":"some value","type":"unknown-type"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("", dataValue.value())
    }

    @Test
    fun testDataValueNoTypeReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":"some value"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("", dataValue.value())
    }

    @Test
    fun testDataValueEmptyString() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"value":"","type":"string"}"""
        val dataValue = json.decodeFromString<Claims.DataValue>(jsonStr)
        assertEquals("\"\"", dataValue.value())
    }

    @Test
    fun testMainSnakWithNullDataValue() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"snaktype":"value","property":"P31","hash":"hash123"}"""
        val mainSnak = json.decodeFromString<Claims.MainSnak>(jsonStr)
        assertNull(mainSnak.dataValue)
    }

    @Test
    fun testClaimWithNullMainSnak() {
        val json = Json { ignoreUnknownKeys = true }
        val jsonStr = """{"type":"statement","id":"id123","rank":"normal"}"""
        val claim = json.decodeFromString<Claims.Claim>(jsonStr)
        assertNull(claim.mainSnak)
    }
}
