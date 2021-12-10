package com.adyen.android.assignment.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adyen.android.assignment.R
import com.adyen.android.assignment.model.data.remote.api.model.Venue
import com.adyen.android.assignment.databinding.FragmentVenueListBinding
import com.adyen.android.assignment.model.LocationLiveData
import com.adyen.android.assignment.utils.Constants.Companion.KEY_VENUE
import com.adyen.android.assignment.utils.DialogUtils
import com.adyen.android.assignment.utils.PermissionUtils
import com.adyen.android.assignment.utils.Resource
import com.adyen.android.assignment.view.adapter.VenuesListAdapter
import com.adyen.android.assignment.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class VenueListFragment : BaseFragment() {

    @Inject
    lateinit var adapter: VenuesListAdapter
    private var binding: FragmentVenueListBinding? = null
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var textChangeCountDownJob: Job
    private val logTag = "VenueListFragment"
    private lateinit var currentLocation : LocationLiveData.LocationModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVenueListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setUpObservers()
        setupSearchView()
    }

    private fun setupUI() {

        binding?.recyclerviewVenue?.adapter = adapter

        adapter.onItemClick = { selectedItem, _ ->
            Log.d(logTag, "Venues OnItem Click : " + selectedItem.id)
            val bundle = Bundle()
            bundle.putString(KEY_VENUE, selectedItem.id)
            findNavController().navigate(R.id.list_to_details_view, bundle)
        }

        DialogUtils.showProgressDialog(context, true)
    }

    private fun setupSearchView() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.length > 2) {
                    if(::textChangeCountDownJob.isInitialized)
                        textChangeCountDownJob.cancel()

                    runBlocking {
                        textChangeCountDownJob = launch {
                            delay(500)
                            searchVenueData(newText)
                        }
                    }
                }
                return true
            }
        })
    }

    private fun setUpObservers() {

        viewModel.response.observe(viewLifecycleOwner, {
            Log.d(logTag, "Venue API response : " + it.data)
            Log.d(logTag, "Venue API response status : " + it.status)
            when (it.status) {
                Resource.Status.SUCCESS -> {
                   DialogUtils.showProgressDialog(context, false)

                    if (it.data != null) {
                        if (it.data.response.totalResults == 0) {
                            context?.let { context ->
                                DialogUtils.showDialogWithOKButton(context, getString(R.string.error_no_results_retry))
                            }
                        } else {
                            val venueList = mutableListOf<Venue>()
                            for (group in it.data.response?.groups!!) {
                                for (recommend in group.items) {
                                    venueList.add(recommend.venue)
                                }
                            }
                            if(this::currentLocation.isInitialized)
                                adapter.updateResults(venueList, currentLocation)
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    DialogUtils.showProgressDialog(context, false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                Resource.Status.LOADING ->
                    DialogUtils.showProgressDialog(context, true)
                }
            }
        )
    }

    private fun fetchVenueData(locationModel: LocationLiveData.LocationModel) {
        if(canMakeApiCall()) {
            viewModel.fetchVenue(locationModel)
        }
    }

    private fun searchVenueData(textToSearch : String) {
        if(canMakeApiCall()) {
            viewModel.searchVenue(textToSearch)
        } else {
            DialogUtils.showProgressDialog(context, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            PermissionUtils.isLocationPermissionGranted(requireContext()) -> startLocationUpdate()
            PermissionUtils.shouldShowRequestPermissionRationale(requireActivity()) -> {
                requestLocationPermissions.launch(PermissionUtils.locationPermissions)
            }
            else -> activity?.let {
                Log.d(logTag, "requestLocationPermissions called..")
                requestLocationPermissions.launch(PermissionUtils.locationPermissions)
            }
        }
    }

    private val requestLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[ACCESS_COARSE_LOCATION] == true
                || permissions[ACCESS_FINE_LOCATION] == true) {
                Log.d(logTag, "Location Permission granted")
                startLocationUpdate()
            } else {
                Log.d(logTag, "Location Permission not granted")
                displayLocationPermissionAlert(context)
            }
        }

    private fun startLocationUpdate() {
        if(PermissionUtils.isLocationEnabled(requireContext())) {
            Log.d(logTag, "startLocationUpdate begins..")
            viewModel.getLocationData().observe(this, {
                Log.d(logTag, "Location : " + it.latitude.toString() + " " + it.longitude.toString())
                currentLocation = LocationLiveData.LocationModel(it.longitude, it.latitude)
                fetchVenueData(currentLocation)
            })
        } else {
            Log.d(logTag, "LocationEnabled false..")
            displayLocationSettingsAlert(context)
        }
    }


    private fun displayLocationSettingsAlert(context: Context?) {

        context?.let {
            AlertDialog.Builder(context)
                .setMessage(R.string.error_closing_no_location_permission)
                .setPositiveButton(R.string.enable) { _, _ ->
                    Log.d(logTag, "GPS Enable selected")
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    private fun displayLocationPermissionAlert(context: Context?) {

        context?.let {
            AlertDialog.Builder(context)
                .setMessage(R.string.error_location_permission_denied)
                .setPositiveButton(R.string.enable) { _, _ ->
                    Log.d(logTag, "Location Permission Enable selected")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    startActivity(intent)
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }
}
