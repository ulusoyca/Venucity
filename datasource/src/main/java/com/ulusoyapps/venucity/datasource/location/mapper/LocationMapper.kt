package com.ulusoyapps.venucity.datasource.location.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Latitude
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.Longitude
import javax.inject.Inject

class LocationMapper
@Inject constructor() : EntityMapper<DataLayerLocation, Location> {
    override fun mapToDomainEntity(type: DataLayerLocation): Location {
        return Location(
            LatLng(latitude = Latitude(type.latitude), longitude = Longitude(type.longitude)),
            timestamp = type.timestamp,
        )
    }

    override fun mapToDataLayerEntity(type: Location): DataLayerLocation {
        return DataLayerLocation(
            latitude = type.latLng.latitude.value,
            longitude = type.latLng.longitude.value,
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