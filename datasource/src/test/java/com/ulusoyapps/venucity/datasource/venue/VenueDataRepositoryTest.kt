package com.ulusoyapps.venucity.datasource.venue

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.datasource.entities.*
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.entities.DataLayerVenue
import com.ulusoyapps.venucity.datasource.venue.mapper.VenueMapper
import com.ulusoyapps.venucity.domain.entities.*
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class VenueDataRepositoryTest : BaseArchTest() {
    private val localVenueDatasource: VenueDataSource = mock()
    private val remoteVenueDatasource: VenueDataSource = mock()
    private val venueMapper: VenueMapper = mock()
    private val venueMessageMapper: DataLayerMessageMapper = mock()
    private val venueDataRepository = VenueDataRepository(
        localVenueDatasource,
        remoteVenueDatasource,
        venueMapper,
        venueMessageMapper,
        coroutinesTestRule.testDispatcherProvider,
    )

    private val venue = Venue(
        "id",
        "name",
        "desc",
        "imageUrl",
        LatLng(0.0, 0.0),
        isFavorite = true,
    )

    private val dataLayerVenue = DataLayerVenue(
        "id",
        "name",
        "desc",
        "imageUrl",
        DataLayerLatLng(0.0, 0.0),
        isFavorite = true,
    )

    @Test
    fun `addVenue succeeds`() = runBlockingTest {
        whenever(venueMapper.mapToDataLayerEntity(venue)).thenReturn(dataLayerVenue)
        whenever(localVenueDatasource.addVenue(dataLayerVenue)).thenReturn(Ok(Unit))
        val actual = venueDataRepository.addFavoriteVenue(venue)
        val expected = Ok(Unit)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `addVenue fails`() = runBlockingTest {
        whenever(venueMapper.mapToDataLayerEntity(venue)).thenReturn(dataLayerVenue)
        whenever(venueMessageMapper.mapToDomainEntity(DataLayerVenueAddFailure)).thenReturn(
            VenueAddFailure
        )
        whenever(localVenueDatasource.addVenue(dataLayerVenue)).thenReturn(
            Err(
                DataLayerVenueAddFailure
            )
        )
        val actual = venueDataRepository.addFavoriteVenue(venue)
        val expected = Err(VenueAddFailure)
        Truth.assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun `removeVenue succeeds`() = runBlockingTest {
        whenever(localVenueDatasource.removeVenue("id")).thenReturn(Ok(Unit))
        val actual = venueDataRepository.removeFavoriteVenue("id")
        val expected = Ok(Unit)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `removeVenue fails`() = runBlockingTest {
        whenever(venueMessageMapper.mapToDomainEntity(DataLayerVenueDoesntExist)).thenReturn(
            VenueDoesntExist
        )
        whenever(localVenueDatasource.removeVenue("id")).thenReturn(Err(DataLayerVenueDoesntExist))
        val actual = venueDataRepository.removeFavoriteVenue("id")
        val expected = Err(VenueDoesntExist)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getAllVenues() = runBlockingTest {
        whenever(venueMapper.mapToDomainEntityList(listOf(dataLayerVenue))).thenReturn(listOf(venue))
        whenever(venueMessageMapper.mapToDomainEntity(DataLayerVenuesFetchError)).thenReturn(
            VenuesFetchError
        )
        val venueFlow = flow {
            emit(Err(DataLayerVenuesFetchError))
            emit(Ok(listOf(dataLayerVenue)))
        }
        whenever(localVenueDatasource.getAllFavoriteVenues()).thenReturn(venueFlow)
        venueDataRepository.getAllFavoriteVenues().collectIndexed { index, value ->
            if (index == 0) {
                Truth.assertThat(value).isEqualTo(Err(VenuesFetchError))
            }
            if (index == 1) {
                Truth.assertThat(value.get()!!.first()).isEqualTo(venue)
            }
        }
    }

    @Test
    fun getResolvedNearbyVenues() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val latLng = LatLng(0.0, 0.0)
        val maxAmount = 15

        val localVenueId1 = Venue(
            "id1",
            "name",
            "desc",
            "imageUrl",
            LatLng(0.0, 0.0),
            isFavorite = true,
        )

        val localDataVenueId1 = DataLayerVenue(
            "id1",
            "name",
            "desc",
            "imageUrl",
            DataLayerLatLng(0.0, 0.0),
            isFavorite = true,
        )

        val remoteVenueId1 = Venue(
            "id1",
            "name",
            "desc",
            "imageUrl",
            LatLng(0.0, 0.0),
            isFavorite = null,
        )

        val remoteDataVenueId1 = DataLayerVenue(
            "id1",
            "name",
            "desc",
            "imageUrl",
            DataLayerLatLng(0.0, 0.0),
            isFavorite = null,
        )

        val remoteVenueId2 = Venue(
            "id2",
            "name",
            "desc",
            "imageUrl",
            LatLng(0.0, 0.0),
            isFavorite = null,
        )

        val remoteDataVenueId2 = DataLayerVenue(
            "id2",
            "name",
            "desc",
            "imageUrl",
            DataLayerLatLng(0.0, 0.0),
            isFavorite = null,
        )

        val remoteDataLayerVenues = listOf(
            remoteDataVenueId1,
            remoteDataVenueId2,
        )
        val remoteDomainLayerVenues = listOf(
            remoteVenueId1,
            remoteVenueId2,
        )
        val localDomainLayerVenues = listOf(localVenueId1)
        val localDataLayerVenues = listOf(localDataVenueId1)

        whenever(remoteVenueDatasource.getNearbyVenues(latLng, maxAmount)).thenReturn(
            Ok(remoteDataLayerVenues)
        )
        whenever(localVenueDatasource.getAllFavoriteVenues()).thenReturn(
            flow {
                emit(Ok(localDataLayerVenues))
            }
        )
        whenever(venueMapper.mapToDomainEntityList(remoteDataLayerVenues)).thenReturn(remoteDomainLayerVenues)
        whenever(venueMapper.mapToDomainEntityList(localDataLayerVenues)).thenReturn(localDomainLayerVenues)
        venueDataRepository.getResolvedNearbyVenues(latLng, maxAmount)
            .collectIndexed { index, value ->
                val value1 = remoteVenueId1.copy(isFavorite = true)
                val value2 = remoteVenueId2.copy(isFavorite = false)
                Truth.assertThat(value).isEqualTo(Ok(
                    listOf(value1, value2)
                ))
            }
    }
}