package com.ulusoyapps.venucity.domain.entities

sealed class DomainMessage

open class VenueMessage : DomainMessage()
object VenueAddFailure : VenueMessage()
object VenueDoesntExist : VenueMessage()
object VenuesFetchError : VenueMessage()

object NetworkError : VenueMessage()
object HttpError : VenueMessage()

open class LocationMessage : DomainMessage()
object LocationReadError : LocationMessage()
object LocationNotAvailable : LocationMessage()
