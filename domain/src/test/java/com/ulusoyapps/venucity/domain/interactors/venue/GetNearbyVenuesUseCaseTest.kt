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
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetNearbyVenuesUseCaseTest : BaseArchTest() {
    private val venueRepository: VenueRepository = mock()
    private val getNearbyVenuesUseCase = GetNearbyVenuesUseCase(venueRepository)

    private val venue = Venue(
        "id",
        "name",
        "desc",
        "imageUrl",
        LatLng(0.0, 0.0),
        isFavorite = true,
    )

    private val venues = Ok(listOf(venue))

    @Test
    fun `should get all venues nearby`() = runBlocking {
        whenever(venueRepository.getNearbyVenues(LatLng(0.0, 0.0), 1)).thenReturn(venues)
        val actual = getNearbyVenuesUseCase(LatLng(0.0, 0.0), 1)
        Truth.assertThat(actual.get()).isEqualTo(listOf(venue))
    }

    @Test
    fun `should get error for venues nearby`() = runBlocking {
        whenever(venueRepository.getNearbyVenues(LatLng(0.0, 0.0), 1)).thenReturn(Err(VenuesFetchError))
        val actual = getNearbyVenuesUseCase(LatLng(0.0, 0.0), 1)
        Truth.assertThat(actual).isEqualTo(Err(VenuesFetchError))
    }
}
