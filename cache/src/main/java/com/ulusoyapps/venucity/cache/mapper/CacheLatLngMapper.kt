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


import com.ulusoyapps.venucity.cache.entities.CachedLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatitude
import com.ulusoyapps.venucity.datasource.entities.DataLayerLongitude
import javax.inject.Inject


class CacheLatLngMapper
@Inject constructor() : EntityMapper<DataLayerLatLng, CachedLatLng> {
    override fun mapToCacheEntity(type: DataLayerLatLng): CachedLatLng {
        return CachedLatLng(
            type.latitude.value,
            type.longitude.value,
        )
    }

    override fun mapToDataLayerEntity(type: CachedLatLng): DataLayerLatLng {
        return DataLayerLatLng(
            DataLayerLatitude(type.latitude),
            DataLayerLongitude(type.longitude),
        )
    }

    override fun mapToDataLayerEntityList(type: List<CachedLatLng>): List<DataLayerLatLng> {
        return type.map { mapToDataLayerEntity(it) }
    }

    override fun mapToCacheLayerEntityList(type: List<DataLayerLatLng>): List<CachedLatLng> {
        return type.map { mapToCacheEntity(it) }
    }
}