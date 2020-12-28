package com.ulusoyapps.venucity.remote.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Restaurant(
    @field:Json(name = "active_menu")
    val activeMenu: ActiveMenu,
    val name: List<RestaurantName>,
    @field:Json(name = "short_description")
    val shortDescription: List<RestaurantDesc>,
    @field:Json(name = "listimage")
    val listImage: String,
    val location: RestaurantLocation
) : Parcelable
