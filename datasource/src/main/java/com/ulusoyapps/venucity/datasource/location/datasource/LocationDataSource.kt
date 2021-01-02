package com.ulusoyapps.venucity.datasource.location.datasource

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    suspend fun getLiveLocation(
        locationUpdateIntervalTimeMillisec: Long,
        numberOfIntervals: Int,
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>>
}