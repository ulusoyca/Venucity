package com.ulusoyapps.venucity.domain.interactors.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.VenueDoesntExist
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RemoveFavoriteVenueUseCaseTest : BaseArchTest() {
    private val venueRepository: VenueRepository = mock()
    private val removeVenueUseCase = RemoveFavoriteVenueUseCase(venueRepository)

    @Test
    fun `should remove venue`() = runBlocking {
        val expected = Ok(Unit)
        whenever(venueRepository.removeFavoriteVenue("id")).thenReturn(expected)
        Truth.assertThat(removeVenueUseCase("id")).isEqualTo(expected)
    }

    @Test
    fun `should fail adding pet`() = runBlocking {
        val expected = Err(VenueDoesntExist)
        whenever(venueRepository.removeFavoriteVenue("id")).thenReturn(expected)
        Truth.assertThat(removeVenueUseCase("id")).isEqualTo(expected)
    }
}
