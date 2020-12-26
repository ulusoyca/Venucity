package com.ulusoyapps.venucity.cache.mapper

import com.ulusoyapps.venucity.cache.entities.CachedVenue
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import javax.inject.Inject


class CacheVenueMapper
@Inject constructor(
    private val cacheLatLngMapper: CacheLatLngMapper,
) : EntityMapper<DataLayerVenue, CachedVenue> {
    override fun mapToCacheEntity(type: DataLayerVenue): CachedVenue {
        return CachedVenue(
            type.id,
            type.name,
            type.desc,
            type.imageUrl,
            cacheLatLngMapper.mapToCacheEntity(type.coordinate)
        )
    }

    override fun mapToDataLayerEntity(type: CachedVenue): DataLayerVenue {
        return DataLayerVenue(
            type.id,
            type.name,
            type.desc,
            type.imageUrl,
            cacheLatLngMapper.mapToDataLayerEntity(type.coordinate)
        )
    }

    override fun mapToDataLayerEntityList(type: List<CachedVenue>): List<DataLayerVenue> {
        return type.map { mapToDataLayerEntity(it) }
    }

    override fun mapToCacheLayerEntityList(type: List<DataLayerVenue>): List<CachedVenue> {
        return type.map { mapToCacheEntity(it) }
    }
}