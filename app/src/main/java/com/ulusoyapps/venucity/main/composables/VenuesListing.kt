package com.ulusoyapps.venucity.main.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue

/**
 * Stateless component that is responsible for the entire venue listing.
 *
 * @param items (state) list of [Venue] to display
 * @param onAddAsFavorite (event) request an item be added
 * @param onRemoveFavorite (event) request an item be removed
 */
@Composable
fun VenuesListing(
    venues: List<Venue>,
    onAddAsFavorite: (Venue) -> Unit,
    onRemoveFavorite: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(venues) { venues ->
            VenueRow(venue = venues, modifier = Modifier, onAddAsFavorite, onRemoveFavorite)
        }
    }
}

@Preview
@Composable
fun VenuesListing() {
    val venue = Venue(
        name = "Venue 1",
        id = "id",
        desc = "desc",
        isFavorite = true,
        coordinate = LatLng(0.0, 0.0),
        imageUrl = "imageUrl"
    )
    val venues = listOf(venue, venue.copy(name = "Venue 2"), venue.copy(name = "Venue 3"))
    VenuesListing(
        venues,
        {},
        {},
    )
}
