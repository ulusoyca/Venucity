/*
 *  Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
