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

package com.ulusoyapps.venucity.cache.mapper

import com.ulusoyapps.venucity.cache.entities.CachedVenueMessage
import com.ulusoyapps.venucity.cache.entities.VenueInsertionError
import com.ulusoyapps.venucity.cache.entities.VenueNotFound
import com.ulusoyapps.venucity.cache.entities.VenuesFetchError
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueAddFailure
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueDoesntExist
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenuesFetchError
import javax.inject.Inject

class CachedVenueMessageMapper
@Inject constructor() : EntityMapper<DataLayerVenueMessage, CachedVenueMessage> {
    override fun mapToCacheEntity(type: DataLayerVenueMessage): CachedVenueMessage {
        throw NotImplementedError()
    }

    override fun mapToDataLayerEntity(type: CachedVenueMessage): DataLayerVenueMessage {
        return when(type) {
            is VenueInsertionError -> DataLayerVenueAddFailure
            is VenueNotFound -> DataLayerVenueDoesntExist
            is VenuesFetchError -> DataLayerVenuesFetchError
        }
    }

    override fun mapToDataLayerEntityList(type: List<CachedVenueMessage>): List<DataLayerVenueMessage> {
        throw NotImplementedError()
    }

    override fun mapToCacheLayerEntityList(type: List<DataLayerVenueMessage>): List<CachedVenueMessage> {
        throw NotImplementedError()
    }
}