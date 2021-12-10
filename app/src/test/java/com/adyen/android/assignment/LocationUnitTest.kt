package com.adyen.android.assignment

import com.adyen.android.assignment.utils.LocationUtils
import org.junit.Assert
import org.junit.Test

class LocationUnitTest {

    @Test
    fun testDistanceBetweenLocations() {

        val currentLat = 11.35135135135135
        val currentLng = 77.73685418735407

        val destLat = 11.337098329975104
        val destLng = 77.71773563126426

        val distance = LocationUtils.distanceInMeters(currentLat, currentLng, destLat, destLng)

        Assert.assertEquals(distance, 2618.331836934674, 0.002)
    }
}