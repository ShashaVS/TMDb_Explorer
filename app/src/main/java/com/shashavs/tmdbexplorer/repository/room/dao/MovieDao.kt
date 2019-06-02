package com.shashavs.tmdbexplorer.repository.room.dao

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.shashavs.tmdbexplorer.repository.data_objects.Movie

@Dao
interface MovieDao {

    @Query("SELECT id, poster_path, release_date, title, vote_average FROM movie ORDER BY popularity DESC")
    fun getMovieList(): DataSource.Factory<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(newsList : List<Movie>) : List<Long>

    @Query("SELECT * FROM movie WHERE id=:id")
    fun getMovieById(id: Int): LiveData<Movie>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovie(movie: Movie): Int

}