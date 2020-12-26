package com.ulusoyapps.venucity.domain

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.*
import com.ulusoyapps.venucity.domain.interactors.location.GetLiveLocationUseCase
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
