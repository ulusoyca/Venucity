package com.ulusoyapps.venucity.di

import com.ulusoyapps.venucity.datasource.location.datasource.mock.MockLocationSource
import com.ulusoyapps.venucity.locationprovider.MockLocationProvider
import dagger.Binds
import dagger.Module

@Module
abstract class LocationModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideLocation(mockLocation: MockLocationProvider): MockLocationSource
}
