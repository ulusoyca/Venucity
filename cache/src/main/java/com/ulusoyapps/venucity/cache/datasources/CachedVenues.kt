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

package com.ulusoyapps.venucity.cache.datasources

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.cache.dao.VenueDao
import com.ulusoyapps.venucity.cache.entities.VenueInsertionError
import com.ulusoyapps.venucity.cache.entities.VenueNotFound
import com.ulusoyapps.venucity.cache.entities.VenuesFetchError
import com.ulusoyapps.venucity.cache.mapper.CacheVenueMapper
import com.ulusoyapps.venucity.cache.mapper.CachedVenueMessageMapper
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CachedVenues
@Inject constructor(
    private val venueDao: VenueDao,
    private val venueMapper: CacheVenueMapper,
    private val messageMapper: CachedVenueMessageMapper,
    private val dispatcherProvider: DispatcherProvider,
) : LocalVenueSource {
    override suspend fun addFavoriteVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage> {
        val rowId = venueDao.addVenue(venueMapper.mapToCacheEntity(venue))
        return if (rowId == -1L) Err(messageMapper.mapToDataLayerEntity(VenueInsertionError)) else Ok(Unit)
    }

    override suspend fun removeFavoriteVenue(venueId: String): Result<Unit, DataLayerVenueMessage> {
        val numberOfRowsDeleted = venueDao.removeByVenueId(venueId)
        val dataErrorMessage = messageMapper.mapToDataLayerEntity(VenueNotFound)
        return if (numberOfRowsDeleted == 0) Err(dataErrorMessage) else Ok(Unit)
    }

    override suspend fun getAllFavoriteVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>> =
        venueDao.getAllVenues()
            .map {
                if (it == null) {
                    val dataErrorMessage = messageMapper.mapToDataLayerEntity(VenuesFetchError)
                    Err(dataErrorMessage)
                } else {
                    Ok(venueMapper.mapToDataLayerEntityList(it))
                }

            }
            .flowOn(dispatcherProvider.io())
}