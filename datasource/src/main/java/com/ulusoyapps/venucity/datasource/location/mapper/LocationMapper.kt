package com.ulusoyapps.venucity.datasource.location.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.datasource.mapper.LatLngMapper
import com.ulusoyapps.venucity.domain.entities.Location
import javax.inject.Inject

class LocationMapper
@Inject constructor(
    private val latLngMapper: LatLngMapper
) : EntityMapper<DataLayerLocation, Location> {
    override fun mapToDomainEntity(type: DataLayerLocation): Location {
        return Location(
            latLngMapper.mapToDomainEntity(type.latLng),
            timestamp = type.timestamp,
        )
    }

    override fun mapToDataLayerEntity(type: Location): DataLayerLocation {
        return DataLayerLocation(
            latLngMapper.mapToDataLayerEntity(type.latLng),
            timestamp = type.timestamp,
        );
    }

    override fun mapToDomainEntityList(type: List<DataLayerLocation>): List<Location> {
        return type.map { mapToDomainEntity(it) }
    }

    override fun mapToDataLayerEntityList(type: List<Location>): List<DataLayerLocation> {
        return type.map { mapToDataLayerEntity(it) }
    }
}