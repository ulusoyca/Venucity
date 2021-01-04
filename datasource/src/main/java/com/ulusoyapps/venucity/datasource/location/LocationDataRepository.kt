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

package com.ulusoyapps.venucity.datasource.location

import com.github.michaelbull.result.*
import com.ulusoyapps.venucity.datasource.mapper.DataLayerMessageMapper
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMapper
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.repositories.location.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationDataRepository
@Inject constructor(
    private val mockLocationDatasource: LocationDataSource,
    private val locationMapper: LocationMapper,
    private val locationMessageMapper: DataLayerMessageMapper,
) : LocationRepository {
    override suspend fun getLiveLocation(
        locationUpdateIntervalTimeMillisec: Long,
        numberOfIntervals: Int,
    ): Flow<Result<Location, LocationMessage>> {
        return mockLocationDatasource.getLiveLocation(locationUpdateIntervalTimeMillisec, numberOfIntervals)
            .map {
                it.mapBoth(
                    success = { success ->
                        Ok(
                            locationMapper.mapToDomainEntity(success)
                        )
                    },
                    failure = { dataLayerLocationMessage ->
                        val message = locationMessageMapper.mapToDomainEntity(dataLayerLocationMessage)
                        assert(message is LocationMessage)
                        Err(message as LocationMessage)
                    }
                )
            }
    }
}