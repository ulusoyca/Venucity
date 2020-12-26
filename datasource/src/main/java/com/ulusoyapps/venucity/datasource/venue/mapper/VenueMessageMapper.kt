package com.ulusoyapps.venucity.datasource.venue.mapper

import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationMessage
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.datasource.mapper.EntityMapper
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueAddFailure
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueDoesntExist
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueMessage
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenuesFetchError
import com.ulusoyapps.venucity.domain.entities.*
import javax.inject.Inject

class VenueMessageMapper
@Inject constructor() : EntityMapper<DataLayerVenueMessage, VenueMessage> {
    override fun mapToDomainEntity(type: DataLayerVenueMessage): VenueMessage {
        return when(type) {
            is DataLayerVenueAddFailure -> VenueAddFailure
            is DataLayerVenueDoesntExist -> VenueDoesntExist
            is DataLayerVenuesFetchError -> VenuesFetchError
        }
    }

    override fun mapToDataLayerEntity(type: VenueMessage): DataLayerVenueMessage {
        TODO("Not yet implemented")
    }

    override fun mapToDomainEntityList(type: List<DataLayerVenueMessage>): List<VenueMessage> {
        TODO("Not yet implemented")
    }

    override fun mapToDataLayerEntityList(type: List<VenueMessage>): List<DataLayerVenueMessage> {
        TODO("Not yet implemented")
    }
}