package com.ulusoyapps.venucity.domain.interactors.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenuesFetchError
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAllFavoriteVenuesUseCaseTest : BaseArchTest() {

    private val venueRepository: VenueRepository = mock()
    private val getAllVenuesUseCase = GetAllFavoriteVenuesUseCase(venueRepository)

    private val flow = flow {
        emit(Err(VenuesFetchError))
        emit(Ok(listOf(venue)))
    }

    private val venue = Venue(
        "id",
        "name",
        "desc",
        "imageUrl",
        LatLng(0.0, 0.0),
        isFavorite = true,
    )

    @Test
    fun `should get all venues`() = runBlocking {
        whenever(venueRepository.getAllFavoriteVenues()).thenReturn(flow)

        getAllVenuesUseCase().collectIndexed { index, value ->
            if (index == 0) {
                Truth.assertThat(value).isEqualTo(Err(VenuesFetchError))
            }
            if (index == 1) {
                Truth.assertThat(value.get()).isEqualTo(listOf(venue))
            }
        }
    }
}
