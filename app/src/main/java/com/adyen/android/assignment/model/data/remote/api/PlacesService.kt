package com.adyen.android.assignment.model.data.remote.api


import com.adyen.android.assignment.model.data.remote.api.model.DetailsResponse
import com.adyen.android.assignment.model.data.remote.api.model.ResponseWrapper
import com.adyen.android.assignment.model.data.remote.api.model.VenueRecommendationsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface PlacesService {

    @GET("venues/explore")
    fun getVenueRecommendations(@QueryMap query: Map<String, String>): Call<ResponseWrapper<VenueRecommendationsResponse>>

    @GET("venues/{venue_id}")
    fun getVenueDetails(@Path("venue_id") venueId: String, @QueryMap query: Map<String, String>): Call<DetailsResponse>

    /*companion object  {

        val loggingInterceptor = HttpLoggingInterceptor()
        var httpClient = OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor).build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()
        }

        val instance: PlacesService by lazy { retrofit.create(PlacesService::class.java) }
    }*/
}
