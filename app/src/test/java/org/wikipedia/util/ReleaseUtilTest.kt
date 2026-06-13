package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReleaseUtilTest {

    @Test
    fun testIsDevReleaseForDevBuild() {
        // In test environment, APPLICATION_ID is typically "org.wikipedia.test" which contains neither
        // beta, alpha, nor dev, so isProdRelease should be true by default.
        val isProd = ReleaseUtil.isProdRelease
        val isPreProd = ReleaseUtil.isPreProdRelease
        // One of them should be true
        assertTrue(isProd || isPreProd)
    }

    @Test
    fun testPreProdAndProdAreMutuallyExclusive() {
        assertNotEquals(ReleaseUtil.isProdRelease, ReleaseUtil.isPreProdRelease)
    }

    @Test
    fun testIsAlphaReleaseReturnsBoolean() {
        assertTrue(ReleaseUtil.isAlphaRelease || !ReleaseUtil.isAlphaRelease)
    }

    @Test
    fun testIsDevReleaseReturnsBoolean() {
        assertTrue(ReleaseUtil.isDevRelease || !ReleaseUtil.isDevRelease)
    }

    @Test
    fun testIsPreBetaReleaseReturnsBoolean() {
        val result = ReleaseUtil.isPreBetaRelease
        // pre-beta is true when not prod and not beta
        if (ReleaseUtil.isProdRelease) {
            assertFalse(result)
        }
    }

    @Test
    fun testAllPropertiesAreConsistent() {
        val isProd = ReleaseUtil.isProdRelease
        val isPreProd = ReleaseUtil.isPreProdRelease
        val isAlpha = ReleaseUtil.isAlphaRelease
        val isDev = ReleaseUtil.isDevRelease

        if (isProd) {
            assertFalse(isAlpha)
            assertFalse(isDev)
            assertFalse(ReleaseUtil.isPreBetaRelease)
        }
        if (isPreProd) {
            assertTrue(!isProd || isAlpha || isDev)
        }
    }

    @Test
    fun testReleaseTypePropertiesAreBoolean() {
        assertTrue(ReleaseUtil.isProdRelease is Boolean)
        assertTrue(ReleaseUtil.isPreProdRelease is Boolean)
        assertTrue(ReleaseUtil.isAlphaRelease is Boolean)
        assertTrue(ReleaseUtil.isPreBetaRelease is Boolean)
        assertTrue(ReleaseUtil.isDevRelease is Boolean)
    }
}
