package com.ulusoyapps.venucity.di

import android.content.Context
import androidx.room.Room
import com.ulusoyapps.coroutines.DefaultDispatcherProvider
import com.ulusoyapps.coroutines.DispatcherProvider
import com.ulusoyapps.venucity.cache.dao.VenueDao
import com.ulusoyapps.venucity.cache.database.VenueDatabase
import com.ulusoyapps.venucity.cache.datasources.CachedVenues
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueSource
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CoroutinesModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideDispatcher(dispatcher: DefaultDispatcherProvider): DispatcherProvider
}
