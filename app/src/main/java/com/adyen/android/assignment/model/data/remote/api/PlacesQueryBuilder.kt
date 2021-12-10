package com.adyen.android.assignment.model.data.remote.api

import com.adyen.android.assignment.BuildConfig
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.mapOf
import kotlin.collections.set

abstract class PlacesQueryBuilder {
    private val baseQueryParams by lazy {
        mapOf(
            "client_id" to BuildConfig.CLIENT_ID.trim(),
            "client_secret" to BuildConfig.CLIENT_SECRET.trim()
        )
    }

    fun build(): Map<String, String> {
        val queryParams = HashMap(baseQueryParams)
        queryParams["v"] = dateFormat.format(Date())
        putQueryParams(queryParams)
        return queryParams
    }

    abstract fun putQueryParams(queryParams: MutableMap<String, String>)

    companion object {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.ROOT)
    }
}
