package com.ulusoyapps.venucity.remote.mapper
import com.ulusoyapps.venucity.datasource.entities.*
import com.ulusoyapps.venucity.remote.entities.HttpError
import com.ulusoyapps.venucity.remote.entities.NetworkError
import com.ulusoyapps.venucity.remote.entities.RemoteVenueMessage
import com.ulusoyapps.venucity.remote.entities.VenueRemoteFetchError
import javax.inject.Inject

class RemoteVenueMessageMapper
@Inject constructor() : EntityMapper<DataLayerMessage, RemoteVenueMessage> {

    override fun mapToDataLayerEntity(type: RemoteVenueMessage): DataLayerMessage {
        return when (type) {
            NetworkError -> DataLayerNetworkError
            VenueRemoteFetchError -> DataLayerVenuesFetchError
            HttpError -> DataLayerHttpError
        }
    }

    override fun mapToDataLayerEntityList(type: List<RemoteVenueMessage>): List<DataLayerMessage> {
        throw NotImplementedError()
    }

    override fun mapToRemoteEntityList(type: List<DataLayerMessage>): List<RemoteVenueMessage> {
        throw NotImplementedError()
    }

    override fun mapToRemoteEntity(type: DataLayerMessage): RemoteVenueMessage {
        throw NotImplementedError()
    }
}
