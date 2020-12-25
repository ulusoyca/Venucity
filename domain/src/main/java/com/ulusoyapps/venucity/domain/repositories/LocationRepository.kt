package com.ulusoyapps.venucity.domain.repositories

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLiveLocation(
        locationUpdateInterval: Long = 1000,
        numberOfIntervals: Int = -1,
    ): Flow<Result<Location, LocationMessage>>
}
