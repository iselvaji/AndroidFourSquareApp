package com.adyen.android.assignment.model.data.remote.api

class VenueRecommendationsQueryBuilder : PlacesQueryBuilder() {
    private var latitudeLongitude: String? = null
    private var searchQuery: String? = null

    fun setLatitudeLongitude(latitude: Double, longitude: Double): VenueRecommendationsQueryBuilder {
        this.latitudeLongitude = "$latitude,$longitude"
        return this
    }

    fun setSearchQuery(searchQuery: String): VenueRecommendationsQueryBuilder {
        this.searchQuery = searchQuery
        return this
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        latitudeLongitude?.apply { queryParams["ll"] = this }
        searchQuery?.apply { queryParams["near"] = this }
    }
}
