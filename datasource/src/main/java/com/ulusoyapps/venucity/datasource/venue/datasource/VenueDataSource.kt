package com.ulusoyapps.venucity.datasource.venue.datasource

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessage
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.domain.entities.*
import kotlinx.coroutines.flow.Flow

interface VenueDataSource {
    suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage>
    suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage>
    suspend fun getAllFavoriteVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>>
    suspend fun getNearbyVenues(latLng: DataLayerLatLng, maxAmount: Int): Result<List<DataLayerVenue>, DataLayerMessage>
}