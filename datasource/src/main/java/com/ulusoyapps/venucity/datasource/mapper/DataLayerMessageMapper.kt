package com.ulusoyapps.venucity.datasource.mapper

import com.ulusoyapps.venucity.datasource.entities.DataLayerHttpError
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessage
import com.ulusoyapps.venucity.datasource.entities.DataLayerNetworkError
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueAddFailure
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueDoesntExist
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenuesFetchError
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