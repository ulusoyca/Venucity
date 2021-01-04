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

package com.ulusoyapps.venucity.remote.datasource

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.datasource.entities.DataLayerHttpError
import com.ulusoyapps.venucity.datasource.entities.DataLayerLatLng
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.remote.mapper.RemoteVenueMapper
import com.ulusoyapps.venucity.remote.mapper.RemoteVenueMessageMapper
import com.ulusoyapps.venucity.remote.retrofit.RestaurantService
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class RestaurantSourceTest : BaseArchTest() {

    private val venueMapper: RemoteVenueMapper = RemoteVenueMapper()
    private val messageMapper: RemoteVenueMessageMapper = RemoteVenueMessageMapper()

    private val mockWebServer: MockWebServer = MockWebServer()

    private lateinit var restaurantService: RestaurantService
    private lateinit var restaurantSource: RestaurantSource

    val expectedVenues: List<DataLayerVenue> = listOf(
        DataLayerVenue(
            "5fb3e194a3eb79f1ee26db72",
            "Goodastrophe",
            "Turning a catastrophe into something delicious!",
            "https://prod-wolt-venue-images-cdn.wolt.com/5fb3e183624955b918c7bcd2/8baa8c84-2a47-11eb-b65f-e633568e2c00_goodastrophe.jpg",
            DataLayerLatLng(14.5206209, 35.8155991),
            isFavorite = null
        ),
        DataLayerVenue(
            "5fdb51789525083f5b218a8a",
            "Crazy Bites",
            "Serving traditional Maltese food and snacks",
            "https://prod-wolt-venue-images-cdn.wolt.com/5fdb4fdeb25521d5d2f517fd/9ea1626c-4064-11eb-9f03-9abb74d3858c_menu_img_1959_crazy_bites.jpg",
            DataLayerLatLng(14.474417, 35.8328225),
            isFavorite = null
        ),
    )

    @Before
    fun init() {
        mockWebServer.start()
        restaurantService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RestaurantService::class.java)

        restaurantSource = RestaurantSource(
            venueMapper,
            messageMapper,
            restaurantService,
        )
    }

    @Test
    fun `read sample success json file`() {
        val reader = MockResponseFileReader("success_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `fetch details and check response Code 200 returned`() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)
        val actualVenues = restaurantSource.getNearbyVenues(DataLayerLatLng(0.0, 0.0), 2)
        Truth.assertThat(actualVenues).isEqualTo(Ok(expectedVenues))
    }

    @Test
    fun `fetch details should return HTTP error`() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)
        val actualVenues = restaurantSource.getNearbyVenues(DataLayerLatLng(0.0, 0.0), 2)
        Truth.assertThat(actualVenues).isEqualTo(Err(DataLayerHttpError))
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }
}
