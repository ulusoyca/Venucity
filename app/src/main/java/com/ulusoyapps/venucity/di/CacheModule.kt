package com.ulusoyapps.venucity.di

import android.content.Context
import androidx.room.Room
import com.ulusoyapps.venucity.cache.dao.VenueDao
import com.ulusoyapps.venucity.cache.database.VenueDatabase
import com.ulusoyapps.venucity.cache.datasources.CachedVenues
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueSource
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CacheModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideLocalDataSource(localOperations: CachedVenues): LocalVenueSource

    companion object {

        @Provides
        fun provideRoomDatabase(context: Context): VenueDatabase {
            return Room.databaseBuilder(
                context,
                VenueDatabase::class.java, "venucity_venues.db"
            ).build()
        }

        @Provides
        fun provideVenueDao(venueDatabase: VenueDatabase): VenueDao {
            return venueDatabase.venueDao()
        }
    }
}
