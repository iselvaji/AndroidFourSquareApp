package com.adyen.android.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.model.data.remote.api.PlacesService
import com.adyen.android.assignment.model.data.remote.api.VenueRecommendationsQueryBuilder
import okhttp3.OkHttpClient
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(JUnit4::class)
class PlacesUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var placesService: PlacesService

    @Before
    fun setUp() {

        val okHttpClient = OkHttpClient.Builder().build()

        placesService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
            .client(okHttpClient)
            .build().create(PlacesService::class.java)

    }

    @Test
     fun testResponseCode() {
         val query = VenueRecommendationsQueryBuilder()
             .setLatitudeLongitude(52.376510, 4.905890)
             .build()

         val response = placesService
             .getVenueRecommendations(query)
             .execute()

         val errorBody = response.errorBody()
         assertNull("Received an error: ${errorBody?.string()}", errorBody)

         val responseWrapper = response.body()
         assertNotNull("Response is null.", responseWrapper)
         assertEquals("Response code", 200, responseWrapper!!.meta.code)
     }
}
