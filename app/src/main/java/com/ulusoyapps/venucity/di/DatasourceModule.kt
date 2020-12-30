package com.ulusoyapps.venucity.di

import com.ulusoyapps.venucity.datasource.location.LocationDataRepository
import com.ulusoyapps.venucity.datasource.location.datasource.LocationDataSource
import com.ulusoyapps.venucity.datasource.location.datasource.mock.MockLocationDataSource
import com.ulusoyapps.venucity.datasource.venue.VenueDataRepository
import com.ulusoyapps.venucity.datasource.venue.datasource.VenueDataSource
import com.ulusoyapps.venucity.datasource.venue.datasource.local.LocalVenueDataSource
import com.ulusoyapps.venucity.datasource.venue.datasource.remote.RemoteVenueDataSource
import com.ulusoyapps.venucity.domain.repositories.location.LocationRepository
import com.ulusoyapps.venucity.domain.repositories.venue.VenueRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DatasourceModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideVenueRepository(repository: VenueDataRepository): VenueRepository

    @Binds
    abstract fun provideLocalVenueDataSource(datasource: LocalVenueDataSource): VenueDataSource

    @Binds
    abstract fun provideRemoteVenueDataSource(datasource: RemoteVenueDataSource): VenueDataSource

    @Binds
    abstract fun provideLocationRepository(repository: LocationDataRepository): LocationRepository

    @Binds
    abstract fun provideLocationDataSource(datasource: MockLocationDataSource): LocationDataSource
}
