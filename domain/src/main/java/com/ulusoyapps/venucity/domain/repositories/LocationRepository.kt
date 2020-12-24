package com.ulusoyapps.venucity.domain.repositories

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLiveLocation(
        trackerId: Long,
        locationUpdateInterval: Long = 1000
    ): Flow<Result<Location, LocationMessage>>
}
