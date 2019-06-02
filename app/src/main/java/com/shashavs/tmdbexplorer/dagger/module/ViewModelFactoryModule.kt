package com.shashavs.tmdbexplorer.dagger.module

import com.shashavs.tmdbexplorer.dagger.scope.ActivityScope
import com.shashavs.tmdbexplorer.ui.view_model.MovieViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule() {

    @ActivityScope
    @Binds
    abstract fun getMovieViewModelFactory(movieViewModelFactory: MovieViewModelFactory) : MovieViewModelFactory
}