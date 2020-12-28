package com.ulusoyapps.venucity.datasource.venue.datasource

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.domain.entities.*
import kotlinx.coroutines.flow.Flow

interface VenueDataSource {
    suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage>
    suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage>
    suspend fun getAllVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>>
    suspend fun getNearbyVenues(latLng: LatLng, maxAmount: Int): Result<List<DataLayerVenue>, DataLayerVenueMessage>
}