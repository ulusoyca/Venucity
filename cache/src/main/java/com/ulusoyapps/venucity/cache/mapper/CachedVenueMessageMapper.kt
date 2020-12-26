package com.ulusoyapps.venucity.cache.mapper

import com.ulusoyapps.venucity.cache.entities.CachedVenueMessage
import com.ulusoyapps.venucity.cache.entities.VenueInsertionError
import com.ulusoyapps.venucity.cache.entities.VenueNotFound
import com.ulusoyapps.venucity.cache.entities.VenuesFetchError
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueAddFailure
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueDoesntExist
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenuesFetchError
import javax.inject.Inject

class CachedVenueMessageMapper
@Inject constructor() : EntityMapper<DataLayerVenueMessage, CachedVenueMessage> {
    override fun mapToCacheEntity(type: DataLayerVenueMessage): CachedVenueMessage {
        throw NotImplementedError()
    }

    override fun mapToDataLayerEntity(type: CachedVenueMessage): DataLayerVenueMessage {
        return when(type) {
            is VenueInsertionError -> DataLayerVenueAddFailure
            is VenueNotFound -> DataLayerVenueDoesntExist
            is VenuesFetchError -> DataLayerVenuesFetchError
        }
    }

    override fun mapToDataLayerEntityList(type: List<CachedVenueMessage>): List<DataLayerVenueMessage> {
        throw NotImplementedError()
    }

    override fun mapToCacheLayerEntityList(type: List<DataLayerVenueMessage>): List<CachedVenueMessage> {
        throw NotImplementedError()
    }
}