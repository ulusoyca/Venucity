package com.ulusoyapps.venucity.cache.dao

import androidx.room.*
import com.ulusoyapps.venucity.cache.entities.CachedVenue
import kotlinx.coroutines.flow.Flow

@Dao
interface VenueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVenue(venue: CachedVenue): Long

    @Query("SELECT * FROM CachedVenue WHERE id = :venueId")
    suspend fun getVenue(venueId: String): CachedVenue?

    @Query("DELETE FROM CachedVenue WHERE id = :venueId")
    suspend fun removeByVenueId(venueId: String): Int

    @Query("SELECT * FROM CachedVenue")
    fun getAllVenues(): Flow<List<CachedVenue>?>
}