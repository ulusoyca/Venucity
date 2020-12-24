package com.ulusoyapps.venucity.domain.entities

data class Location(
    val latLng: LatLng,
    val timestamp: Long = 0
)
