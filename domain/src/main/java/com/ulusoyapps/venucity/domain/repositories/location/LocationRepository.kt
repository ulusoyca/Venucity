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

package com.ulusoyapps.venucity.domain.repositories.location

import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLiveLocation(
        locationUpdateIntervalTimeMillisec: Long,
        numberOfIntervals: Int = -1,
    ): Flow<Result<Location, LocationMessage>>
}
