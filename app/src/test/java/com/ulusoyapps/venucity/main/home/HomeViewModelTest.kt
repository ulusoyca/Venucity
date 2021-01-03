package com.ulusoyapps.venucity.main.home

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.FavoriteVenueInsertionSuccess
import com.ulusoyapps.venucity.domain.entities.FavoriteVenueRemovalSuccess
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationNotAvailable
import com.ulusoyapps.venucity.domain.entities.SuccessfulVenueOperation
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenueAddFailure
import com.ulusoyapps.venucity.domain.entities.VenuesFetchError
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

    private val firstLatLng = LatLng(0.0, 0.0)
    private val firstVenueList = listOf(
        Venue(
            id = "first",
            isFavorite = false,
            imageUrl = "imageUrl",
            name = "name",
            coordinate = firstLatLng,
            desc = "desc"
        )
    )

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
        Truth.assertThat(actualVenueOperationResult).isInstanceOf(SuccessfulVenueOperation::class.java)
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
        Truth.assertThat(actual).isInstanceOf(FavoriteVenueInsertionSuccess::class.java)
    }

    @Test
    fun `should fail adding a venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(addFavoriteVenueUseCase(venue)).thenReturn(Err(VenueAddFailure))
        homeViewModel.onAddFavoriteVenue(venue)
        val actual = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actual).isEqualTo(VenueAddFailure)
    }

    @Test
    fun `should remove venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(removeFavoriteVenueUseCase(venue.id)).thenReturn(Ok(Unit))
        homeViewModel.onRemoveFavoriteVenue(venue)
        val actual = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actual).isInstanceOf(FavoriteVenueRemovalSuccess::class.java)
    }

    @Test
    fun `should fail removing a venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(removeFavoriteVenueUseCase(venue.id)).thenReturn(Err(VenueAddFailure))
        homeViewModel.onRemoveFavoriteVenue(venue)
        val actual = homeViewModel.venueOperationResultListener.getOrAwaitValue()
        Truth.assertThat(actual).isEqualTo(VenueAddFailure)
    }
}
