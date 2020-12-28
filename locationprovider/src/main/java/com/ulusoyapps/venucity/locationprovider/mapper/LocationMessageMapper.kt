package com.ulusoyapps.venucity.locationprovider.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.locationprovider.entity.*
import javax.inject.Inject

class LocationMessageMapper
@Inject constructor() : EntityMapper<DataLayerLocationMessage, LocationProviderMessage> {
    override fun mapToLocationProviderEntity(type: DataLayerLocationMessage): LocationProviderMessage {
        throw NotImplementedError()
    }

    override fun mapToDataLayerEntity(type: LocationProviderMessage): DataLayerLocationMessage {
        return when (type) {
            is SourceNotFound, SourceEmpty -> DataLayerLocationNotAvailable
            is SourceReadError -> DataLayerLocationReadError
        }
    }

    override fun mapToDataLayerEntityList(type: List<DataLayerLocationMessage>): List<LocationProviderMessage> {
        throw NotImplementedError()
    }

    override fun mapToLocationProviderEntityList(type: List<LocationProviderMessage>): List<DataLayerLocationMessage> {
        throw NotImplementedError()
    }
}
