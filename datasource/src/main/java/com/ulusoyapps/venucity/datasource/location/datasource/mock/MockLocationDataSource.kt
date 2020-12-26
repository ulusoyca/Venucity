package com.ulusoyapps.venucity.datasource.location.datasource.mock

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMapper
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMessageMapper
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MockLocationDataSource
@Inject constructor(
    private val mockLocationDataSource: MockLocationSource
) : LocationDataSource {
    override suspend fun getLiveLocation(
        locationUpdateInterval: Long,
        numberOfIntervals: Int
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>> {
        return mockLocationDataSource.getLiveLocation(locationUpdateInterval, numberOfIntervals)
    }
}