package com.ulusoyapps.venucity.datasource.venue

import com.github.michaelbull.result.*
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessageMapper
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMapper
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VenueDataRepository(
    private val localVenueDataSource: VenueDataSource,
    private val remoteVenueDataSource: VenueDataSource,
    private val venueMapper: VenueMapper,
    private val venueMessageMapper: DataLayerMessageMapper,
    private val dispatcherProvider: DispatcherProvider,
) : VenueRepository {
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
        return localVenueDataSource.getAllFavoriteVenues()
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

    override suspend fun getResolvedNearbyVenues(
        latLng: LatLng,
        maxAmount: Int
    ): Flow<Result<List<Venue>, VenueMessage>> = withContext(dispatcherProvider.io()) {
        val nearbyVenues = async { getNearbyVenues(latLng, maxAmount) }
        val favoriteVenues = async { getAllFavoriteVenues() }
        updatedNearbyVenues(nearbyVenues.await(), favoriteVenues.await())
    }

    private suspend fun updatedNearbyVenues(
        allNearby: Result<List<Venue>, VenueMessage>,
        allFavorite: Flow<Result<List<Venue>, VenueMessage>>
    ): Flow<Result<List<Venue>, VenueMessage>> = flow {
        allFavorite.collect { favoriteVenuesResult ->
            favoriteVenuesResult.mapBoth(
                success = { favoriteVenues ->
                    allNearby.mapBoth(
                        success = { nearByVenues ->
                            val updatedList: List<Venue> = nearByVenues.map { nearByVenue ->
                                nearByVenue.copy(
                                    isFavorite = isVenueFavorite(
                                        nearByVenue.id,
                                        favoriteVenues
                                    )
                                )
                            }
                            emit(Ok(updatedList))
                        },
                        failure = { message ->
                            emit(Err(message))
                        }
                    )
                },
                failure = { message ->
                    emit(Err(message))
                }
            )
        }
    }

    private fun isVenueFavorite(nearByVenueId: String, favoriteVenues: List<Venue>): Boolean {
        var isFavorite = false
        for (venue in favoriteVenues) {
            if (venue.id == nearByVenueId) {
                isFavorite = true
                break
            }
        }
        return isFavorite
    }
}