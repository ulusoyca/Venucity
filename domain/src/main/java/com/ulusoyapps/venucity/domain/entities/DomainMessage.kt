package com.ulusoyapps.venucity.domain.entities

sealed class DomainMessage

open class VenueMessage : DomainMessage()

open class LocationMessage : DomainMessage()
object LocationReadError : LocationMessage()
object LocationNotAvailable : LocationMessage()