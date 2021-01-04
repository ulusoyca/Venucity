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

package com.ulusoyapps.venucity.locationprovider

import androidx.annotation.RawRes
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.locationprovider.entity.LocationProviderMessage
import com.ulusoyapps.venucity.locationprovider.entity.MockLocation
import com.ulusoyapps.venucity.locationprovider.entity.SourceEmpty
import com.ulusoyapps.venucity.locationprovider.entity.SourceNotFound
import com.ulusoyapps.venucity.locationprovider.entity.SourceReadError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

class CsvLocationParser
@Inject constructor(
    private val inputStreamOpener: InputStreamOpener
) {
    suspend fun parseCsvFile(@RawRes csvFileResId: Int): Result<List<MockLocation>, LocationProviderMessage> = withContext(Dispatchers.IO) {
        try {
            val locations = mutableListOf<MockLocation>()
            val inputStream = inputStreamOpener.openStream(csvFileResId)
            inputStream?.run {
                val bufferedReader = bufferedReader()
                bufferedReader.useLines {
                    it.forEach { line ->
                        val coordinates: List<String> = line.split(",")
                        locations.add(MockLocation(coordinates[0].toDouble(), coordinates[1].toDouble(), timestamp = System.currentTimeMillis()))
                    }
                }
            }
            if (locations.isEmpty()) {
                Err(SourceEmpty)
            } else {
                Ok(locations)
            }
        } catch (e: FileNotFoundException) {
            Err(SourceNotFound)
        } catch (e: IOException) {
            Err(SourceReadError)
        }
    }
}
