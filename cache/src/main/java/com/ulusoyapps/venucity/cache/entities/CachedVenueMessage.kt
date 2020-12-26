package com.ulusoyapps.venucity.cache.entities

sealed class CachedVenueMessage

object VenueInsertionError : CachedVenueMessage()
object VenueNotFound : CachedVenueMessage()
object VenuesFetchError : CachedVenueMessage()