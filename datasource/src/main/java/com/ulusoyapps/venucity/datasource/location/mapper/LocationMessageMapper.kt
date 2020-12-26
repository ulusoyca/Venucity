package com.ulusoyapps.venucity.datasource.location.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.LocationNotAvailable
import com.ulusoyapps.venucity.domain.entities.LocationReadError
import javax.inject.Inject

class LocationMessageMapper
@Inject constructor() : EntityMapper<DataLayerLocationMessage, LocationMessage> {
    override fun mapToDomainEntity(type: DataLayerLocationMessage): LocationMessage {
        return when(type) {
            is DataLayerLocationNotAvailable -> LocationNotAvailable
            is DataLayerLocationReadError -> LocationReadError
        }
    }

    override fun mapToDataLayerEntity(type: LocationMessage): DataLayerLocationMessage {
        TODO("Not yet implemented")
    }

    override fun mapToDomainEntityList(type: List<DataLayerLocationMessage>): List<LocationMessage> {
        TODO("Not yet implemented")
    }

    override fun mapToDataLayerEntityList(type: List<LocationMessage>): List<DataLayerLocationMessage> {
        TODO("Not yet implemented")
    }
}