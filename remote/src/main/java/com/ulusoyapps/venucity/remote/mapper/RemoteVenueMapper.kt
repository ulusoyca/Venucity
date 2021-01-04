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

package com.ulusoyapps.venucity.remote.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.remote.entities.ActiveMenu
import com.ulusoyapps.venucity.remote.entities.Restaurant
import com.ulusoyapps.venucity.remote.entities.RestaurantDesc
import com.ulusoyapps.venucity.remote.entities.RestaurantLocation
import com.ulusoyapps.venucity.remote.entities.RestaurantName
import javax.inject.Inject

class RemoteVenueMapper
@Inject constructor() : EntityMapper<DataLayerVenue, Restaurant> {

    override fun mapToRemoteEntity(type: DataLayerVenue): Restaurant {
        return Restaurant(
            ActiveMenu(type.id),
            listOf(
                RestaurantName(
                    lang = "en",
                    type.name,
                )
            ),
            listOf(
                RestaurantDesc(
                    lang = "en",
                    type.desc,
                )
            ),
            type.imageUrl,
            RestaurantLocation(
                coordinates = listOf(type.coordinate.latDoubleValue, type.coordinate.lngDoubleValue),
                type = "Point"
            )
        )
    }

    override fun mapToDataLayerEntity(type: Restaurant): DataLayerVenue {
        return type.run {
            DataLayerVenue(
                activeMenu.id,
                name.first().value,
                shortDescription.first().value,
                listImage,
                DataLayerLatLng(location.coordinates.first(), location.coordinates.last()),
                isFavorite = null,
            )
        }
    }

    override fun mapToDataLayerEntityList(type: List<Restaurant>): List<DataLayerVenue> {
        return type.map { mapToDataLayerEntity(it) }
    }

    override fun mapToRemoteEntityList(type: List<DataLayerVenue>): List<Restaurant> {
        return type.map { mapToRemoteEntity(it) }
    }
}
