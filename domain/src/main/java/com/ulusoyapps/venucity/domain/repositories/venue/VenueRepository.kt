package com.ulusoyapps.venucity.domain.repositories.venue

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueMessage
import kotlinx.coroutines.flow.Flow

interface VenueRepository {
    suspend fun addFavoriteVenue(venue: Venue): Result<Unit, VenueMessage>
    suspend fun removeFavoriteVenue(venueId: String): Result<Unit, VenueMessage>
    suspend fun getAllFavoriteVenues(): Flow<Result<List<Venue>, VenueMessage>>
    suspend fun getNearbyVenues(latLng: LatLng, maxAmount: Int): Result<List<Venue>, VenueMessage>
}
