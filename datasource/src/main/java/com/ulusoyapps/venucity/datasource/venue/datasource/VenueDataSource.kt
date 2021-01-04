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

package com.ulusoyapps.venucity.datasource.venue.datasource

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessage
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.domain.entities.*
import kotlinx.coroutines.flow.Flow

interface VenueDataSource {
    suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage>
    suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage>
    suspend fun getAllFavoriteVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>>
    suspend fun getNearbyVenues(latLng: DataLayerLatLng, maxAmount: Int): Result<List<DataLayerVenue>, DataLayerMessage>
}