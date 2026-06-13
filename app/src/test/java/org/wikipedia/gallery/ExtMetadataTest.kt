package org.wikipedia.gallery

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExtMetadataTest {

    @Test
    fun testLicenseShortName() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"LicenseShortName":{"value":"CC BY-SA 4.0","source":"commons","hidden":""}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("CC BY-SA 4.0", metadata.licenseShortName())
    }

    @Test
    fun testLicenseShortNameMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"Artist":{"value":"Test Artist"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.licenseShortName())
    }

    @Test
    fun testLicenseUrl() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"LicenseUrl":{"value":"https://creativecommons.org/licenses/by-sa/4.0/"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("https://creativecommons.org/licenses/by-sa/4.0/", metadata.licenseUrl())
    }

    @Test
    fun testLicenseUrlMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.licenseUrl())
    }

    @Test
    fun testLicense() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"License":{"value":"cc-by-sa-4.0"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("cc-by-sa-4.0", metadata.license())
    }

    @Test
    fun testLicenseMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.license())
    }

    @Test
    fun testImageDescription() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"ImageDescription":{"value":"A beautiful sunset"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("A beautiful sunset", metadata.imageDescription())
    }

    @Test
    fun testImageDescriptionMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.imageDescription())
    }

    @Test
    fun testDateTime() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"DateTimeOriginal":{"value":"2024-01-15 10:30:00"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("2024-01-15 10:30:00", metadata.dateTime())
    }

    @Test
    fun testDateTimeMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.dateTime())
    }

    @Test
    fun testArtist() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"Artist":{"value":"Jane Doe"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("Jane Doe", metadata.artist())
    }

    @Test
    fun testArtistMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.artist())
    }

    @Test
    fun testCredit() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{"Credit":{"value":"Museum Collection"}}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("Museum Collection", metadata.credit())
    }

    @Test
    fun testCreditMissingReturnsEmpty() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """{}"""
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("", metadata.credit())
    }

    @Test
    fun testAllFieldsDeserialized() {
        val json = Json { ignoreUnknownKeys = true }
        val response = """
        {
            "LicenseShortName":{"value":"CC BY 4.0"},
            "LicenseUrl":{"value":"https://example.com/license"},
            "License":{"value":"cc-by-4.0"},
            "ImageDescription":{"value":"Test image"},
            "DateTimeOriginal":{"value":"2024-06-01"},
            "Artist":{"value":"Artist Name"},
            "Credit":{"value":"Provided by museum"}
        }
        """.trimIndent()
        val metadata = json.decodeFromString<ExtMetadata>(response)
        assertEquals("CC BY 4.0", metadata.licenseShortName())
        assertEquals("https://example.com/license", metadata.licenseUrl())
        assertEquals("cc-by-4.0", metadata.license())
        assertEquals("Test image", metadata.imageDescription())
        assertEquals("2024-06-01", metadata.dateTime())
        assertEquals("Artist Name", metadata.artist())
        assertEquals("Provided by museum", metadata.credit())
    }
}
