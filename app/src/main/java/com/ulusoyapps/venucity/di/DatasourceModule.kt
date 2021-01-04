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

import com.ulusoyapps.venucity.datasource.location.LocationDataRepository
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.datasource.mock.MockLocationDataSource
import com.ulusoyapps.venucity.datasource.venue.LOCAL_VENUE_DATA_SOURCE
import com.ulusoyapps.venucity.datasource.venue.REMOTE_VENUE_DATA_SOURCE
import com.ulusoyapps.venucity.datasource.venue.VenueDataRepository
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueDataSource
import com.ulusoyapps.venucity.datasource.venue.datasource.remote.RemoteVenueDataSource
import com.ulusoyapps.venucity.domain.repositories.location.LocationRepository
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class DatasourceModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideVenueRepository(repository: VenueDataRepository): VenueRepository

    @Binds
    @Named(LOCAL_VENUE_DATA_SOURCE)
    abstract fun provideLocalVenueDataSource(datasource: LocalVenueDataSource): VenueDataSource

    @Binds
    @Named(REMOTE_VENUE_DATA_SOURCE)
    abstract fun provideRemoteVenueDataSource(datasource: RemoteVenueDataSource): VenueDataSource

    @Binds
    abstract fun provideLocationRepository(repository: LocationDataRepository): LocationRepository

    @Binds
    abstract fun provideLocationDataSource(datasource: MockLocationDataSource): LocationDataSource
}
