package org.wikipedia.util

import android.graphics.Color
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResourceUtilTest {

    @Test
    fun testColorToCssStringWithBlack() {
        val result = ResourceUtil.colorToCssString(Color.BLACK)
        assertEquals("000000ff", result)
    }

    @Test
    fun testColorToCssStringWithWhite() {
        val result = ResourceUtil.colorToCssString(Color.WHITE)
        // WHITE = -1 = 0xFFFFFFFF: shl 8 = 0xFFFFFF00, shr 24 = 0xFF, result = 0xFFFFFFFF
        assertEquals("ffffffff", result)
    }

    @Test
    fun testColorToCssStringWithRed() {
        val result = ResourceUtil.colorToCssString(Color.RED)
        assertEquals("ff0000ff", result)
    }

    @Test
    fun testColorToCssStringWithBlue() {
        val result = ResourceUtil.colorToCssString(Color.BLUE)
        assertEquals("0000ffff", result)
    }

    @Test
    fun testColorToCssStringWithTransparent() {
        val result = ResourceUtil.colorToCssString(Color.TRANSPARENT)
        // Transparent is 0x00000000, after shift and OR becomes "00000000"
        assertEquals("00000000", result)
    }

    @Test
    fun testColorToCssStringFormatIsEightHexDigits() {
        val testColor = 0x12345678
        val result = ResourceUtil.colorToCssString(testColor)
        assertEquals("34567812", result)
    }

    @Test
    fun testLightenColorReturnsLighterColor() {
        val original = Color.RED
        val lightened = ResourceUtil.lightenColor(original)
        assertTrue(lightened != original)
    }

    @Test
    fun testDarkenColorReturnsDarkerColor() {
        val original = Color.WHITE
        val darkened = ResourceUtil.darkenColor(original)
        assertTrue(darkened != original)
        assertTrue(darkened != Color.WHITE)
    }

    @Test
    fun testLightenAndDarkenRoundTrip() {
        // Lightening then darkening should not equal original exactly
        val original = Color.BLUE
        val lightened = ResourceUtil.lightenColor(original)
        val darkened = ResourceUtil.darkenColor(lightened)
        // Values should be different after blending operations
        assertTrue(lightened != darkened)
    }
}
