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
