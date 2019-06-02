package com.shashavs.tmdbexplorer.dagger.module

import com.shashavs.tmdbexplorer.ui.MainActivity
import com.shashavs.tmdbexplorer.dagger.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ViewModelFactoryModule::class
    ])
    abstract fun contributeActivityAndroidInjector(): MainActivity
}