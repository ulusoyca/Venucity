package com.ulusoyapps.venucity

import com.ulusoyapps.venucity.di.CacheModule
import com.ulusoyapps.venucity.di.CoroutinesModule
import com.ulusoyapps.venucity.di.DatasourceModule
import com.ulusoyapps.venucity.di.LocationModule
import com.ulusoyapps.venucity.di.RemoteModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Application component refers to application level modules only
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ContributeActivityModule::class,
        ViewModelModule::class,
        CacheModule::class,
        DatasourceModule::class,
        LocationModule::class,
        RemoteModule::class,
        CoroutinesModule::class,
    ]
)
interface AppComponent : AndroidInjector<VenucityApp> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<VenucityApp>
}
