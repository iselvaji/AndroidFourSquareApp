package com.adyen.android.assignment.model.data.remote.api.model

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String,
    val popularityByGeo: Double,
    val venuePage: VenuePage
)
