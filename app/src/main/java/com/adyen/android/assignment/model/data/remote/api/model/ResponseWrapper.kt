package com.adyen.android.assignment.model.data.remote.api.model

data class ResponseWrapper<T>(
    val meta: Meta,
    val response: T
)
