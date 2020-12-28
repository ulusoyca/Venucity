package com.ulusoyapps.venucity.remote.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ActiveMenu(
    @field:Json(name = "\$oid")
    val id: String,
) : Parcelable
