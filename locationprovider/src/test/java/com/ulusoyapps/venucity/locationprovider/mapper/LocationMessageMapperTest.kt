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

package com.ulusoyapps.venucity.locationprovider.mapper

import com.google.common.truth.Truth
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.locationprovider.entity.SourceEmpty
import com.ulusoyapps.venucity.locationprovider.entity.SourceNotFound
import com.ulusoyapps.venucity.locationprovider.entity.SourceReadError
import org.junit.Test

class LocationMessageMapperTest {
    @Test
    fun mapToDataLayerEntityList() {
        val mapper = LocationMessageMapper()
        Truth.assertThat(mapper.mapToDataLayerEntity(SourceReadError)).isEqualTo(
            DataLayerLocationReadError
        )
        Truth.assertThat(mapper.mapToDataLayerEntity(SourceNotFound)).isEqualTo(
            DataLayerLocationNotAvailable
        )
        Truth.assertThat(mapper.mapToDataLayerEntity(SourceEmpty)).isEqualTo(
            DataLayerLocationNotAvailable
        )
    }
}
