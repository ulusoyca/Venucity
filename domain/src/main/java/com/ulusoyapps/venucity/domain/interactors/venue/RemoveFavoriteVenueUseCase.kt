package com.ulusoyapps.venucity.domain.interactors.venue

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import javax.inject.Inject

class RemoveFavoriteVenueUseCase
@Inject constructor(
    private val venueRepository: VenueRepository
) {
    suspend operator fun invoke(venueId: String): Result<Unit, VenueMessage> {
        return venueRepository.removeFavoriteVenue(venueId)
    }
}
