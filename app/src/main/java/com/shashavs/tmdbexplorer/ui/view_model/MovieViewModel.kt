package com.shashavs.tmdbexplorer.ui.view_model

import android.arch.lifecycle.*

import android.arch.paging.LivePagedListBuilder
import javax.inject.Inject
import android.arch.paging.PagedList
import android.util.Log
import com.shashavs.tmdbexplorer.repository.Repository
import com.shashavs.tmdbexplorer.repository.data_objects.Movie

class MovieViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val TAG = "MovieViewModel"

    var pagedListLiveData: LiveData<PagedList<Movie>>? = null
    var position: Int = 0

    private val pagedConfig = PagedList.Config.Builder()
        .setPrefetchDistance(10)
        .setPageSize(15)
        .build()

    fun initDataSourceLiveData(apiKey : String) {
        pagedListLiveData = LivePagedListBuilder(repository.dataSourceFactory(), pagedConfig)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Movie>() {
                override fun onItemAtEndLoaded(itemAtEnd: Movie) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    repository.loadPage(apiKey, false)
                    Log.d(TAG, "BoundaryCallback onItemAtEndLoaded itemAtEnd: $itemAtEnd")
                }
            })
            .build()
    }

    fun refresh(apiKey : String) = repository.loadPage(apiKey, true)

    fun search(apiKey : String, query: String)= repository.search(apiKey, query)

    fun getQuery() = repository.query()

    fun getDetails(apiKey : String, id: Int)= repository.getDetails(id, apiKey)

    fun refreshLiveData() = repository.getRefreshLiveData()

    fun clear() {
        repository.clear()
        pagedListLiveData = null
    }

    override fun onCleared() {
        clear()
        super.onCleared()
    }
}
