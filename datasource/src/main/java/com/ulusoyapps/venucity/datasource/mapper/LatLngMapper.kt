package com.ulusoyapps.venucity.datasource.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.domain.entities.LatLng
import javax.inject.Inject


class LatLngMapper
@Inject constructor() : EntityMapper<DataLayerLatLng, LatLng> {
    override fun mapToDomainEntity(type: DataLayerLatLng): LatLng {
        return LatLng(
            type.latDoubleValue,
            type.lngDoubleValue
        )
    }

    override fun mapToDataLayerEntity(type: LatLng): DataLayerLatLng {
        return DataLayerLatLng(
            type.latDoubleValue,
            type.lngDoubleValue
        )
    }

    override fun mapToDomainEntityList(type: List<DataLayerLatLng>): List<LatLng> {
        return type.map { mapToDomainEntity(it) }
    }

    override fun mapToDataLayerEntityList(type: List<LatLng>): List<DataLayerLatLng> {
        return type.map { mapToDataLayerEntity(it) }
    }
}