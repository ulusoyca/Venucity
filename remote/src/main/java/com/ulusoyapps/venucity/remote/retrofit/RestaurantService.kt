package com.ulusoyapps.venucity.remote.retrofit

import com.ulusoyapps.venucity.remote.entities.Results
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {
    @GET("venues?")
    suspend fun getRestaurants(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Results
}
