package com.ulusoyapps.venucity.datasource.location.datasource

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    suspend fun getLiveLocation(
        locationUpdateInterval: Long,
        numberOfIntervals: Int,
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>>
}