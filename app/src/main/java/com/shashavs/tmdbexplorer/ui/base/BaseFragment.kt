package com.shashavs.tmdbexplorer.ui.base

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.shashavs.tmdbexplorer.ui.view_model.MovieViewModelFactory
import com.shashavs.tmdbexplorer.ui.view_model.MovieViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

open class BaseFragment : Fragment() {

    lateinit var viewModel: MovieViewModel
    @Inject
    lateinit var viewModelFactory: MovieViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(MovieViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

}