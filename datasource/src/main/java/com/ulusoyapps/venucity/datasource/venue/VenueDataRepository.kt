package com.ulusoyapps.venucity.datasource.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueDataSource
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMapper
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMessageMapper
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VenueDataRepository(
    private val localVenueDataSource: VenueDataSource,
    private val venueMapper: VenueMapper,
    private val venueMessageMapper: VenueMessageMapper,
): VenueRepository {
    override suspend fun addVenue(venue: Venue): Result<Unit, VenueMessage> {
        val dataLayerVenue = venueMapper.mapToDataLayerEntity(venue)
        return localVenueDataSource.addVenue(dataLayerVenue).mapBoth(
            success = { Ok(Unit) },
            failure = { message ->
                Err(
                    venueMessageMapper.mapToDomainEntity(message)
                )
            }
        )
    }

    override suspend fun removeVenue(venueId: String): Result<Unit, VenueMessage> {
        return localVenueDataSource.removeVenue(venueId).mapBoth(
            success = { Ok(Unit) },
            failure = { message ->
                Err(venueMessageMapper.mapToDomainEntity(message))
            }
        )
    }

    override suspend fun getAllVenues(): Flow<Result<List<Venue>, VenueMessage>> {
        return localVenueDataSource.getAllVenues()
            .map {
                it.mapBoth(
                    success = { success ->
                        Ok(venueMapper.mapToDomainEntityList(success))
                    },
                    failure = { message ->
                        Err(venueMessageMapper.mapToDomainEntity(message))
                    }
                )
            }
    }
}