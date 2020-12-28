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
import javax.inject.Inject

class MockLocationProvider
@Inject constructor(
    private val csvLocationParser: CsvLocationParser,
    private val dispatcherProvider: DispatcherProvider,
    private val locationMapper: LocationMapper,
    private val locationMessageMapper: LocationMessageMapper
) : MockLocationSource {

    override suspend fun getLiveLocation(
        locationUpdateInterval: Long,
        numberOfIntervals: Int
    ): Flow<Result<DataLayerLocation, DataLayerLocationMessage>> = flow {
        val locationList = csvLocationParser.parseCsvFile(R.raw.mock_locations)
        locationList.mapBoth(
            success = { locations ->
                var counter = 0
                locations.asSequence()
                    .repeat()
                    .takeWhile { counter < numberOfIntervals }
                    .forEach { location ->
                        counter++
                        delay(locationUpdateInterval)
                        emit(Ok(locationMapper.mapToDataLayerEntity(location)))
                    }
            },
            failure = {
                emit(Err(locationMessageMapper.mapToDataLayerEntity(it)))
            }
        )
    }.flowOn(dispatcherProvider.io())
}

// https://stackoverflow.com/questions/48007311/how-do-i-infinitely-repeat-a-sequence-in-kotlin/48024169#48024169
fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }
