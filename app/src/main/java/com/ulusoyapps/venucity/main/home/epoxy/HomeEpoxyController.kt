package com.ulusoyapps.venucity.main.home.epoxy

import com.airbnb.epoxy.EpoxyController
import com.ulusoyapps.venucity.domain.entities.Venue
import com.ulusoyapps.venucity.main.home.HomeViewModel
import com.ulusoyapps.venucity.venueCard

class HomeEpoxyController(
    private val viewModel: HomeViewModel
) : EpoxyController() {

    private var venues = listOf<Venue>()

    fun updateData(venues: List<Venue>) {
        this.venues = venues
        requestModelBuild()
    }

    override fun buildModels() {
        for (venue in venues) {
            venueCard {
                id(venue.id)
                venue(venue)
                viewmodel(viewModel)
            }
        }
    }
}
