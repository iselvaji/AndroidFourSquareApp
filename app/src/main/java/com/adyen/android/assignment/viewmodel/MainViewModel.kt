package com.adyen.android.assignment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.model.data.remote.api.model.ResponseWrapper
import com.adyen.android.assignment.model.data.remote.api.model.VenueRecommendationsResponse
import com.adyen.android.assignment.model.LocationLiveData
import com.adyen.android.assignment.model.repository.VenueRepository
import com.adyen.android.assignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: VenueRepository,
    application: Application
) : AndroidViewModel(application) {

    var response: MutableLiveData<Resource<ResponseWrapper<VenueRecommendationsResponse>>> = MutableLiveData()

    fun fetchVenue(currentLocation: LocationLiveData.LocationModel) = viewModelScope.launch {
        repository.getVenues(currentLocation, "").collect {
            response.value = it
        }
    }

    fun searchVenue(searchQuery : String) = viewModelScope.launch {
        repository.getVenues(LocationLiveData.LocationModel(0.0, 0.0), searchQuery).collect {
            response.value = it
        }
    }

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData

}


