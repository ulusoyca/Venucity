package com.ulusoyapps.venucity.locationprovider.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.locationprovider.entity.MockLocation
import javax.inject.Inject

class LocationMapper
@Inject constructor() : EntityMapper<DataLayerLocation, MockLocation> {
    override fun mapToLocationProviderEntity(type: DataLayerLocation): MockLocation {
        return MockLocation(
            type.latitude.toDouble(),
            type.longitude.toDouble(),
            type.timestamp,
        )
    }

    override fun mapToDataLayerEntity(type: MockLocation): DataLayerLocation {
        return DataLayerLocation(
            type.latitude.toFloat(),
            type.longitude.toFloat(),
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
