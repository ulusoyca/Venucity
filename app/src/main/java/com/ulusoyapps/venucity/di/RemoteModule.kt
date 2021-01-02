package com.ulusoyapps.venucity.di

import com.ulusoyapps.venucity.datasource.venue.datasource.remote.RemoteVenueSource
import com.ulusoyapps.venucity.remote.datasource.RestaurantSource
import com.ulusoyapps.venucity.remote.retrofit.RestaurantService
import com.ulusoyapps.venucity.remote.retrofit.ServiceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RemoteModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideRemoteDataSource(restaurantSource: RestaurantSource): RemoteVenueSource

    companion object {
        @Provides
        fun provideRestaurantService(): RestaurantService {
            return ServiceFactory.makeRestaurantService()
        }
    }
}
