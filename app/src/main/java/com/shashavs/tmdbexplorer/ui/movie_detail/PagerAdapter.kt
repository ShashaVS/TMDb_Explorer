package com.shashavs.tmdbexplorer.ui.movie_detail

import android.arch.paging.AsyncPagedListDiffer
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.shashavs.tmdbexplorer.repository.data_objects.Movie

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var mDiffer: AsyncPagedListDiffer<Movie> = AsyncPagedListDiffer(object : ListUpdateCallback {

        override fun onChanged(p0: Int, p1: Int, p2: Any?) { }

        override fun onMoved(p0: Int, p1: Int) { notifyDataSetChanged() }

        override fun onInserted(p0: Int, p1: Int) { notifyDataSetChanged() }

        override fun onRemoved(p0: Int, p1: Int) { notifyDataSetChanged() }

    }, AsyncDifferConfig.Builder<Movie>(object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(p0: Movie, p1: Movie) = p0.id == p1.id

        override fun areContentsTheSame(p0: Movie, p1: Movie) = p0 == p1

    }).build())

    fun submit(pagedList: PagedList<Movie>?) {
        mDiffer.submitList(pagedList)
    }

    override fun getItem(position: Int)=
        MovieFragment().apply { arguments = Bundle().apply{ putInt("movieId", mDiffer.getItem(position)?.id!!) }}

    override fun getCount() = mDiffer.itemCount

}