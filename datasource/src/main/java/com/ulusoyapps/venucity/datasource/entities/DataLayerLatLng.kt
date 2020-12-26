package com.ulusoyapps.venucity.datasource.entities

inline class DataLayerLatitude(val value: Float)
inline class DataLayerLongitude(val value: Float)

data class DataLayerLatLng(
    val latitude: DataLayerLatitude,
    val longitude: DataLayerLongitude
) {
    constructor(latitude: Double, longitude: Double) : this(
        DataLayerLatitude(latitude.toFloat()),
        DataLayerLongitude(longitude.toFloat())
    )
    val latDoubleValue: Double
        get() = latitude.value.toDouble()

    val lngDoubleValue: Double
        get() = longitude.value.toDouble()
}