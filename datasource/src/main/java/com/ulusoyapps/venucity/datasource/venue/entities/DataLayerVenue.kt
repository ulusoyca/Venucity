package com.ulusoyapps.venucity.datasource.venue.entities

import com.ulusoyapps.venucity.domain.entities.LatLng

data class DataLayerVenue(
    val id: String,
    val name: String,
    val desc: String,
    val imageUrl: String,
    val coordinate: LatLng
)
