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
