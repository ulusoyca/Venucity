package com.ulusoyapps.venucity.remote.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.remote.entities.*
import javax.inject.Inject

class RemoteVenueMapper
@Inject constructor() : EntityMapper<DataLayerVenue, Restaurant> {

    override fun mapToRemoteEntity(type: DataLayerVenue): Restaurant {
        return Restaurant(
            ActiveMenu(type.id),
            listOf(
                RestaurantName(
                    lang = "en",
                    type.name,
                )
            ),
            listOf(
                RestaurantDesc(
                    lang = "en",
                    type.desc,
                )
            ),
            type.imageUrl,
            RestaurantLocation(
                coordinates = listOf(type.coordinate.latDoubleValue, type.coordinate.lngDoubleValue),
                type = "Point"
            )
        )
    }

    override fun mapToDataLayerEntity(type: Restaurant): DataLayerVenue {
        return type.run {
            DataLayerVenue(
                activeMenu.id,
                name.first().value,
                shortDescription.first().value,
                listImage,
                DataLayerLatLng(location.coordinates.first(), location.coordinates.last()),
                isFavorite = false,
            )
        }
    }

    override fun mapToDataLayerEntityList(type: List<Restaurant>): List<DataLayerVenue> {
        return type.map { mapToDataLayerEntity(it) }
    }

    override fun mapToRemoteEntityList(type: List<DataLayerVenue>): List<Restaurant> {
        return type.map { mapToRemoteEntity(it) }
    }
}
