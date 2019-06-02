package com.shashavs.tmdbexplorer.ui.movies_list

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import androidx.navigation.fragment.findNavController
import com.shashavs.tmdbexplorer.R

import com.shashavs.tmdbexplorer.ui.base.BaseFragment
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.search.SearchSuggestionProvider
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.toolbar
import android.util.DisplayMetrics

class MoviesListFragment : BaseFragment() {

    private var searchView: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            viewModel.initDataSourceLiveData(getString(R.string.api_key))
            viewModel.refresh(getString(R.string.api_key))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_item_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        viewModel.pagedListLiveData?.observe(viewLifecycleOwner, Observer { pagedList: PagedList<Movie>? ->
            if(list.adapter == null) {
                init(pagedList)
            } else {
                (list.adapter as MoviesListAdapter).submitList(pagedList)
            }
        })

        viewModel.refreshLiveData().observe(viewLifecycleOwner, Observer { refresh: Boolean? ->
            swipeRefresh.isRefreshing = refresh ?: false
            if(refresh == true) {
                toolbar.subtitle = viewModel.getQuery() ?: getString(R.string.now_playing)
            }
        })
    }

    private fun init(pagedList: PagedList<Movie>? ) {
        toolbar.subtitle = viewModel.getQuery() ?: getString(R.string.now_playing)

        val count = calculateGridCount(500)
        list.layoutManager = GridLayoutManager(context, count)
        list.setHasFixedSize(true)

        list.adapter = MoviesListAdapter{ position ->
            viewModel.position = position
            findNavController().navigate(R.id.action_moviesListFragment_to_pagerFragment)
        }.apply {
            submitList(pagedList)
        }
        (list.layoutManager as GridLayoutManager).scrollToPosition(viewModel.position)

        swipeRefresh.setColorSchemeResources(R.color.primaryLightColor)
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh(getString(R.string.api_key))
            searchView?.collapseActionView()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(list != null)
            viewModel.position = (list.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        searchView = menu?.findItem(R.id.search)

        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {

            setSearchableInfo(searchManager.getSearchableInfo(this@MoviesListFragment.requireActivity().componentName))
            setIconifiedByDefault(false)

            setOnQueryTextListener(object: SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    SearchRecentSuggestions(requireContext(), SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
                        .saveRecentQuery(query, null)
                    viewModel.search(getString(R.string.api_key), query!!)
                    searchView?.collapseActionView()
                    clearFocus()

                    return true
                }
                override fun onQueryTextChange(query: String?) = false
            })

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {

                override fun onSuggestionClick(positin: Int): Boolean {
                    val cursor = suggestionsAdapter.getItem(positin)
                    if(cursor is Cursor) {
                        val index = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
                        setQuery(cursor.getString(index), true)
                    }
                    return true
                }
                override fun onSuggestionSelect(positin: Int) = false
            })
        }
    }

    private fun calculateGridCount(width: Int) : Int {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels.div(width)
    }

    private fun clear() {
        list.adapter = null
        swipeRefresh.setOnRefreshListener(null)
        (searchView?.actionView as SearchView).apply {
            setOnQueryTextListener(null)
            setOnSuggestionListener(null)
        }
        searchView = null
    }

    override fun onDestroyView() {
        clear()
        super.onDestroyView()
    }

}
