package com.ulusoyapps.venucity.remote.entities

sealed class RemoteVenueMessage

object VenueRemoteFetchError : RemoteVenueMessage()
object NetworkError : RemoteVenueMessage()
object HttpError : RemoteVenueMessage()
