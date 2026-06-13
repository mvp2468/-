package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DimenUtilTest {

    @Test
    fun testHtmlPxToIntWithPxSuffix() {
        assertEquals(100, DimenUtil.htmlPxToInt("100px"))
    }

    @Test
    fun testHtmlPxToIntWithoutPxSuffix() {
        assertEquals(200, DimenUtil.htmlPxToInt("200"))
    }

    @Test
    fun testHtmlPxToIntWithSpaceAndPx() {
        // Contains "px" so strip and parse
        assertEquals(50, DimenUtil.htmlPxToInt("50px"))
    }

    @Test
    fun testHtmlPxToIntWithLargeValue() {
        assertEquals(1920, DimenUtil.htmlPxToInt("1920px"))
    }

    @Test
    fun testHtmlPxToIntWithZero() {
        assertEquals(0, DimenUtil.htmlPxToInt("0px"))
        assertEquals(0, DimenUtil.htmlPxToInt("0"))
    }

    @Test
    fun testHtmlPxToIntWithNegativeValue() {
        assertEquals(-50, DimenUtil.htmlPxToInt("-50px"))
    }

    @Test
    fun testHtmlPxToIntWithInvalidString() {
        assertEquals(0, DimenUtil.htmlPxToInt("abc"))
    }

    @Test
    fun testHtmlPxToIntWithEmptyString() {
        assertEquals(0, DimenUtil.htmlPxToInt(""))
    }

    @Test
    fun testHtmlPxToIntWithPartialMatch() {
        // "px100": after replacing "px" with "", becomes "100", parsed to 100
        assertEquals(100, DimenUtil.htmlPxToInt("px100"))
    }
}
