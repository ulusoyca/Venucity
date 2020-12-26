package com.ulusoyapps.venucity.locationprovider.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.locationprovider.entity.MockLocation
import javax.inject.Inject

class LocationMapper
@Inject constructor() : EntityMapper<DataLayerLocation, MockLocation> {
    override fun mapToLocationProviderEntity(type: DataLayerLocation): MockLocation {
        return MockLocation(
            type.latLng.latDoubleValue,
            type.latLng.lngDoubleValue,
            type.timestamp,
        )
    }

    override fun mapToDataLayerEntity(type: MockLocation): DataLayerLocation {
        return DataLayerLocation(
            latLng = DataLayerLatLng(type.latitude, type.longitude),
            type.timestamp,
        )
    }

    override fun mapToDataLayerEntityList(type: List<DataLayerLocation>): List<MockLocation> {
        return type.map { mapToLocationProviderEntity(it) }
    }

    override fun mapToLocationProviderEntityList(type: List<MockLocation>): List<DataLayerLocation> {
        return type.map { mapToDataLayerEntity(it) }
    }
}
