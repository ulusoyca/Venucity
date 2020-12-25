package com.ulusoyapps.venucity.locationprovider.entity

sealed class LocationProviderMessage

object SourceNotFound : LocationProviderMessage()

object SourceReadError : LocationProviderMessage()

object SourceEmpty : LocationProviderMessage()
