package com.ulusoyapps.venucity.datasource.venue.datasource

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import kotlinx.coroutines.flow.Flow

interface VenueDataSource {
    suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage>
    suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage>
    suspend fun getAllVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>>
}