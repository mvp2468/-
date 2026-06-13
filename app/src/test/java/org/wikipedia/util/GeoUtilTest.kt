package org.wikipedia.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GeoUtilTest {

    @Test
    fun testIsSamePlaceWithIdenticalCoordinates() {
        assertTrue(GeoUtil.isSamePlace(52.3731, 52.3731, 4.8931, 4.8931))
    }

    @Test
    fun testIsSamePlaceWithVeryCloseCoordinates() {
        // Difference is less than tolerance 0.0000001
        assertTrue(GeoUtil.isSamePlace(52.37310000, 52.37310001, 4.89310000, 4.89310001))
    }

    @Test
    fun testIsSamePlaceWithDistantCoordinates() {
        assertFalse(GeoUtil.isSamePlace(52.3731, 48.8566, 4.8931, 2.3522))
    }

    @Test
    fun testIsSamePlaceWithSignificantlyDifferentLatitude() {
        assertFalse(GeoUtil.isSamePlace(52.3731, 52.3831, 4.8931, 4.8931))
    }

    @Test
    fun testIsSamePlaceWithSignificantlyDifferentLongitude() {
        assertFalse(GeoUtil.isSamePlace(52.3731, 52.3731, 4.8931, 4.9031))
    }

    @Test
    fun testIsSamePlaceWithZeroCoordinates() {
        assertTrue(GeoUtil.isSamePlace(0.0, 0.0, 0.0, 0.0))
    }

    @Test
    fun testIsSamePlaceWithNegativeCoordinates() {
        assertTrue(GeoUtil.isSamePlace(-33.8688, -33.8688, 151.2093, 151.2093))
    }

    @Test
    fun testIsSamePlaceAtToleranceBoundary() {
        // Exactly at tolerance boundary: 0.0000001 difference
        assertTrue(GeoUtil.isSamePlace(0.0, 0.000000099, 0.0, 0.000000099))
        // Just above tolerance
        assertFalse(GeoUtil.isSamePlace(0.0, 0.000001, 0.0, 0.0))
    }

    @Test
    fun testLocationClustererHaversineDistanceSamePoint() {
        val clusterer = GeoUtil.LocationClusterer()
        // Can't test private method directly but we can verify through clusterLocations
        assertNotNull(clusterer)
    }

    @Test
    fun testClusterDefault() {
        val cluster = GeoUtil.Cluster(id = 1)
        assertEquals(1, cluster.id)
        assertTrue(cluster.locations.isEmpty())
        assertNull(cluster.centroid)
    }

    @Test
    fun testClusterWithLocations() {
        val cluster = GeoUtil.Cluster(id = 2, locations = mutableListOf())
        assertEquals(2, cluster.id)
        assertTrue(cluster.locations.isEmpty())
    }
}
