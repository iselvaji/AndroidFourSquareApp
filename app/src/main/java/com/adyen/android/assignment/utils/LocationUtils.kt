package com.adyen.android.assignment.utils

import com.adyen.android.assignment.model.data.remote.api.model.VenueDetail
import java.text.DecimalFormat

object LocationUtils {

    fun formatRatings(venueDetail: VenueDetail): String {
        return DecimalFormat("#.##").format(venueDetail.rating?.div(2)?:0) ?: "0"
    }

    fun ratingBar(venueDetail: VenueDetail): Float {
        return venueDetail.rating?.div(2)?.toFloat()?:0f
    }

    fun address(venueDetail: VenueDetail) : String {
        if (venueDetail.location == null || venueDetail.location?.formattedAddress.isNullOrEmpty()) {
            return ""
        }

        var addressString = ""
        venueDetail.location?.formattedAddress.let {
            it?.forEach { s ->
                addressString += s + System.getProperty("line.separator")
            }
        }

        return addressString
    }

    fun category(venueDetail: VenueDetail) : String {
        return venueDetail.categories?.get(0)?.shortName?:""
    }

    fun phone(venueDetail: VenueDetail) : String {
        val contact = venueDetail.contact
        return contact?.formattedPhone?:""
    }

    fun distanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) *
                Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        return dist * 1000
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}
