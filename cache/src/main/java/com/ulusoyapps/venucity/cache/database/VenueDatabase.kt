package com.ulusoyapps.venucity.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ulusoyapps.venucity.cache.dao.VenueDao
import com.ulusoyapps.venucity.cache.entities.CachedVenue

@Database(entities = [CachedVenue::class], version = 1, exportSchema = true)
abstract class VenueDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}