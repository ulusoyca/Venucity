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
