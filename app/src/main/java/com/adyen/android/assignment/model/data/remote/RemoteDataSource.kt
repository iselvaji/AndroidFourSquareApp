package com.adyen.android.assignment.model.data.remote

import com.adyen.android.assignment.model.data.remote.api.PlacesService
import com.adyen.android.assignment.model.data.remote.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.model.LocationLiveData
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val placesService: PlacesService
): BaseDataSource() {

    suspend fun getVenues(location : LocationLiveData.LocationModel, searchQuery: String) = getResult {
        val query = VenueRecommendationsQueryBuilder().
        setLatitudeLongitude(location.latitude, location.longitude).
        setSearchQuery(searchQuery).build()
        placesService.getVenueRecommendations(query).execute()
    }

    suspend fun getVenueDetails(venueId: String) = getResult {
        val query = VenueRecommendationsQueryBuilder().build()
        placesService.getVenueDetails(venueId, query).execute()
    }
}