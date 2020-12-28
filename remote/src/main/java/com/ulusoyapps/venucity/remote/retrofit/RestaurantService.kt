package com.ulusoyapps.venucity.remote.retrofit

import com.ulusoyapps.venucity.remote.entities.Results
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {
    @GET("venues?lat=latitude&lon=longitude}")
    suspend fun getRestaurants(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Results
}
