package com.ulusoyapps.venucity.remote.retrofit

import com.ulusoyapps.venucity.remote.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT_IN_MINUTES = 30L
private const val READ_TIMEOUT_IN_MINUTES = 30L
private const val WOLT_BASE_LIBRARY = "https://restaurant-api.wolt.fi/v3/"

object ServiceFactory {

    fun makeRestaurantService(): RestaurantService {
        return Retrofit.Builder()
            .client(makeOkHttpClient())
            .baseUrl(WOLT_BASE_LIBRARY)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RestaurantService::class.java)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
            .readTimeout(READ_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
            .build()
    }
}
