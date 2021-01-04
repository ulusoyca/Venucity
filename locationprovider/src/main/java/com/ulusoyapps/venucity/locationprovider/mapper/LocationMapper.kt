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

package com.ulusoyapps.venucity.locationprovider.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.locationprovider.entity.MockLocation
import javax.inject.Inject

class LocationMapper
@Inject constructor() : EntityMapper<DataLayerLocation, MockLocation> {
    override fun mapToLocationProviderEntity(type: DataLayerLocation): MockLocation {
        return MockLocation(
            type.latLng.latDoubleValue,
            type.latLng.lngDoubleValue,
            type.timestamp,
        )
    }

    override fun mapToDataLayerEntity(type: MockLocation): DataLayerLocation {
        return DataLayerLocation(
            latLng = DataLayerLatLng(type.latitude, type.longitude),
            type.timestamp,
        )
    }

    override fun mapToDataLayerEntityList(type: List<DataLayerLocation>): List<MockLocation> {
        return type.map { mapToLocationProviderEntity(it) }
    }

    override fun mapToLocationProviderEntityList(type: List<MockLocation>): List<DataLayerLocation> {
        return type.map { mapToDataLayerEntity(it) }
    }
}
