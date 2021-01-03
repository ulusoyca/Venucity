package com.ulusoyapps.venucity.main.home

import androidx.lifecycle.ViewModel
import com.ulusoyapps.venucity.FragmentScope
import com.ulusoyapps.venucity.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {
    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}
