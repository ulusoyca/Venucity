package com.ulusoyapps.venucity.datasource.venue.datasource.remote

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue

interface RemoteVenueSource {
    suspend fun getNearbyVenues(
        latLng: DataLayerLatLng,
        maxAmount: Int
    ): Result<List<DataLayerVenue>, DataLayerMessage>
}