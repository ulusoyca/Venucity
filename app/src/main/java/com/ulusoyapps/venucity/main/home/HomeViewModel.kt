package com.ulusoyapps.venucity.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.domain.entities.*
import com.ulusoyapps.venucity.domain.interactors.location.GetLiveLocationUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.AddFavoriteVenueUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.GetResolvedNearbyVenuesUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.RemoveFavoriteVenueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel
@Inject constructor(
    private val getLiveLocationUseCase: GetLiveLocationUseCase,
    private val addFavoriteVenueUseCase: AddFavoriteVenueUseCase,
    private val removeFavoriteVenueUseCase: RemoveFavoriteVenueUseCase,
    private val getResolvedNearbyVenuesUseCase: GetResolvedNearbyVenuesUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private var liveTrackingJob: Job? = null

    private val _nearbyVenues = MutableLiveData<List<Venue>>()
    val nearbyVenues: LiveData<List<Venue>>
        get() = _nearbyVenues

    private val _venueOperationResultListener = MutableLiveData<DomainMessage>()
    val venueOperationResultListener: LiveData<DomainMessage>
        get() = _venueOperationResultListener

    private val _locationErrorListener = MutableLiveData<LocationMessage>()
    val locationErrorListener: LiveData<LocationMessage>
        get() = _locationErrorListener

    fun onStartFetchingVenues(locationUpdateInterval: Long, maxAmount: Int) =
        viewModelScope.launch(dispatcherProvider.io()) {
            getLiveLocationUseCase(locationUpdateInterval)
                .map { locationResult ->
                    locationResult.mapBoth(
                        success = { location ->
                            getResolvedNearbyVenuesUseCase(location.latLng, maxAmount).collect {
                                it.mapBoth(
                                    success = { venues ->
                                        _venueOperationResultListener.postValue(SucceededVenueOperation)
                                        _nearbyVenues.postValue(venues)
                                    },
                                    failure = { venueMessage ->
                                        _venueOperationResultListener.postValue(venueMessage)
                                    }
                                )
                            }
                        },
                        failure = { message ->
                            _locationErrorListener.postValue(message)
                        }
                    )
                }.collect()
        }

    fun onAddFavoriteVenue(venue: Venue) {
        viewModelScope.launch(dispatcherProvider.io()) {
            addFavoriteVenueUseCase(venue).mapBoth(
                success = { _venueOperationResultListener.postValue(SucceededVenueOperation)},
                failure = { message ->
                    _venueOperationResultListener.postValue(message)
                }
            )
        }
    }

    fun onRemoveFavoriteVenue(venueId: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeFavoriteVenueUseCase(venueId).mapBoth(
                success = { _venueOperationResultListener.postValue(SucceededVenueOperation)},
                failure = { message ->
                    _venueOperationResultListener.postValue(message)
                }
            )
        }
    }
}