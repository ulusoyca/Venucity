package com.ulusoyapps.venucity.domain.interactors.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueAddFailure
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddFavoriteVenueUseCaseTest : BaseArchTest() {
    private val venueRepository: VenueRepository = mock()
    private val addVenueUseCase = AddFavoriteVenueUseCase(venueRepository)

    private val venue = Venue(
        "id",
        "name",
        "desc",
        "imageUrl",
        LatLng(0.0, 0.0),
        isFavorite = true,
    )

    @Test
    fun `should add venue`() = runBlocking {
        val expected = Ok(Unit)
        whenever(venueRepository.addFavoriteVenue(venue)).thenReturn(expected)
        Truth.assertThat(addVenueUseCase(venue)).isEqualTo(expected)
    }

    @Test
    fun `should fail adding pet`() = runBlocking {
        val expected = Err(VenueAddFailure)
        whenever(venueRepository.addFavoriteVenue(venue)).thenReturn(expected)
        Truth.assertThat(addVenueUseCase(venue)).isEqualTo(expected)
    }
}