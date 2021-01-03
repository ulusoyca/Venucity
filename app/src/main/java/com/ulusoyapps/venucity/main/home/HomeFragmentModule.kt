package com.ulusoyapps.venucity.main.home
import com.ulusoyapps.venucity.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class HomeFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun bindFragment(): HomeFragment
}
