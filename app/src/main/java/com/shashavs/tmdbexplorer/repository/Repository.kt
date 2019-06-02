package com.shashavs.tmdbexplorer.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.repository.data_objects.PaginationData
import okhttp3.ResponseBody
import retrofit2.Response

interface Repository {
    fun loadPage(apiKey : String, isRefresh: Boolean)
    fun nowPlaying(apiKey : String)
    fun search(apiKey : String, query: String)
    fun getDetails(id: Int, apiKey: String) : LiveData<Movie>
    fun parseResponse(response: Response<PaginationData<Movie>>?)
    fun parseDetails(response: Response<Movie>?)
    fun parseError(respBody: ResponseBody?)
    fun query(): String?
    fun getRefreshLiveData(): LiveData<Boolean>
    fun getErrorLiveData(): LiveData<String>
    fun dataSourceFactory() : DataSource.Factory<Int, Movie>
    fun clear()
}