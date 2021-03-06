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

import com.ulusoyapps.venucity.cache.entities.CachedVenue
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import javax.inject.Inject


class CacheVenueMapper
@Inject constructor(
    private val cacheLatLngMapper: CacheLatLngMapper,
) : EntityMapper<DataLayerVenue, CachedVenue> {
    override fun mapToCacheEntity(type: DataLayerVenue): CachedVenue {
        return CachedVenue(
            type.id,
            type.name,
            type.desc,
            type.imageUrl,
            cacheLatLngMapper.mapToCacheEntity(type.coordinate)
        )
    }

    override fun mapToDataLayerEntity(type: CachedVenue): DataLayerVenue {
        return DataLayerVenue(
            type.id,
            type.name,
            type.desc,
            type.imageUrl,
            cacheLatLngMapper.mapToDataLayerEntity(type.coordinate),
            isFavorite = true,
        )
    }

    override fun mapToDataLayerEntityList(type: List<CachedVenue>): List<DataLayerVenue> {
        return type.map { mapToDataLayerEntity(it) }
    }

    override fun mapToCacheLayerEntityList(type: List<DataLayerVenue>): List<CachedVenue> {
        return type.map { mapToCacheEntity(it) }
    }
}