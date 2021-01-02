package com.ulusoyapps.venucity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.ViewModelProvider
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.main.composables.VenuesListing
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onStartFetchingVenues(1000, 15)
        setContent {
            VenueListingScreen(viewModel)
        }
    }

    @Composable
    private fun VenueListingScreen(viewModel: MainViewModel) {
        val venues: List<Venue> by viewModel.nearbyVenues.observeAsState(listOf())
        return VenuesListing(
            venues = venues,
            onAddAsFavorite = viewModel::onAddFavoriteVenue,
            onRemoveFavorite = viewModel::onRemoveFavoriteVenue
        )
    }
}
