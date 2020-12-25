package com.ulusoyapps.venucity.domain.entities

inline class Latitude(val value: Float)
inline class Longitude(val value: Float)

data class LatLng(
    val latitude: Latitude,
    val longitude: Longitude
) {
    constructor(latitude: Double, longitude: Double) : this(
        Latitude(latitude.toFloat()),
        Longitude(longitude.toFloat())
    )
    val latDoubleValue: Double
        get() = latitude.value.toDouble()

    val lngDoubleValue: Double
        get() = longitude.value.toDouble()
}
