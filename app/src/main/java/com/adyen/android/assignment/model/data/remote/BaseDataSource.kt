package com.adyen.android.assignment.model.data.remote

import android.util.Log
import com.adyen.android.assignment.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {

    suspend fun <T> getResult(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                Log.i("response body", body.toString())
                body?.let {
                    return Resource.success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): Resource<T> =
        Resource.error("Api call failed $errorMessage")
}