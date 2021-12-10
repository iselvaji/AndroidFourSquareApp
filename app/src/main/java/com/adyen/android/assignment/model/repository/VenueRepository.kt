package com.adyen.android.assignment.model.repository

import com.adyen.android.assignment.model.data.remote.api.model.DetailsResponse
import com.adyen.android.assignment.model.data.remote.api.model.ResponseWrapper
import com.adyen.android.assignment.model.data.remote.api.model.VenueRecommendationsResponse
import com.adyen.android.assignment.model.LocationLiveData
import com.adyen.android.assignment.model.data.remote.RemoteDataSource
import com.adyen.android.assignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VenueRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource) {

    fun getVenues(location : LocationLiveData.LocationModel, searchQuery: String): Flow<Resource<ResponseWrapper<VenueRecommendationsResponse>>> {
        return flow {
            emit(remoteDataSource.getVenues(location, searchQuery))
        }.flowOn(Dispatchers.IO)
    }

    fun getVenueDetails(venueId: String): Flow<Resource<DetailsResponse>> {
        return flow {
            emit(remoteDataSource.getVenueDetails(venueId))
        }.flowOn(Dispatchers.IO)
    }
}