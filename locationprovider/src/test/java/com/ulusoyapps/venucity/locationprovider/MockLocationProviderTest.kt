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

package com.ulusoyapps.venucity.locationprovider

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.locationprovider.entity.MockLocation
import com.ulusoyapps.venucity.locationprovider.entity.SourceReadError
import com.ulusoyapps.venucity.locationprovider.mapper.LocationMapper
import com.ulusoyapps.venucity.locationprovider.mapper.LocationMessageMapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class MockLocationProviderTest : BaseArchTest() {

    private val csvLocationParser: CsvLocationParser = mock()
    private val locationMapper: LocationMapper = mock()
    private val locationMessageMapper: LocationMessageMapper = mock()

    @Test
    fun `should collect locations in loop`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val locations = listOf(
            MockLocation(0.0, 1.0, timestamp = 0),
            MockLocation(2.0, 3.0, timestamp = 0),
        )
        val dataLocations = listOf(
            DataLayerLocation(DataLayerLatLng(0.0, 1.0), timestamp = 0),
            DataLayerLocation(DataLayerLatLng(2.0, 3.0), timestamp = 0),
        )
        whenever(csvLocationParser.parseCsvFile(any())).thenReturn(Ok(locations))
        val mockLocationProvider =
            MockLocationProvider(
                csvLocationParser,
                coroutinesTestRule.testDispatcherProvider,
                locationMapper,
                locationMessageMapper
            )
        whenever(locationMapper.mapToDataLayerEntity(locations[0])).thenReturn(dataLocations[0])
        whenever(locationMapper.mapToDataLayerEntity(locations[1])).thenReturn(dataLocations[1])
        mockLocationProvider.getLiveLocation(1000, 5).collectIndexed { index, value ->
            if (index == 0) {
                Truth.assertThat(value).isEqualTo(Ok(dataLocations[0]))
            }
            if (index == 1) {
                Truth.assertThat(value).isEqualTo(Ok(dataLocations[1]))
            }
            if (index == 2) {
                Truth.assertThat(value).isEqualTo(Ok(dataLocations[0]))
            }
            if (index == 3) {
                Truth.assertThat(value).isEqualTo(Ok(dataLocations[1]))
            }
            if (index == 4) {
                Truth.assertThat(value).isEqualTo(Ok(dataLocations[0]))
            }
        }
    }

    @Test
    fun `should collect error`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        whenever(csvLocationParser.parseCsvFile(any())).thenReturn(Err(SourceReadError))
        val mockLocationProvider =
            MockLocationProvider(
                csvLocationParser,
                coroutinesTestRule.testDispatcherProvider,
                locationMapper,
                locationMessageMapper
            )
        whenever(locationMessageMapper.mapToDataLayerEntity(SourceReadError)).thenReturn(
            DataLayerLocationReadError
        )
        mockLocationProvider.getLiveLocation(1000, 5).collect {
            Truth.assertThat(it).isEqualTo(Err(DataLayerLocationReadError))
        }
    }
}
