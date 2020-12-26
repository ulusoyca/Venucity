package com.ulusoyapps.venucity.cache.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CachedVenue(
    @PrimaryKey val id: String,
    val name: String,
    val desc: String,
    val imageUrl: String,
    @Embedded val coordinate: CachedLatLng
)