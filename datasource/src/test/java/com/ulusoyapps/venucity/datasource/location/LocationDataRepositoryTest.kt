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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.datasource.mapper.DataLayerMessageMapper
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMapper
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationNotAvailable
import com.ulusoyapps.venucity.domain.entities.LocationReadError
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class LocationDataRepositoryTest : BaseArchTest() {

    private val mockLocationDatasource: LocationDataSource = mock()
    private val locationMapper: LocationMapper = mock()
    private val locationMessageMapper: DataLayerMessageMapper = mock()
    private val dataLayerLatLng = DataLayerLatLng(0.0, 1.0)
    private val dataLayerFlow = flow {
        emit(
            Ok(DataLayerLocation(dataLayerLatLng, timestamp = 0)),
        )
        emit(
            Err(DataLayerLocationReadError),
        )
        emit(
            Err(DataLayerLocationNotAvailable),
        )
    }

    @Test
    fun `should collect locations in loop`() = runBlockingTest {
        val locationRepository = LocationDataRepository(
            mockLocationDatasource,
            locationMapper,
            locationMessageMapper,
        )
        val dataLayerLocation = DataLayerLocation(dataLayerLatLng, timestamp = 0)
        val domainLocation = Location(LatLng(0.0, 1.0), 0)
        whenever(locationMapper.mapToDomainEntity(dataLayerLocation)).thenReturn(domainLocation)
        whenever(locationMessageMapper.mapToDomainEntity(DataLayerLocationReadError)).thenReturn(LocationReadError)
        whenever(locationMessageMapper.mapToDomainEntity(DataLayerLocationNotAvailable)).thenReturn(LocationNotAvailable)
        whenever(mockLocationDatasource.getLiveLocation(1000, 5)).thenReturn(dataLayerFlow)
        locationRepository.getLiveLocation(1000, 5).collectIndexed { index, value ->
            if (index == 0) {
                Truth.assertThat(value).isEqualTo(Ok(domainLocation))
            }
            if (index == 1) {
                Truth.assertThat(value).isEqualTo(Err(LocationReadError))
            }
            if (index == 2) {
                Truth.assertThat(value).isEqualTo(Err(LocationNotAvailable))
            }
        }
    }
}