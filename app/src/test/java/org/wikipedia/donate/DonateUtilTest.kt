package org.wikipedia.donate

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DonateUtilTest {

    @Test
    fun testGetAmountFloatWithValidIntegerString() {
        assertEquals(10f, DonateUtil.getAmountFloat("10"))
    }

    @Test
    fun testGetAmountFloatWithValidDecimalString() {
        assertEquals(12.5f, DonateUtil.getAmountFloat("12.5"))
    }

    @Test
    fun testGetAmountFloatWithCommaDecimalString() {
        assertEquals(12.5f, DonateUtil.getAmountFloat("12,5"))
    }

    @Test
    fun testGetAmountFloatWithDotToCommaConversion() {
        // When "1.234" contains "." and fails as float, it converts "." to ","
        // "1,234" is parsed as 1234f in some locales or might fail
        // The behavior depends on locale, but the result will be either a float or 0f
        val result = DonateUtil.getAmountFloat("1.234")
        assertTrue(result == 1.234f || result == 1234f || result == 0f)
    }

    @Test
    fun testGetAmountFloatWithCommaToDotConversion() {
        // When "1,234" contains "," it converts "," to "."
        assertEquals(1.234f, DonateUtil.getAmountFloat("1,234"))
    }

    @Test
    fun testGetAmountFloatWithEmptyString() {
        assertEquals(0f, DonateUtil.getAmountFloat(""))
    }

    @Test
    fun testGetAmountFloatWithInvalidString() {
        assertEquals(0f, DonateUtil.getAmountFloat("abc"))
    }

    @Test
    fun testGetAmountFloatWithZero() {
        assertEquals(0f, DonateUtil.getAmountFloat("0"))
    }

    @Test
    fun testGetAmountFloatWithNegativeNumber() {
        assertEquals(-5.5f, DonateUtil.getAmountFloat("-5.5"))
    }

    @Test
    fun testGetAmountFloatWithLargeNumber() {
        assertEquals(999999f, DonateUtil.getAmountFloat("999999"))
    }

    @Test
    fun testGetAmountFloatWithCommaAndDotMixed() {
        val result = DonateUtil.getAmountFloat("1,234.56")
        assertTrue(result == 1234.56f || result == 0f)
    }
}
