package com.ulusoyapps.venucity.datasource.venue.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.domain.entities.*
import javax.inject.Inject

class VenueMapper
@Inject constructor() : EntityMapper<DataLayerVenue, Venue> {
    override fun mapToDomainEntity(type: DataLayerVenue): Venue {
        return Venue(
            id = type.id,
            name = type.name,
            desc = type.desc,
            imageUrl = type.imageUrl,
            coordinate = type.coordinate
        )
    }

    override fun mapToDataLayerEntity(type: Venue): DataLayerVenue {
        return DataLayerVenue(
            id = type.id,
            name = type.name,
            desc = type.desc,
            imageUrl = type.imageUrl,
            coordinate = type.coordinate
        );
    }

    override fun mapToDomainEntityList(type: List<DataLayerVenue>): List<Venue> {
        return type.map { mapToDomainEntity(it) }
    }

    override fun mapToDataLayerEntityList(type: List<Venue>): List<DataLayerVenue> {
        return type.map { mapToDataLayerEntity(it) }
    }
}