package com.ulusoyapps.venucity.remote.entities

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RestaurantLocation(
    val coordinates: List<Double>,
    val type: String,
) : Parcelable
