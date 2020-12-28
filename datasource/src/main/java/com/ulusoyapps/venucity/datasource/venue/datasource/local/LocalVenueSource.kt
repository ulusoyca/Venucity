package com.ulusoyapps.venucity.datasource.venue.datasource.local

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import kotlinx.coroutines.flow.Flow

interface LocalVenueSource {
    suspend fun addFavoriteVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage>
    suspend fun removeFavoriteVenue(venueId: String): Result<Unit, DataLayerVenueMessage>
    suspend fun getAllFavoriteVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>>
}