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

package com.ulusoyapps.venucity.datasource.location.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.datasource.mapper.LatLngMapper
import com.ulusoyapps.venucity.domain.entities.Location
import javax.inject.Inject

class LocationMapper
@Inject constructor(
    private val latLngMapper: LatLngMapper
) : EntityMapper<DataLayerLocation, Location> {
    override fun mapToDomainEntity(type: DataLayerLocation): Location {
        return Location(
            latLngMapper.mapToDomainEntity(type.latLng),
            timestamp = type.timestamp,
        )
    }

    override fun mapToDataLayerEntity(type: Location): DataLayerLocation {
        return DataLayerLocation(
            latLngMapper.mapToDataLayerEntity(type.latLng),
            timestamp = type.timestamp,
        );
    }

    override fun mapToDomainEntityList(type: List<DataLayerLocation>): List<Location> {
        return type.map { mapToDomainEntity(it) }
    }

    override fun mapToDataLayerEntityList(type: List<Location>): List<DataLayerLocation> {
        return type.map { mapToDataLayerEntity(it) }
    }
}