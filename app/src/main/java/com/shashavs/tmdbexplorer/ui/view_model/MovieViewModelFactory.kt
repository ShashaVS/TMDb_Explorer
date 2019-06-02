package com.shashavs.tmdbexplorer.ui.view_model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.shashavs.tmdbexplorer.repository.Repository
import javax.inject.Inject
import javax.inject.Provider

class MovieViewModelFactory @Inject constructor(private val repository: Provider<Repository>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MovieViewModel(repository.get()) as T
}