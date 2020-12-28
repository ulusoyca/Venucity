package com.ulusoyapps.venucity.datasource.venue.datasource.remote

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteVenueDataSource
@Inject constructor(
    private val remoteVenueSource: RemoteVenueSource
) : VenueDataSource {
    override suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage> {
        throw NotImplementedError()
    }

    override suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage> {
        throw NotImplementedError()
    }

    override suspend fun getAllVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>> {
        throw NotImplementedError()
    }

    override suspend fun getNearbyVenues(
        latLng: LatLng,
        maxAmount: Int
    ): Result<List<DataLayerVenue>, DataLayerVenueMessage> {
        throw NotImplementedError()
    }
}