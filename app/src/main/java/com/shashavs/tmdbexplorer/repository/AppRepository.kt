package com.shashavs.tmdbexplorer.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.repository.room.AppDatabase
import com.shashavs.tmdbexplorer.repository.retrofit.ApiService
import com.shashavs.tmdbexplorer.repository.data_objects.PaginationData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService,
                                        private val appDatabase: AppDatabase) : Repository {

    private val TAG = "Repository"
    private val compositeDisposable: CompositeDisposable
    private val refreshLiveData: MutableLiveData<Boolean>
    private val errorLiveData: MutableLiveData<String>
    var page: Int?
    var totalPages: Int?
    var query: String?

    init {
        compositeDisposable = CompositeDisposable()
        refreshLiveData = MutableLiveData()
        errorLiveData = MutableLiveData()
        page = 0
        totalPages = null
        query = null
    }

    override fun loadPage(apiKey: String, isRefresh: Boolean) {
        if(isRefresh) {
            page = 0
            totalPages = null
            query = null
        }
        if (page == totalPages) return

        if(query.isNullOrEmpty()) {
            nowPlaying(apiKey)
        } else {
            search(apiKey, query!!)
        }
    }

    override fun nowPlaying(apiKey : String) {
        apiService.nowPlaying(apiKey, page?.plus(1))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { refreshLiveData.postValue(true) }
            .doFinally { refreshLiveData.postValue(false) }
            .subscribe(
                { response: Response<PaginationData<Movie>>? ->
                    if(response?.code() == 200) {
                        parseResponse(response)
                    } else {
                        parseError(response?.errorBody())
                    }
                },
                { error -> Log.e(TAG, "nowPlaying error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    override fun search(apiKey : String, query: String) {
        if(this.query.isNullOrEmpty() || this.query != query) {
            this.query = query
            page = 0
            totalPages = null
        }
        apiService.search(apiKey, page?.plus(1), query)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { refreshLiveData.postValue(true) }
            .doFinally { refreshLiveData.postValue(false) }
            .subscribe(
                { response: Response<PaginationData<Movie>>? ->
                    if(response?.code() == 200) {
                        parseResponse(response)
                    } else {
                        parseError(response?.errorBody())
                    }
                },
                { error ->  Log.e(TAG, "nowPlaying error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    override fun getDetails(id: Int, apiKey: String): LiveData<Movie> {
        apiService.getDetail(id, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { refreshLiveData.postValue(true) }
            .doFinally { refreshLiveData.postValue(false) }
            .subscribe(
                { response: Response<Movie>? ->
                    if(response?.code() == 200) {
                        parseDetails(response)
                    } else {
                        parseError(response?.errorBody())
                    }
                },
                { error ->  Log.e(TAG, "nowPlaying error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
        return appDatabase.movieDao().getMovieById(id)
    }

    override fun parseResponse(response: Response<PaginationData<Movie>>?) {
        val pageResponse = response?.body()
        val results = pageResponse?.results
        if(results != null) {
            page = pageResponse.page
            totalPages = pageResponse.total_pages
            if(page == 1) {
                appDatabase.clearAllTables()
                Log.d(TAG, "clearAllTables")
            }
            val ids= appDatabase.movieDao().insertMovies(results)
            Log.d(TAG, "parseResponse ids: $ids")
        }
    }

    override fun parseDetails(response: Response<Movie>?) {
        val movie = response?.body()
        if(movie != null) {
            val movieId= appDatabase.movieDao().updateMovie(movie)
            Log.d(TAG, "update Movie id_movie: $movieId")
        }
    }

    override fun parseError(respBody: ResponseBody?) {
        try {
            val json = JSONObject(respBody?.string())
            val statusMessage= json.getString("status_message")
            errorLiveData.postValue(statusMessage)
        } catch (e: JSONException) {
            Log.e(TAG, "parseError JSONException: ", e)
        } catch (e: IOException) {
            Log.e(TAG, "parseError IOException: ", e)
        }
    }

    override fun query() = query

    override fun getRefreshLiveData(): LiveData<Boolean> = refreshLiveData

    override fun getErrorLiveData(): LiveData<String> = errorLiveData

    override fun dataSourceFactory()= appDatabase.movieDao().getMovieList()

    override fun clear() {
        compositeDisposable.clear()
        page = null
        totalPages = null
        query = null
        refreshLiveData.value = null
        errorLiveData.value = null
    }
}