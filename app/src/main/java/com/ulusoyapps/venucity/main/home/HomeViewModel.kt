package com.ulusoyapps.venucity.main.home

import androidx.lifecycle.*
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.domain.entities.*
import com.ulusoyapps.venucity.domain.interactors.location.GetLiveLocationUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.AddFavoriteVenueUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.GetResolvedNearbyVenuesUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.RemoveFavoriteVenueUseCase
import kotlinx.coroutines.flow.collect
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

    private val _currentCoordinate: MutableLiveData<Location> = MutableLiveData()
    private var maxAmountOfVenues = 15

    // https://developer.android.com/topic/libraries/architecture/coroutines#livedata
    private val _nearbyVenues = _currentCoordinate.switchMap { location ->
        liveData(context = viewModelScope.coroutineContext + dispatcherProvider.io()) {
            getResolvedNearbyVenuesUseCase(location.latLng, maxAmountOfVenues).collect {
                it.mapBoth(
                    success = { venues ->
                        _venueOperationResultListener.postValue(SucceededVenueOperation)
                        emit(venues)
                    },
                    failure = { venueMessage ->
                        _venueOperationResultListener.postValue(venueMessage)
                    }
                )
            }
        }
    }
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
            maxAmountOfVenues = maxAmount
            getLiveLocationUseCase(locationUpdateInterval).collect { locationResult ->
                locationResult.mapBoth(
                    success = { location ->
                        _currentCoordinate.postValue(location)
                    },
                    failure = { message ->
                        _locationErrorListener.postValue(message)
                    }
                )
            }
        }

    fun onAddFavoriteVenue(venue: Venue) {
        viewModelScope.launch(dispatcherProvider.io()) {
            addFavoriteVenueUseCase(venue).mapBoth(
                success = { _venueOperationResultListener.postValue(SucceededVenueOperation) },
                failure = { message ->
                    _venueOperationResultListener.postValue(message)
                }
            )
        }
    }

    fun onRemoveFavoriteVenue(venueId: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeFavoriteVenueUseCase(venueId).mapBoth(
                success = { _venueOperationResultListener.postValue(SucceededVenueOperation) },
                failure = { message ->
                    _venueOperationResultListener.postValue(message)
                }
            )
        }
    }
}
