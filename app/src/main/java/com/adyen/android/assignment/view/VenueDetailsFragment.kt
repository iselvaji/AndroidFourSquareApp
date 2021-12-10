package com.adyen.android.assignment.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.adyen.android.assignment.databinding.FragmentVenueDetailsBinding
import com.adyen.android.assignment.utils.Constants
import com.adyen.android.assignment.utils.DialogUtils
import com.adyen.android.assignment.utils.LocationUtils
import com.adyen.android.assignment.utils.Resource
import com.adyen.android.assignment.viewmodel.VenueDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_venue_details.*

@AndroidEntryPoint
class VenueDetailsFragment : BaseFragment() {

    private val logTag = "VenueDetailsFragment"
    private var binding: FragmentVenueDetailsBinding? = null
    private val viewModel by viewModels<VenueDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVenueDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setUpObservers()
    }

    private fun setupUI() {
        DialogUtils.showProgressDialog(context, true)
        if (arguments != null) {
            val venueId: String? = arguments!!.getString(Constants.KEY_VENUE)
            if(venueId != null) {
                fetchVenueDetails(venueId)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.response.observe(viewLifecycleOwner, {

            Log.d(logTag, "Venue Details Api response status : " + it.status)
            Log.d(logTag, "Venue Details Api response : " + it.data)

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    DialogUtils.showProgressDialog(context, false)

                    it.data?.let {  detailsResponse ->
                        detailsResponse.response.let { response ->
                            response.venue.let { venueDetail ->

                                binding?.apply {
                                    detailsName.text = venueDetail.name
                                    details_rating.text = LocationUtils.formatRatings(venueDetail)
                                    detailsRatingBar.rating = LocationUtils.ratingBar(venueDetail)
                                    details_category.text = LocationUtils.category(venueDetail)
                                    details_address.text = LocationUtils.address(venueDetail)
                                    detailsPhone.text = LocationUtils.phone(venueDetail)
                                    detailsHours.text = venueDetail.hours?.status
                                }
                            }
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
        })
    }

    private fun fetchVenueDetails(venueId : String) {
        if(canMakeApiCall()) {
            Log.d(logTag, "getVenueDetails for $venueId")
            viewModel.fetchVenueDetails(venueId)
        } else {
            DialogUtils.showProgressDialog(context, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}