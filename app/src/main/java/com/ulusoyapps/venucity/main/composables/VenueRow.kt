package com.ulusoyapps.venucity.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ulusoyapps.venucity.domain.entities.LatLng
import com.ulusoyapps.venucity.domain.entities.Venue

/**
 * Stateless composable that displays a full-width [Venue].
 *
 * @param venue venue to show
 * @param modifier modifier for this element
 */
@Composable
fun VenueRow(
    venue: Venue,
    modifier: Modifier = Modifier,
    onAddAsFavorite: (Venue) -> Unit,
    onRemoveFavorite: (String) -> Unit
) {
    Row(
        modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(venue.name)
        IconButton(
            content = {
                Icon(
                    imageVector = if (venue.isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                )
            },
            onClick = {
                if (venue.isFavorite == true) onRemoveFavorite(venue.id) else onAddAsFavorite(venue)
            },
        )
    }
}

@Preview
@Composable
fun PreviewVenueRow() {
    VenueRow(
        venue = Venue(
            name = "test",
            id = "id",
            desc = "desc",
            isFavorite = true,
            coordinate = LatLng(0.0, 0.0),
            imageUrl = "imageUrl"
        ),
        modifier = Modifier.fillMaxWidth(),
        {},
        {},
    )
}
