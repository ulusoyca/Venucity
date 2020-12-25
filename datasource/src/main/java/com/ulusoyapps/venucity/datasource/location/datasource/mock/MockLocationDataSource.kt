package com.ulusoyapps.venucity.datasource.location.datasource.mock

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MockLocationDataSource
@Inject constructor(
    private val locationDataSource: LocationDataSource
): LocationDataSource {
    override suspend fun getLiveLocation(
        locationUpdateInterval: Long,
        numberOfIntervals: Int
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>> {
        return locationDataSource.getLiveLocation(locationUpdateInterval, numberOfIntervals)
    }
}