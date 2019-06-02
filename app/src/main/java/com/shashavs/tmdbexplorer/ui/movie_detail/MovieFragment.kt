package com.shashavs.tmdbexplorer.ui.movie_detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shashavs.tmdbexplorer.R
import com.shashavs.tmdbexplorer.ui.base.BaseFragment
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.util.DateTimeUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : BaseFragment() {

    private var movieId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movieId")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_movie, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(movieId != null) viewModel.getDetails(getString(R.string.api_key), movieId!!)
            .observe(viewLifecycleOwner, Observer { movie: Movie? ->
                if(movie != null) init(movie)
            })

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun init(movie: Movie) {
        toolbar.title = movie.title
        vote_average.text = movie.vote_average.toString()

        val date = DateTimeUtil.getDate(movie.release_date)
        release_date.text = String.format(getString(R.string.date_name), date)

        if(movie.revenue != null && movie.revenue > 0)
            revenue.text = String.format(getString(R.string.revenue), movie.revenue.div(1000000))

        if(movie.runtime != null && movie.runtime > 0)
            runtime.text = String.format(getString(R.string.runtime), movie.runtime)

        overview.text = movie.overview
        homepage.text = movie.homepage

        Picasso.get()
            .load(getString(R.string.backdrop_path, movie.backdrop_path))
            .placeholder(R.drawable.backdrop_place_holder)
            .fit()
            .centerCrop()
            .into(backdrop)
    }

}
