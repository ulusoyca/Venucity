package com.ulusoyapps.venucity.locationprovider

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.location.datasource.mock.MockLocationSource
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.locationprovider.mapper.LocationMapper
import com.ulusoyapps.venucity.locationprovider.mapper.LocationMessageMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class MockLocationProvider
@Inject constructor(
    private val csvLocationParser: CsvLocationParser,
    private val dispatcherProvider: DispatcherProvider,
    private val locationMapper: LocationMapper,
    private val locationMessageMapper: LocationMessageMapper
) : MockLocationSource {

    override suspend fun getLiveLocation(
        locationUpdateIntervalTimeMillisec: Long,
        numberOfIntervals: Int
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>> = flow {
        val locationList = csvLocationParser.parseCsvFile(R.raw.mock_locations)
        locationList.mapBoth(
            success = { locations ->
                locations.asSequence()
                    .repeat(numberOfIntervals)
                    .forEach { location ->
                        Timber.d("New location lat: ${location.latitude} & lng: ${location.latitude} update interval: $locationUpdateIntervalTimeMillisec")
                        emit(Ok(locationMapper.mapToDataLayerEntity(location)))
                        delay(locationUpdateIntervalTimeMillisec)
                    }
            },
            failure = {
                emit(Err(locationMessageMapper.mapToDataLayerEntity(it)))
            }
        )
    }.flowOn(dispatcherProvider.io())
}

// https://stackoverflow.com/questions/48007311/how-do-i-infinitely-repeat-a-sequence-in-kotlin/48024169#48024169
fun <T> Sequence<T>.repeat(n: Int) =
    if (n < 0) {
        sequence { while (true) yieldAll(this@repeat) }
    } else {
        sequence { repeat(n) { yieldAll(this@repeat) } }
    }
