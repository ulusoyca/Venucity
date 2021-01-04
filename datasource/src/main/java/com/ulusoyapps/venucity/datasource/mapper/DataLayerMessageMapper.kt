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