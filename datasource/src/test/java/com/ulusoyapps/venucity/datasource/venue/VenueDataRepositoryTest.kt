package com.ulusoyapps.venucity.datasource.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocation
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.location.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMapper
import com.ulusoyapps.venucity.datasource.location.mapper.LocationMessageMapper
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueAddFailure
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenueDoesntExist
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenuesFetchError
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMapper
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMessageMapper
import com.ulusoyapps.venucity.domain.entities.*
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*

class VenueDataRepositoryTest {
    private val venueDatasource: VenueDataSource = mock()
    private val venueMapper: VenueMapper = mock()
    private val venueMessageMapper: VenueMessageMapper = mock()
    private val venueDataRepository = VenueDataRepository(
        localVenueDataSource = venueDatasource,
        venueMapper,
        venueMessageMapper
    )

    private val venue = Venue(
        "id",
        "name",
        "desc",
        "imageUrl",
        LatLng(0.0, 0.0),
    )

    private val dataLayerVenue = DataLayerVenue(
        "id",
        "name",
        "desc",
        "imageUrl",
        LatLng(0.0, 0.0),
    )

    private val dataLayerFlow = flow {
        emit(
            Ok(venue)
        )
        emit(
            Err(DataLayerVenueAddFailure),
        )
    }

    @Test
    fun `addVenue succeeds`() = runBlockingTest {
        whenever(venueMapper.mapToDataLayerEntity(venue)).thenReturn(dataLayerVenue)
        whenever(venueDatasource.addVenue(dataLayerVenue)).thenReturn(Ok(Unit))
        val actual = venueDataRepository.addVenue(venue)
        val expected = Ok(Unit)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `addVenue fails`() = runBlockingTest {
        whenever(venueMapper.mapToDataLayerEntity(venue)).thenReturn(dataLayerVenue)
        whenever(venueMessageMapper.mapToDomainEntity(DataLayerVenueAddFailure)).thenReturn(VenueAddFailure)
        whenever(venueDatasource.addVenue(dataLayerVenue)).thenReturn(Err(DataLayerVenueAddFailure))
        val actual = venueDataRepository.addVenue(venue)
        val expected = Err(VenueAddFailure)
        Truth.assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun `removeVenue succeeds`() = runBlockingTest {
        whenever(venueDatasource.removeVenue("id")).thenReturn(Ok(Unit))
        val actual = venueDataRepository.removeVenue("id")
        val expected = Ok(Unit)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `removeVenue fails`() = runBlockingTest {
        whenever(venueMessageMapper.mapToDomainEntity(DataLayerVenueDoesntExist)).thenReturn(VenueDoesntExist)
        whenever(venueDatasource.removeVenue("id")).thenReturn(Err(DataLayerVenueDoesntExist))
        val actual = venueDataRepository.removeVenue("id")
        val expected = Err(VenueDoesntExist)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getAllVenues() = runBlockingTest {
        whenever(venueMapper.mapToDomainEntityList(listOf(dataLayerVenue))).thenReturn(listOf(venue))
        whenever(venueMessageMapper.mapToDomainEntity(DataLayerVenuesFetchError)).thenReturn(VenuesFetchError)
        val venueFlow = flow {
            emit(Err(DataLayerVenuesFetchError))
            emit(Ok(listOf(dataLayerVenue)))
        }
        whenever(venueDatasource.getAllVenues()).thenReturn(venueFlow)
        venueDataRepository.getAllVenues().collectIndexed { index, value ->
            if (index == 0) {
                Truth.assertThat(value).isEqualTo(Err(VenuesFetchError))
            }
            if (index == 1) {
                Truth.assertThat(value.get()!!.first()).isEqualTo(venue)
            }
        }
    }
}