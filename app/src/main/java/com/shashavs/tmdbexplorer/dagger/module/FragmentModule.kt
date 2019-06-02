package com.shashavs.tmdbexplorer.dagger.module

import com.shashavs.tmdbexplorer.dagger.scope.FragmentScope
import com.shashavs.tmdbexplorer.ui.movie_detail.MovieFragment
import com.shashavs.tmdbexplorer.ui.movie_detail.PagerFragment
import com.shashavs.tmdbexplorer.ui.movies_list.MoviesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMoviesListFragment(): MoviesListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePagerFragment(): PagerFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMovieFragment(): MovieFragment
}