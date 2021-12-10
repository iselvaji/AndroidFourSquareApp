package com.adyen.android.assignment.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.R
import com.adyen.android.assignment.model.data.remote.api.model.Venue
import com.adyen.android.assignment.databinding.ListItemVenueBinding
import com.adyen.android.assignment.model.LocationLiveData
import com.adyen.android.assignment.utils.LocationUtils
import com.bumptech.glide.Glide
import javax.inject.Inject
import kotlin.math.roundToInt

class VenuesListAdapter @Inject constructor() : RecyclerView.Adapter<VenuesListAdapter.VenueViewHolder>(){

    private var data = ArrayList<Venue>()
    var onItemClick: ((item: Venue, view: View) -> Unit)? = null
    private lateinit var currentLocation : LocationLiveData.LocationModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemVenueBinding.inflate(inflater, parent, false)
        return VenueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateResults(newList: List<Venue>, currentLocation : LocationLiveData.LocationModel) {
        this.currentLocation = currentLocation
        val diffCallback = DiffCallback(data, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data.clear()
        data.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class VenueViewHolder(private var binding: ListItemVenueBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(venue: Venue) {
            binding.locationName.text = venue.name
            binding.locationCategory.text = venue.categories[0].name
            Glide.with(itemView.context).load(buildIconPath(venue)).into(binding.locationImage)

            venue?.location?.apply {
                currentLocation?.let {

                    val distanceInMeters = LocationUtils.distanceInMeters(
                        currentLocation.latitude, currentLocation.longitude,
                        venue.location.lat, venue.location.lng
                    )

                    if (distanceInMeters >= 1609.34) {
                        val miles = (distanceInMeters / 1609.34) //convert to miles
                        if(miles > 0)
                            binding.locationDistance.text = itemView.context.resources.getQuantityString(
                                R.plurals.miles, miles.roundToInt(), String.format("%.1f", miles))
                    } else {
                        val feet = (distanceInMeters / 3.28084).roundToInt() //convert to feet
                        if(feet > 0)
                            binding.locationDistance.text = itemView.context.resources.getQuantityString(R.plurals.feet, feet, feet.toString())
                    }

                }
            }

            //send the click event to the listener
            binding.root.setOnClickListener{
                venue.id?.let {
                    onItemClick?.invoke(data[adapterPosition], itemView)
                }
            }
        }

        private fun buildIconPath(venue: Venue): String {
            venue.categories?.firstOrNull()?.icon?.let{
                if (it.prefix.isNotBlank() && it.suffix.isNotBlank()) {
                    return it.prefix + "88" + it.suffix
                }
            }
            return ""
        }

    }

    inner class DiffCallback(private val oldList: ArrayList<Venue>, private val newList: List<Venue>) : DiffUtil.Callback() {

        override fun getOldListSize() : Int = oldList.size

        override fun getNewListSize() : Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            val oldVersion = if (oldItemPosition < oldList.size) oldList[oldItemPosition] else null
            val newVersion = if (newItemPosition < newList.size) newList[newItemPosition] else null

            return oldVersion?.name == newVersion?.name &&
                    oldVersion?.id == newVersion?.id
        }
    }
}

