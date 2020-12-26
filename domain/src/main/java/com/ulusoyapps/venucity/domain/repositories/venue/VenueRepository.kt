package com.ulusoyapps.venucity.domain.repositories.venue

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import kotlinx.coroutines.flow.Flow

interface VenueRepository {
    suspend fun addVenue(venue: Venue): Result<Unit, VenueMessage>
    suspend fun removeVenue(venueId: String): Result<Unit, VenueMessage>
    suspend fun getAllVenues(): Flow<Result<List<Venue>, VenueMessage>>
}
