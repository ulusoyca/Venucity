package com.ulusoyapps.venucity.datasource.location

import com.github.michaelbull.result.*
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMapper
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMessageMapper
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.repositories.location.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationDataRepository
@Inject constructor(
    private val mockLocationDatasource: LocationDataSource,
    private val locationMapper: LocationMapper,
    private val locationMessageMapper: LocationMessageMapper,
) : LocationRepository {
    override suspend fun getLiveLocation(
        locationUpdateInterval: Long,
        numberOfIntervals: Int,
    ): Flow<Result<Location, LocationMessage>> {
        return mockLocationDatasource.getLiveLocation(locationUpdateInterval, numberOfIntervals)
            .map {
                it.mapBoth(
                    success = { success ->
                        Ok(
                            locationMapper.mapToDomainEntity(success)
                        )
                    },
                    failure = { dataLayerLocationMessage ->
                        Err(
                            locationMessageMapper.mapToDomainEntity(
                                dataLayerLocationMessage
                            )
                        )
                    }
                )
            }
    }
}