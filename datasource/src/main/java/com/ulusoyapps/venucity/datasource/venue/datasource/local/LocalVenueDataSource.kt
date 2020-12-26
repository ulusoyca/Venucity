package com.ulusoyapps.venucity.datasource.venue.datasource.local

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMapper
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMessageMapper
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMapper
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMessageMapper
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalVenueDataSource
@Inject constructor(
    private val localVenueSource: LocalVenueSource
) : VenueDataSource {
    override suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage> {
        return localVenueSource.addVenue(venue = venue)
    }

    override suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage> {
        return localVenueSource.removeVenue(venueId)
    }

    override suspend fun getAllVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>> {
        return localVenueSource.getAllVenues()
    }
}