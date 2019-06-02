package com.shashavs.tmdbexplorer.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.repository.room.dao.MovieDao

@Database(entities = arrayOf(Movie::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}