package com.ulusoyapps.venucity.cache.mapper


import com.ulusoyapps.venucity.cache.entities.CachedLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatitude
import com.ulusoyapps.venucity.datasource.entities.DataLayerLongitude
import javax.inject.Inject


class CacheLatLngMapper
@Inject constructor() : EntityMapper<DataLayerLatLng, CachedLatLng> {
    override fun mapToCacheEntity(type: DataLayerLatLng): CachedLatLng {
        return CachedLatLng(
            type.latitude.value,
            type.longitude.value,
        )
    }

    override fun mapToDataLayerEntity(type: CachedLatLng): DataLayerLatLng {
        return DataLayerLatLng(
            DataLayerLatitude(type.latitude),
            DataLayerLongitude(type.longitude),
        )
    }

    override fun mapToDataLayerEntityList(type: List<CachedLatLng>): List<DataLayerLatLng> {
        return type.map { mapToDataLayerEntity(it) }
    }

    override fun mapToCacheLayerEntityList(type: List<DataLayerLatLng>): List<CachedLatLng> {
        return type.map { mapToCacheEntity(it) }
    }
}