package com.ulusoyapps.venucity.cache.datasources

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.cache.dao.VenueDao
import com.ulusoyapps.venucity.cache.entities.VenueInsertionError
import com.ulusoyapps.venucity.cache.entities.VenueNotFound
import com.ulusoyapps.venucity.cache.entities.VenuesFetchError
import com.ulusoyapps.venucity.cache.mapper.CacheVenueMapper
import com.ulusoyapps.venucity.cache.mapper.CachedVenueMessageMapper
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CachedVenues
@Inject constructor(
    private val venueDao: VenueDao,
    private val venueMapper: CacheVenueMapper,
    private val messageMapper: CachedVenueMessageMapper,
    private val dispatcherProvider: DispatcherProvider,
) : LocalVenueSource {
    override suspend fun addVenue(venue: DataLayerVenue): Result<Unit, DataLayerVenueMessage> {
        val rowId = venueDao.addVenue(venueMapper.mapToCacheEntity(venue))
        return if (rowId == -1L) Err(messageMapper.mapToDataLayerEntity(VenueInsertionError)) else Ok(Unit)
    }

    override suspend fun removeVenue(venueId: String): Result<Unit, DataLayerVenueMessage> {
        val numberOfRowsDeleted = venueDao.removeByVenueId(venueId)
        val dataErrorMessage = messageMapper.mapToDataLayerEntity(VenueNotFound)
        return if (numberOfRowsDeleted == 0) Err(dataErrorMessage) else Ok(Unit)
    }

    override suspend fun getAllVenues(): Flow<Result<List<DataLayerVenue>, DataLayerVenueMessage>> =
        venueDao.getAllVenues()
            .map {
                if (it == null) {
                    val dataErrorMessage = messageMapper.mapToDataLayerEntity(VenuesFetchError)
                    Err(dataErrorMessage)
                } else {
                    Ok(venueMapper.mapToDataLayerEntityList(it))
                }

            }
            .flowOn(dispatcherProvider.io())
}