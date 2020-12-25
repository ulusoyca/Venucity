package com.ulusoyapps.venucity.locationprovider

import androidx.annotation.RawRes
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ulusoyapps.venucity.locationprovider.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
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
