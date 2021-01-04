/*
 *  Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
