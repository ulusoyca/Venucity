package com.ulusoyapps.venucity.datasource.entities

import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.domain.entities.*
import javax.inject.Inject

class DataLayerMessageMapper
@Inject constructor() : EntityMapper<DataLayerMessage, DomainMessage> {
    override fun mapToDomainEntity(type: DataLayerMessage): DomainMessage {
        return when(type) {
            DataLayerLocationNotAvailable -> LocationNotAvailable
            DataLayerLocationReadError -> LocationReadError
            DataLayerNetworkError -> NetworkError
            DataLayerHttpError -> HttpError
            DataLayerVenueAddFailure -> VenueAddFailure
            DataLayerVenueDoesntExist -> VenueDoesntExist
            DataLayerVenuesFetchError -> VenuesFetchError
        }
    }

    override fun mapToDataLayerEntity(type: DomainMessage): DataLayerMessage {
        TODO("Not yet implemented")
    }

    override fun mapToDomainEntityList(type: List<DataLayerMessage>): List<DomainMessage> {
        TODO("Not yet implemented")
    }

    override fun mapToDataLayerEntityList(type: List<DomainMessage>): List<DataLayerMessage> {
        TODO("Not yet implemented")
    }

}