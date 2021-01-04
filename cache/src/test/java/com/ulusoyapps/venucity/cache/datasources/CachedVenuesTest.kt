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

package com.ulusoyapps.venucity.cache.datasources

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.cache.dao.VenueDao
import com.ulusoyapps.venucity.cache.entities.*
import com.ulusoyapps.venucity.cache.mapper.CacheVenueMapper
import com.ulusoyapps.venucity.cache.mapper.CachedVenueMessageMapper
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenueAddFailure
import com.ulusoyapps.venucity.datasource.entities.DataLayerVenuesFetchError
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CachedVenuesTest: BaseArchTest() {

    private val venueDao: VenueDao = mock()
    private val venueMapper: CacheVenueMapper = mock()
    private val messageMapper: CachedVenueMessageMapper = mock()
    private val cachedVenues = CachedVenues(
        venueDao,
        venueMapper,
        messageMapper,
        coroutinesTestRule.testDispatcherProvider
    )

    private val cachedVenue = CachedVenue(
        "id",
        "name",
        "desc",
        "imageUrl",
        CachedLatLng(0.0f, 0.0f),
    )

    private val dataVenue = DataLayerVenue(
        "id",
        "name",
        "desc",
        "imageUrl",
        DataLayerLatLng(0.0, 0.0),
        isFavorite = true,
    )

    @Test
    fun `addVenue succeeds`() = runBlocking {
        whenever(venueMapper.mapToCacheEntity(dataVenue)).thenReturn(cachedVenue)
        whenever(venueDao.addVenue(cachedVenue)).thenReturn(1)
        val expected = cachedVenues.addFavoriteVenue(dataVenue)
        Truth.assertThat(expected).isEqualTo(Ok(Unit))
    }

    @Test
    fun `addVenue fails`() = runBlocking {
        whenever(venueMapper.mapToCacheEntity(dataVenue)).thenReturn(cachedVenue)
        whenever(venueDao.addVenue(cachedVenue)).thenReturn(-1L)
        whenever(messageMapper.mapToDataLayerEntity(VenueInsertionError)).thenReturn(DataLayerVenueAddFailure)
        val expected = cachedVenues.addFavoriteVenue(dataVenue)
        Truth.assertThat(expected).isEqualTo(Err(DataLayerVenueAddFailure))
    }

    @Test
    fun `should return all the venues`() = runBlocking {
        whenever(venueDao.getAllVenues()).thenReturn(flowOf(listOf(cachedVenue)))
        whenever(venueMapper.mapToDataLayerEntityList(listOf(cachedVenue))).thenReturn(listOf(dataVenue))
        cachedVenues.getAllFavoriteVenues().collect {
            Truth.assertThat(it).isEqualTo(Ok(listOf(dataVenue)))
        }
    }

    @Test
    fun `should return error if fetching all venues fails`() = runBlocking {
        whenever(venueDao.getAllVenues()).thenReturn(flowOf(null))
        whenever(messageMapper.mapToDataLayerEntity(VenuesFetchError)).thenReturn(DataLayerVenuesFetchError)
        cachedVenues.getAllFavoriteVenues().collect {
            Truth.assertThat(it).isEqualTo(Err(DataLayerVenuesFetchError))
        }
    }

    @Test
    fun `removeVenue succeeds`() = runBlocking {
        whenever(venueDao.removeByVenueId("cachedVenue")).thenReturn(1)
        val expected = cachedVenues.removeFavoriteVenue("cachedVenue")
        Truth.assertThat(expected).isEqualTo(Ok(Unit))
    }

    @Test
    fun `removeVenue fails`() = runBlocking {
        whenever(venueDao.removeByVenueId("cachedVenue")).thenReturn(0)
        whenever(messageMapper.mapToDataLayerEntity(VenueNotFound)).thenReturn(DataLayerVenuesFetchError)
        val expected = cachedVenues.removeFavoriteVenue("cachedVenue")
        Truth.assertThat(expected).isEqualTo(Err(DataLayerVenuesFetchError))
    }
}