package com.adyen.android.assignment.model.data.remote.api.model

data class Reasons(
    val count: Int,
    val items: List<Item>
) {
    data class Item(
        val reasonName: String,
        val summary: String,
        val type: String
    )
}
