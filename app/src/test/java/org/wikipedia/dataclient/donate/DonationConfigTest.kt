package org.wikipedia.dataclient.donate

import org.junit.Assert.*
import org.junit.Test

class DonationConfigTest {

    @Test
    fun testDefaultValues() {
        val config = DonationConfig(version = 1)
        assertEquals(1, config.version)
        assertTrue(config.currencyMinimumDonation.isEmpty())
        assertTrue(config.currencyMaximumDonation.isEmpty())
        assertTrue(config.currencyAmountPresets.isEmpty())
        assertTrue(config.currencyTransactionFees.isEmpty())
        assertTrue(config.countryCodeEmailOptInRequired.isEmpty())
        assertTrue(config.countryCodeGooglePayEnabled.isEmpty())
    }

    @Test
    fun testConstructorWithValues() {
        val config = DonationConfig(
            version = 2,
            currencyMinimumDonation = mapOf("USD" to 1f),
            currencyMaximumDonation = mapOf("USD" to 25000f),
            currencyAmountPresets = mapOf("USD" to listOf(5f, 10f, 20f, 50f, 100f)),
            currencyTransactionFees = mapOf("USD" to 0.3f),
            countryCodeEmailOptInRequired = listOf("US", "GB"),
            countryCodeGooglePayEnabled = listOf("US")
        )
        assertEquals(2, config.version)
        assertEquals(1f, config.currencyMinimumDonation["USD"])
        assertEquals(25000f, config.currencyMaximumDonation["USD"])
        assertEquals(5, config.currencyAmountPresets["USD"]?.size)
        assertEquals(0.3f, config.currencyTransactionFees["USD"])
        assertTrue(config.countryCodeEmailOptInRequired.contains("US"))
        assertTrue(config.countryCodeGooglePayEnabled.contains("US"))
    }
}
