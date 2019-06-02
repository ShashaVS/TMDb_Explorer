package com.shashavs.tmdbexplorer.ui.movie_detail

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashavs.tmdbexplorer.R

import com.shashavs.tmdbexplorer.ui.base.BaseFragment
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import kotlinx.android.synthetic.main.fragment_pager.*

class PagerFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_pager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pagedListLiveData?.observe(viewLifecycleOwner, Observer { pagedList: PagedList<Movie>? ->
            if(viewPager.adapter == null) {
                init(pagedList)
            } else {
                (viewPager.adapter as PagerAdapter).submit(pagedList)
            }
        })
    }

    private fun init(pagedList: PagedList<Movie>?) {
        viewPager.adapter = PagerAdapter(childFragmentManager).also {
            it.submit(pagedList)
        }
        viewPager.currentItem = viewModel.position

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(p0: Int) {
                viewModel.position = viewPager.currentItem
            }
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageScrollStateChanged(p0: Int) { }
        })
    }

}
