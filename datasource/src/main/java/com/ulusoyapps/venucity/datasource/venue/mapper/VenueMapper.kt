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

package com.ulusoyapps.venucity.datasource.venue.mapper

import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.datasource.mapper.LatLngMapper
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.domain.entities.*
import javax.inject.Inject

class VenueMapper
@Inject constructor(
    private val latLngMapper: LatLngMapper
) : EntityMapper<DataLayerVenue, Venue> {
    override fun mapToDomainEntity(type: DataLayerVenue): Venue {
        return Venue(
            id = type.id,
            name = type.name,
            desc = type.desc,
            imageUrl = type.imageUrl,
            coordinate = latLngMapper.mapToDomainEntity(type.coordinate),
            isFavorite = type.isFavorite,
        )
    }

    override fun mapToDataLayerEntity(type: Venue): DataLayerVenue {
        return DataLayerVenue(
            id = type.id,
            name = type.name,
            desc = type.desc,
            imageUrl = type.imageUrl,
            coordinate = latLngMapper.mapToDataLayerEntity(type.coordinate),
            isFavorite = type.isFavorite,
        )
    }

    override fun mapToDomainEntityList(type: List<DataLayerVenue>): List<Venue> {
        return type.map { mapToDomainEntity(it) }
    }

    override fun mapToDataLayerEntityList(type: List<Venue>): List<DataLayerVenue> {
        return type.map { mapToDataLayerEntity(it) }
    }
}