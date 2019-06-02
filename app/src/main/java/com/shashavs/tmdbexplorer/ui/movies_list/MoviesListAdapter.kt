package com.shashavs.tmdbexplorer.ui.movies_list

import android.arch.paging.PagedListAdapter
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.shashavs.tmdbexplorer.R
import com.shashavs.tmdbexplorer.ui.base.RoundTransformation
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.util.DateTimeUtil
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_item.view.*

class MoviesListAdapter(private inline val listener: (Int) -> Unit) :
    PagedListAdapter<Movie, MoviesListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(p0: Movie, p1: Movie) = p0.id == p1.id

        override fun areContentsTheSame(p0: Movie, p1: Movie) = p0 == p1
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) holder.bind(item)
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

        private val thumbnail: ImageView = mView.thumbnail
        private val title: TextView = mView.title
        private val voteAverage: TextView = mView.vote_average
        private val releaseDate: TextView = mView.release_date

        fun bind(movie: Movie) {
            title.text = movie.title
            voteAverage.text = movie.vote_average.toString()

            val date= DateTimeUtil.getDate(movie.release_date)
            if(date != null) {
                val dateText = String.format(mView.context.getString(R.string.date_name), date)

                releaseDate.text = if(DateTimeUtil.isCurrentYear(date)) {
                    val index= dateText.indexOf(",").plus(1)
                    SpannableString(dateText).apply {
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            index,
                            dateText.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.red)),
                            index,
                            dateText.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                } else {
                    dateText
                }
            }

            Picasso.get()
                .load(mView.context.getString(R.string.poster_path, movie.poster_path))
                .placeholder(R.drawable.poster_place_holder)
                .transform(RoundTransformation(12f))
                .into(thumbnail)

            mView.setOnClickListener { listener(adapterPosition) }
        }
    }

}
