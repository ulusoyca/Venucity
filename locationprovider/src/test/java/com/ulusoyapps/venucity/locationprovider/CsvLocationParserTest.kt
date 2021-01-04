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

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.locationprovider.entity.MockLocation
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CsvLocationParserTest : BaseArchTest() {

    private val inputStreamOpener: InputStreamOpener = InputStreamOpener(getTestContext(1001, "mock_locations.csv"))

    // TODO get this work!!!!
    @Test
    fun `should get locations from res file`() = runBlocking {
        val csvLocationParser = CsvLocationParser(inputStreamOpener)
        val parsedValue = csvLocationParser.parseCsvFile(1001)
        val expected = listOf(Ok(MockLocation(1.0, 2.0, 0)))
        Truth.assertThat(parsedValue.get()).isEqualTo(expected)
    }
}
