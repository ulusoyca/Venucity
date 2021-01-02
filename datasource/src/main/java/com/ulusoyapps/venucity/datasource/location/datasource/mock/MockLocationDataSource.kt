package com.ulusoyapps.venucity.datasource.location.datasource.mock

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MockLocationDataSource
@Inject constructor(
    private val mockLocationDataSource: MockLocationSource
) : LocationDataSource {
    override suspend fun getLiveLocation(
        locationUpdateIntervalTimeMillisec: Long,
        numberOfIntervals: Int
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>> {
        return mockLocationDataSource.getLiveLocation(locationUpdateIntervalTimeMillisec, numberOfIntervals)
    }
}