package com.ulusoyapps.venucity.domain.entities

data class Venue(
    /**
     * The unique if of the venue
     */
    val id: String,
    /**
     * Name of the venue
     */
    val name: String,
    /**
     * Description of the venue
     */
    val desc: String,
    /**
     * Image URL for the venue
     */
    val imageUrl: String,
    /**
     * [LatLng] of the location in maps
     */
    val coordinate: LatLng
)
