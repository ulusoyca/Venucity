package com.ulusoyapps.venucity.datasource.location.datasource.mock

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import kotlinx.coroutines.flow.Flow

interface MockLocationSource {
    suspend fun getLiveLocation(
        locationUpdateInterval: Long,
        numberOfIntervals: Int,
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>>
}