package com.ulusoyapps.venucity.domain.interactors.venue

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import javax.inject.Inject

class AddVenueUseCase
@Inject constructor(
    private val venueRepository: VenueRepository
) {
    suspend operator fun invoke(venue: Venue): Result<Unit, VenueMessage> {
        return venueRepository.addVenue(venue)
    }
}
