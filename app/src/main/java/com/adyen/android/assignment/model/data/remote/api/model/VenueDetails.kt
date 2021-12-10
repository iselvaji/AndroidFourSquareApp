package com.adyen.android.assignment.model.data.remote.api.model

class VenueDetail(
    var id: String,
    var name: String,
    var location: LocationDetail,
    var canonicalUrl: String,
    var categories: List<CategoryDetail>,
    var verified: Boolean,
    var url: String,
    var rating: Double,
    var ratingColor: String,
    var ratingSignals: Int,
    var shortUrl: String,
    var timeZone: String,
    var hours: Hours,
    var contact: Contact
)

class LocationDetail(
    var lat: Double,
    var lng: Double,
    var formattedAddress: List<String> = ArrayList()
)

class CategoryDetail(
    var shortName: String
)
