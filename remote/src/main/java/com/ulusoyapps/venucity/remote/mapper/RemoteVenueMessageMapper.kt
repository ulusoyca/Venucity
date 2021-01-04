/*
 *  Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ulusoyapps.venucity.remote.mapper
import com.ulusoyapps.venucity.datasource.entities.DataLayerHttpError
import com.ulusoyapps.venucity.datasource.entities.DataLayerMessage
import com.ulusoyapps.venucity.datasource.entities.DataLayerNetworkError
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenuesFetchError
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
