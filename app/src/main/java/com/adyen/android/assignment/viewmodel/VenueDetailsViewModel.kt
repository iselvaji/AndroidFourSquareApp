package com.adyen.android.assignment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.model.data.remote.api.model.DetailsResponse
import com.adyen.android.assignment.model.repository.VenueRepository
import com.adyen.android.assignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor
    (private val repository: VenueRepository, application: Application) : AndroidViewModel(application) {

    var response: MutableLiveData<Resource<DetailsResponse>> = MutableLiveData()

    fun fetchVenueDetails(venueId : String) = viewModelScope.launch {
        repository.getVenueDetails(venueId).collect {
            response.value = it
        }
    }

}


