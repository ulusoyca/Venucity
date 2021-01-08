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

package com.ulusoyapps.venucity.main.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.domain.entities.DomainMessage
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.entities.VenuesFetchError
import com.ulusoyapps.venucity.domain.interactors.location.GetLiveLocationUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.AddFavoriteVenueUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.GetResolvedNearbyVenuesUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.RemoveFavoriteVenueUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel
@Inject constructor(
    private val getLiveLocationUseCase: GetLiveLocationUseCase,
    private val addFavoriteVenueUseCase: AddFavoriteVenueUseCase,
    private val removeFavoriteVenueUseCase: RemoveFavoriteVenueUseCase,
    private val getResolvedNearbyVenuesUseCase: GetResolvedNearbyVenuesUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private var maxAmountOfVenues = 15

    private var locationTrackingJob: Job? = null

    @VisibleForTesting
    val _uiState = MutableLiveData<VenuesUiState>().apply {
        value = VenuesUiState.Loading
    }
    val uiState: LiveData<VenuesUiState>
        get() = _uiState

    fun onStartFetchingVenues(locationUpdateInterval: Long, maxAmount: Int) {
        locationTrackingJob?.cancel()
        locationTrackingJob = viewModelScope.launch(dispatcherProvider.io()) {
            maxAmountOfVenues = maxAmount
            getLiveLocationUseCase(locationUpdateInterval).flatMapLatest { locationResult ->
                locationResult.mapBoth(
                    success = { location ->
                        getResolvedNearbyVenuesUseCase(location.latLng, maxAmountOfVenues)
                    },
                    failure = { message ->
                        flow {
                            emit(Err(VenuesFetchError))
                        }
                    }
                )
            }.collect {
                it.mapBoth(
                    success = { venues ->
                        _uiState.postValue(VenuesUiState.Success(venues))
                    },
                    failure = { venueMessage ->
                        _uiState.postValue(VenuesUiState.Error(venueMessage))
                    }
                )
            }
        }
    }

    fun onAddFavoriteVenue(venue: Venue) {
        viewModelScope.launch(dispatcherProvider.io()) {
            addFavoriteVenueUseCase(venue).mapBoth(
                success = {
                    Timber.d("Venue ${venue.name} is added to favorites")
                    val currentState = uiState.value
                    if (currentState is VenuesUiState.Success) {
                        val currentValues = currentState.venues.toMutableList()
                        val updatedList = currentValues.map { currentVenue ->
                            if (currentVenue.id == venue.id) {
                                currentVenue.copy(isFavorite = true)
                            } else {
                                currentVenue
                            }
                        }
                        _uiState.postValue(VenuesUiState.Success(updatedList))
                    }
                },
                failure = { message ->
                    Timber.w("Error adding Venue ${venue.name} to favorites")
                    _uiState.postValue(VenuesUiState.Error(message))
                }
            )
        }
    }

    fun onRemoveFavoriteVenue(venue: Venue) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeFavoriteVenueUseCase(venue.id).mapBoth(
                success = {
                    Timber.d("Venue ${venue.name} is removed from favorites")
                    val currentState = uiState.value
                    if (currentState is VenuesUiState.Success) {
                        val currentValues = currentState.venues
                        val updatedList = currentValues.map { currentVenue ->
                            if (currentVenue.id == venue.id) {
                                currentVenue.copy(isFavorite = false)
                            } else {
                                currentVenue
                            }
                        }
                        _uiState.postValue(VenuesUiState.Success(updatedList))
                    }
                },
                failure = { message ->
                    Timber.w("Error adding Venue ${venue.name} to favorites")
                    _uiState.postValue(VenuesUiState.Error(message))
                }
            )
        }
    }

    fun onStop() {
        cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }

    private fun cancelJobs() {
        locationTrackingJob?.cancel()
        locationTrackingJob = null
    }
}

// Represents different states for the LatestNews screen
sealed class VenuesUiState {
    data class Success(val venues: List<Venue>) : VenuesUiState()
    data class Error(val message: DomainMessage) : VenuesUiState()
    object Loading : VenuesUiState()
}
