package com.ulusoyapps.venucity.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.domain.entities.DomainMessage
import com.ulusoyapps.venucity.domain.entities.FavoriteVenueInsertionSuccess
import com.ulusoyapps.venucity.domain.entities.FavoriteVenueRemovalSuccess
import com.ulusoyapps.venucity.domain.entities.Location
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.SuccessfulVenueOperation
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.domain.interactors.location.GetLiveLocationUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.AddFavoriteVenueUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.GetResolvedNearbyVenuesUseCase
import com.ulusoyapps.venucity.domain.interactors.venue.RemoveFavoriteVenueUseCase
import kotlinx.coroutines.flow.collect
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

    private val _currentCoordinate: MutableLiveData<Location> = MutableLiveData()
    private var maxAmountOfVenues = 15

    private val _venueOperationResultListener = MutableLiveData<DomainMessage>()
    val venueOperationResultListener: LiveData<DomainMessage>
        get() = _venueOperationResultListener

    // https://developer.android.com/topic/libraries/architecture/coroutines#livedata
    private val _resolvedNearbyVenues = _currentCoordinate.switchMap { location ->
        liveData(context = viewModelScope.coroutineContext + dispatcherProvider.io()) {
            getResolvedNearbyVenuesUseCase(location.latLng, maxAmountOfVenues).collect {
                it.mapBoth(
                    success = { venues ->
                        _venueOperationResultListener.postValue(SuccessfulVenueOperation())
                        emit(venues)
                    },
                    failure = { venueMessage ->
                        _venueOperationResultListener.postValue(venueMessage)
                    }
                )
            }
        }
    }

    private val _nearbyVenues = MediatorLiveData<List<Venue>>().apply {
        addSource(_resolvedNearbyVenues) {
            value = it
        }
        addSource(_venueOperationResultListener) { result ->
            value?.let {
                val currentValues = it.toMutableList()
                when (result) {
                    is FavoriteVenueInsertionSuccess -> {
                        val updatedList = currentValues.map { currentVenue ->
                            if (currentVenue.id == result.venue.id) {
                                currentVenue.copy(isFavorite = true)
                            } else {
                                currentVenue
                            }
                        }
                        value = updatedList
                    }
                    is FavoriteVenueRemovalSuccess -> {
                        val updatedList = currentValues.map { currentVenue ->
                            if (currentVenue.id == result.venue.id) {
                                currentVenue.copy(isFavorite = false)
                            } else {
                                currentVenue
                            }
                        }
                        value = updatedList
                    }
                    else -> {
                    }
                }
            }
        }
    }

    val nearbyVenues: LiveData<List<Venue>>
        get() = _nearbyVenues

    private val _locationErrorListener = MutableLiveData<LocationMessage>()
    val locationErrorListener: LiveData<LocationMessage>
        get() = _locationErrorListener

    fun onStartFetchingVenues(locationUpdateInterval: Long, maxAmount: Int) =
        viewModelScope.launch {
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
        viewModelScope.launch {
            addFavoriteVenueUseCase(venue).mapBoth(
                success = {
                    Timber.d("Venue ${venue.name} is added to favorites")
                    _venueOperationResultListener.postValue(FavoriteVenueInsertionSuccess(venue))
                },
                failure = { message ->
                    Timber.w("Error adding Venue ${venue.name} to favorites")
                    _venueOperationResultListener.postValue(message)
                }
            )
        }
    }

    fun onRemoveFavoriteVenue(venue: Venue) {
        viewModelScope.launch {
            removeFavoriteVenueUseCase(venue.id).mapBoth(
                success = {
                    Timber.d("Venue ${venue.name} is removed from favorites")
                    _venueOperationResultListener.postValue(FavoriteVenueRemovalSuccess(venue))
                },
                failure = { message ->
                    Timber.w("Error adding Venue ${venue.name} to favorites")
                    _venueOperationResultListener.postValue(message)
                }
            )
        }
    }
}
