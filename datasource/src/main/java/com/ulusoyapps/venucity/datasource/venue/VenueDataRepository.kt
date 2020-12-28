package com.ulusoyapps.venucity.datasource.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessageMapper
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMapper
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VenueDataRepository(
    private val localVenueDataSource: VenueDataSource,
    private val remoteVenueDataSource: VenueDataSource,
    private val venueMapper: VenueMapper,
    private val venueMessageMapper: DataLayerMessageMapper,
): VenueRepository {
    override suspend fun addFavoriteVenue(venue: Venue): Result<Unit, VenueMessage> {
        val dataLayerVenue = venueMapper.mapToDataLayerEntity(venue)
        return localVenueDataSource.addVenue(dataLayerVenue).mapBoth(
            success = { Ok(Unit) },
            failure = { message ->
                val error = venueMessageMapper.mapToDomainEntity(message)
                assert(error is VenueMessage)
                Err(error as VenueMessage)
            }
        )
    }

    override suspend fun removeFavoriteVenue(venueId: String): Result<Unit, VenueMessage> {
        return localVenueDataSource.removeVenue(venueId).mapBoth(
            success = { Ok(Unit) },
            failure = { message ->
                val error = venueMessageMapper.mapToDomainEntity(message)
                assert(error is VenueMessage)
                Err(error as VenueMessage)
            }
        )
    }

    override suspend fun getAllFavoriteVenues(): Flow<Result<List<Venue>, VenueMessage>> {
        return localVenueDataSource.getAllVenues()
            .map {
                it.mapBoth(
                    success = { success ->
                        Ok(venueMapper.mapToDomainEntityList(success))
                    },
                    failure = { message ->
                        val error = venueMessageMapper.mapToDomainEntity(message)
                        assert(error is VenueMessage)
                        Err(error as VenueMessage)
                    }
                )
            }
    }

    override suspend fun getNearbyVenues(
        latLng: LatLng,
        maxAmount: Int
    ): Result<List<Venue>, VenueMessage> {
        return remoteVenueDataSource.getNearbyVenues(latLng, maxAmount).mapBoth(
            success = { success ->
                Ok(venueMapper.mapToDomainEntityList(success))
            },
            failure = { message ->
                val error = venueMessageMapper.mapToDomainEntity(message)
                assert(error is VenueMessage)
                Err(error as VenueMessage)
            }
        )
    }
}