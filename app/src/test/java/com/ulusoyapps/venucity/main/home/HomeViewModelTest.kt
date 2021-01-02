package com.ulusoyapps.venucity.main.home

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.*
import com.ulusoyapps.venucity.domain.interactors.location.GetLiveLocationUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.AddFavoriteVenueUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.GetResolvedNearbyVenuesUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.RemoveFavoriteVenueUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class HomeViewModelTest : BaseArchTest() {

    private val getLiveLocationUseCase: GetLiveLocationUseCase = mock()
    private val addFavoriteVenueUseCase: AddFavoriteVenueUseCase = mock()
    private val removeFavoriteVenueUseCase: RemoveFavoriteVenueUseCase = mock()
    private val getResolvedNearbyVenuesUseCase: GetResolvedNearbyVenuesUseCase = mock()

    private val homeViewModel = HomeViewModel(
        getLiveLocationUseCase,
        addFavoriteVenueUseCase,
        removeFavoriteVenueUseCase,
        getResolvedNearbyVenuesUseCase,
        coroutinesTestRule.testDispatcherProvider,
    )
    private val locations: List<Location> = listOf(
        Location(
            LatLng(0.0, 0.0),
            1,
        ),
        Location(
            LatLng(1.0, 1.0),
            2,
        ),
    )
    private val locationErrorMessage = LocationMessage()

    private val firstLatLng = LatLng(0.0, 0.0)
    private val secondLatLng = LatLng(1.0, 1.0)
    private val firstVenueList = listOf(Venue(
        id = "first",
        isFavorite = false,
        imageUrl = "imageUrl",
        name = "name",
        coordinate = firstLatLng,
        desc = "desc"
    ))
    private val secondVenueList = listOf(Venue(
        id = "second",
        isFavorite = false,
        imageUrl = "imageUrl",
        name = "name",
        coordinate = secondLatLng,
        desc = "desc"
    ))

    @Test
    fun `should start fetching venues`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val updateInterval: Long = 1
        val maxAmount = 3
        val locationFlow = flow {
            emit(Ok(Location(firstLatLng, 0)))
        }
        whenever(getLiveLocationUseCase(updateInterval)).thenReturn(locationFlow)
        whenever(getResolvedNearbyVenuesUseCase(firstLatLng, maxAmount)).thenReturn(
            flow {
                emit(Ok(firstVenueList))
            }
        )
        homeViewModel.onStartFetchingVenues(updateInterval, maxAmount)
        val actualVenues = homeViewModel.nearbyVenues.getOrAwaitValue()
        val actualVenueOperationResult = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actualVenues).isEqualTo(firstVenueList)
        Truth.assertThat(actualVenueOperationResult).isEqualTo(SucceededVenueOperation)
    }

    @Test
    fun `should fail fetching venues`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val updateInterval: Long = 1
        val maxAmount = 3
        val locationFlow = flow {
            emit(Ok(Location(firstLatLng, 0)))
        }
        whenever(getLiveLocationUseCase(updateInterval)).thenReturn(locationFlow)
        whenever(getResolvedNearbyVenuesUseCase(firstLatLng, maxAmount)).thenReturn(
            flow {
                emit(Err(VenuesFetchError))
            }
        )
        homeViewModel.onStartFetchingVenues(updateInterval, maxAmount)
        val actualVenueOperationResult = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actualVenueOperationResult).isEqualTo(VenuesFetchError)
    }

    @Test
    fun `should fail fetching location`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val updateInterval: Long = 1
        val maxAmount = 3
        val locationFlow = flow {
            emit(Err(LocationNotAvailable))
        }
        whenever(getLiveLocationUseCase(updateInterval)).thenReturn(locationFlow)
        homeViewModel.onStartFetchingVenues(updateInterval, maxAmount)
        val actual = homeViewModel.locationErrorListener.getOrAwaitValue()
        Truth.assertThat(actual).isEqualTo(LocationNotAvailable)
    }

    @Test
    fun `should add venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(addFavoriteVenueUseCase(venue)).thenReturn(Ok(Unit))
        homeViewModel.onAddFavoriteVenue(venue)
        val actual = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actual).isEqualTo(SucceededVenueOperation)
    }

    @Test
    fun `should fail adding a venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(addFavoriteVenueUseCase(venue)).thenReturn(Err(VenueAddFailure))
        homeViewModel.onAddFavoriteVenue(venue)
        val actual = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actual).isEqualTo(VenueAddFailure)
    }
}