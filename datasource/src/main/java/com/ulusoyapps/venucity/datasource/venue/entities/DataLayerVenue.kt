package com.ulusoyapps.venucity.datasource.venue.entities

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng

data class DataLayerVenue(
    val id: String,
    val name: String,
    val desc: String,
    val imageUrl: String,
    val coordinate: DataLayerLatLng,
    val isFavorite: Boolean,
)
