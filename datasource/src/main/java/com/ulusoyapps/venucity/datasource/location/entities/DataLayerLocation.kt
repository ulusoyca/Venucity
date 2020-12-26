package com.ulusoyapps.venucity.datasource.location.entities

import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng

data class DataLayerLocation(
    val latLng: DataLayerLatLng,
    val timestamp: Long,
)
