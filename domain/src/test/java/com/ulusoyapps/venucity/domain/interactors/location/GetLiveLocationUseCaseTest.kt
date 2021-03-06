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

package com.ulusoyapps.venucity.domain.interactors.location

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Latitude
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.Longitude
import com.ulusoyapps.venucity.domain.repositories.location.LocationRepository
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetLiveLocationUseCaseTest : BaseArchTest() {
    private val locationRepository: LocationRepository = mock()
    private val getLiveLocationsUseCase =
        GetLiveLocationUseCase(
            locationRepository
        )

    private val locationErrorMessage = LocationMessage()

    private val flow = flow {
        emit(Err(locationErrorMessage))
        emit(Ok(lastLocation))
    }

    private val lastLocation = Location(
        LatLng(
            latitude = Latitude(0f),
            longitude = Longitude(0f)
        ),
        0,
    )

    @Test
    fun `should get live location`() = runBlockingTest {
        whenever(locationRepository.getLiveLocation(0)).thenReturn(flow)
        getLiveLocationsUseCase(0).collectIndexed { index, value ->
            if (index == 0) {
                Truth.assertThat(value).isEqualTo(Err(locationErrorMessage))
            }
            if (index == 1) {
                Truth.assertThat(value.get()).isEqualTo(lastLocation)
            }
        }
    }
}
