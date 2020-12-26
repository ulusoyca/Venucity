package com.ulusoyapps.venucity.domain.interactors.location

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.repositories.location.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLiveLocationUseCase
@Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(locationUpdateInterval: Long = 1000): Flow<Result<Location, LocationMessage>> {
        return locationRepository.getLiveLocation(locationUpdateInterval)
    }
}
