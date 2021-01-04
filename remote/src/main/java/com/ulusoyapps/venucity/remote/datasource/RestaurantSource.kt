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

package com.ulusoyapps.venucity.remote.datasource

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessage
import com.ulusoyapps.venucity.datasource.venue.datasource.remote.RemoteVenueSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.remote.entities.HttpError
import com.ulusoyapps.venucity.remote.entities.NetworkError
import com.ulusoyapps.venucity.remote.entities.VenueRemoteFetchError
import com.ulusoyapps.venucity.remote.mapper.RemoteVenueMapper
import com.ulusoyapps.venucity.remote.mapper.RemoteVenueMessageMapper
import com.ulusoyapps.venucity.remote.retrofit.RestaurantService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RestaurantSource
@Inject constructor(
    private val venueMapper: RemoteVenueMapper,
    private val messageMapper: RemoteVenueMessageMapper,
    private val restaurantService: RestaurantService,
) : RemoteVenueSource {
    override suspend fun getNearbyVenues(
        latLng: DataLayerLatLng,
        maxAmount: Int
    ): Result<List<DataLayerVenue>, DataLayerMessage> {
        return try {
            val restaurants = restaurantService.getRestaurants(latLng.latDoubleValue, latLng.lngDoubleValue)
                .results
                .take(maxAmount)
            val dataRestaurants = venueMapper.mapToDataLayerEntityList(restaurants)
            Ok(dataRestaurants)
        } catch (throwable: Throwable) {
            val message = when (throwable) {
                is IOException -> NetworkError
                is HttpException -> HttpError
                else -> VenueRemoteFetchError
            }
            Err(messageMapper.mapToDataLayerEntity(message))
        }
    }
}
