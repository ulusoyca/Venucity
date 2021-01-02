package com.ulusoyapps.venucity.di

import com.ulusoyapps.coroutines.DefaultDispatcherProvider
import com.ulusoyapps.coroutines.DispatcherProvider
import dagger.Binds
import dagger.Module

@Module
abstract class CoroutinesModule {
    // Use @Binds to tell Dagger which implementation it needs to use when providing an interface.
    @Binds
    abstract fun provideDispatcher(dispatcher: DefaultDispatcherProvider): DispatcherProvider
}
