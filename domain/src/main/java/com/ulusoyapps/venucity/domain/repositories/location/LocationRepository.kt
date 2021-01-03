package com.ulusoyapps.venucity.domain.repositories.location

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLiveLocation(
        locationUpdateIntervalTimeMillisec: Long,
        numberOfIntervals: Int = -1,
    ): Flow<Result<Location, LocationMessage>>
}
