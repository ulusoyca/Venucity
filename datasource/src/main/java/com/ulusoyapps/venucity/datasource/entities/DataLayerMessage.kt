package com.ulusoyapps.venucity.datasource.entities

sealed class DataLayerMessage
object DataLayerNetworkError : DataLayerMessage()
object DataLayerHttpError : DataLayerMessage()

sealed class DataLayerVenueMessage: DataLayerMessage()
object DataLayerVenueAddFailure : DataLayerVenueMessage()
object DataLayerVenueDoesntExist : DataLayerVenueMessage()
object DataLayerVenuesFetchError : DataLayerVenueMessage()

sealed class DataLayerLocationMessage: DataLayerMessage()
object DataLayerLocationReadError : DataLayerLocationMessage()
object DataLayerLocationNotAvailable : DataLayerLocationMessage()



