package com.ulusoyapps.venucity.domain.interactors.venue

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetResolvedNearbyVenuesUseCase
@Inject constructor(
    private val venueRepository: VenueRepository
) {
    suspend operator fun invoke(latLng: LatLng, maxAmount: Int): Flow<Result<List<Venue>, VenueMessage>> {
        return venueRepository.getResolvedNearbyVenues(latLng, maxAmount)
    }
}
