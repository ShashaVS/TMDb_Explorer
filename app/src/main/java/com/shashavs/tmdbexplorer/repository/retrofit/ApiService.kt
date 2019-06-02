package com.shashavs.tmdbexplorer.repository.retrofit

import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.repository.data_objects.PaginationData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /**
     * Get a list of movies in theatres.
     * Ex.: 3/movie/now_playing?api_key=<<api_key>>&language=en-US&page=1
     */
    @GET("3/movie/now_playing")
    fun nowPlaying(@Query("api_key") apiKey : String,
                   @Query("page") page : Int? = null,
                   @Query("language") language : String?="en-US") : Observable<Response<PaginationData<Movie>>>

    /**
     * Get the primary information about a movie.
     * Ex.: https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>&language=en-US
     */
    @GET("3/movie/{movie_id}")
    fun getDetail(@Path("movie_id") movie_id: Int,
                  @Query("api_key") apiKey : String) : Observable<Response<Movie>>

    /**
     * Search
     * Ex.: https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
     */
    @GET("3/search/movie")
    fun search(@Query("api_key") apiKey : String,
               @Query("page") page : Int? = null,
               @Query("query") query: String) : Observable<Response<PaginationData<Movie>>>
}