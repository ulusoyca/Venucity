package com.ulusoyapps.venucity.main.home

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationNotAvailable
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

    private val homeViewModel by lazy {
        HomeViewModel(
            getLiveLocationUseCase,
            addFavoriteVenueUseCase,
            removeFavoriteVenueUseCase,
            getResolvedNearbyVenuesUseCase,
            coroutinesTestRule.testDispatcherProvider,
        )
    }

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
        val initiallUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat(initiallUiState).isEqualTo(VenuesUiState.Loading)
        homeViewModel.onStartFetchingVenues(updateInterval, maxAmount)
        val actualUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat((actualUiState as VenuesUiState.Success).venues).isEqualTo(firstVenueList)
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
        val actualUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat((actualUiState as VenuesUiState.Error).message).isEqualTo(VenuesFetchError)
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
        val actualUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat((actualUiState as VenuesUiState.Error).message).isEqualTo(VenuesFetchError)
    }

    @Test
    fun `should add fav venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val notFavoriteVenue = Venue(
            id = "notFavorite",
            isFavorite = false,
            imageUrl = "imageUrl",
            name = "name",
            coordinate = firstLatLng,
            desc = "desc"
        )
        val notFavoriteVenues = listOf(notFavoriteVenue)
        homeViewModel._uiState.value = VenuesUiState.Success(notFavoriteVenues)
        whenever(addFavoriteVenueUseCase(notFavoriteVenue)).thenReturn(Ok(Unit))
        homeViewModel.onAddFavoriteVenue(notFavoriteVenue)
        val actualUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat((actualUiState as VenuesUiState.Success).venues.first().isFavorite).isTrue()
    }

    @Test
    fun `should fail adding a fav venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(addFavoriteVenueUseCase(venue)).thenReturn(Err(VenueAddFailure))
        homeViewModel.onAddFavoriteVenue(venue)
        verify(addFavoriteVenueUseCase).invoke(venue)
    }

    @Test
    fun `should remove fav venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val favoriteVenue = Venue(
            id = "notFavorite",
            isFavorite = true,
            imageUrl = "imageUrl",
            name = "name",
            coordinate = firstLatLng,
            desc = "desc"
        )
        val favoriteVenues = listOf(favoriteVenue)
        homeViewModel._uiState.value = VenuesUiState.Success(favoriteVenues)
        whenever(removeFavoriteVenueUseCase(favoriteVenue.id)).thenReturn(Ok(Unit))
        homeViewModel.onRemoveFavoriteVenue(favoriteVenue)
        val actualUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat((actualUiState as VenuesUiState.Success).venues.first().isFavorite).isFalse()
    }

    @Test
    fun `should fail removing a fav venue`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val venue = firstVenueList.first()
        whenever(removeFavoriteVenueUseCase(venue.id)).thenReturn(Err(VenueAddFailure))
        homeViewModel.onRemoveFavoriteVenue(venue)
        val actualUiState = homeViewModel.uiState.getOrAwaitValue()
        Truth.assertThat((actualUiState as VenuesUiState.Error).message).isEqualTo(VenueAddFailure)
    }
}
