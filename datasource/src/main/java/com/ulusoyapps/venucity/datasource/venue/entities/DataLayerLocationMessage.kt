package com.ulusoyapps.venucity.datasource.venue.entities

import com.ulusoyapps.venucity.domain.entities.DomainMessage

sealed class DataLayerVenueMessage
object DataLayerVenueAddFailure : DataLayerVenueMessage()
object DataLayerVenueDoesntExist : DataLayerVenueMessage()
object DataLayerVenuesFetchError : DataLayerVenueMessage()

