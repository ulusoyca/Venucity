package com.ulusoyapps.venucity.datasource.location.entities

sealed class DataLayerLocationMessage

object DataLayerLocationReadError : DataLayerLocationMessage()
object DataLayerLocationNotAvailable : DataLayerLocationMessage()

