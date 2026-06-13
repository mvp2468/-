package org.wikipedia.gallery

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageLicenseTest {

    @Test
    fun testLicenseIconForCCLicense() {
        val license = ImageLicense(licenseName = "CC BY 4.0", licenseShortName = "CC BY 4.0")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testLicenseIconForPDLicense() {
        val license = ImageLicense(licenseName = "PD", licenseShortName = "PD")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testLicenseIconForCC0() {
        val license = ImageLicense(licenseName = "CC0", licenseShortName = "CC0")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testLicenseIconForCCBySaLicense() {
        val license = ImageLicense(licenseName = "CC BY-SA 4.0", licenseShortName = "CC BY-SA 4.0")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testLicenseIconForUnknownLicense() {
        val license = ImageLicense(licenseName = "SomeOtherLicense", licenseShortName = "unknown")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testLicenseIconForEmptyStrings() {
        val license = ImageLicense()
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testCCLicenseDetectedByShortName() {
        val license = ImageLicense(licenseName = "anything", licenseShortName = "CC BY 4.0")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testPDLicenseDetectedByShortName() {
        val license = ImageLicense(licenseName = "anything", licenseShortName = "PD-old")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testCCLicenseCaseInsensitive() {
        val license = ImageLicense(licenseName = "cc by 4.0", licenseShortName = "cc by 4.0")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testPDLicenseCaseInsensitive() {
        val license = ImageLicense(licenseName = "pd-old", licenseShortName = "pd-old")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testCCBySaWithHyphens() {
        val license = ImageLicense(licenseName = "CC-BY-SA 4.0", licenseShortName = "CC-BY-SA 4.0")
        assertNotEquals(0, license.licenseIcon())
    }

    @Test
    fun testConstructorFromExtMetadata() {
        val metadata = ExtMetadata(
            objectName = null,
            imageDescription = null,
            dateTimeOriginal = null,
            artist = null,
            credit = null,
            licenseShortName = ExtMetadata.Values("CC BY-SA 4.0"),
            usageTerms = null,
            licenseUrl = ExtMetadata.Values("https://example.com"),
            license = ExtMetadata.Values("cc-by-sa-4.0")
        )
        val license = ImageLicense(metadata)
        assertEquals("cc-by-sa-4.0", license.licenseName)
        assertEquals("CC BY-SA 4.0", license.licenseShortName)
        assertEquals("https://example.com", license.licenseUrl)
    }

    @Test
    fun testDefaultConstructorEmptyStrings() {
        val license = ImageLicense()
        assertEquals("", license.licenseName)
        assertEquals("", license.licenseShortName)
        assertEquals("", license.licenseUrl)
    }
}
