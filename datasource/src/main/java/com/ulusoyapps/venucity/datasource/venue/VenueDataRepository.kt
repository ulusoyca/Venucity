/*
 *  Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ulusoyapps.venucity.datasource.venue

import com.github.michaelbull.result.*
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.datasource.mapper.DataLayerMessageMapper
import com.ulusoyapps.venucity.datasource.mapper.LatLngMapper
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
import javax.inject.Inject
import javax.inject.Named

const val REMOTE_VENUE_DATA_SOURCE = "Remote"
const val LOCAL_VENUE_DATA_SOURCE = "Local"

class VenueDataRepository
@Inject constructor(
    @Named(LOCAL_VENUE_DATA_SOURCE)
    private val localVenueDataSource: VenueDataSource,
    @Named(REMOTE_VENUE_DATA_SOURCE)
    private val remoteVenueDataSource: VenueDataSource,
    private val venueMapper: VenueMapper,
    private val latLngMapper: LatLngMapper,
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
        return remoteVenueDataSource.getNearbyVenues(
            latLngMapper.mapToDataLayerEntity(latLng),
            maxAmount
        ).mapBoth(
            success = { success ->
                Ok(venueMapper.mapToDomainEntityList(success))
            },
            failure = { message ->
                val error = venueMessageMapper.mapToDomainEntity(message)
                Err(error as VenueMessage)
            }
        )
    }

    override suspend fun getResolvedNearbyVenues(
        latLng: LatLng,
        maxAmount: Int
    ): Flow<Result<List<Venue>, VenueMessage>> = withContext(dispatcherProvider.io()) {
        getAllFavoriteVenues().map { favoriteVenuesResult ->
            favoriteVenuesResult.mapBoth(
                success = { favoriteVenues ->
                    getNearbyVenues(latLng, maxAmount).mapBoth(
                        success = { nearByVenues ->
                            val updatedList: List<Venue> = nearByVenues.map { nearByVenue ->
                                nearByVenue.copy(
                                    isFavorite = isVenueFavorite(
                                        nearByVenue.id,
                                        favoriteVenues
                                    )
                                )
                            }
                            Ok(updatedList)
                        },
                        failure = { message ->
                            Err(message)
                        }
                    )
                },
                failure = { message ->
                    Err(message)
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