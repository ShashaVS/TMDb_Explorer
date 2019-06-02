package com.shashavs.tmdbexplorer.repository.data_objects

import android.arch.persistence.room.*
import com.shashavs.tmdbexplorer.repository.room.ListTypeConverter

@Entity(tableName = "movie")
@TypeConverters(ListTypeConverter::class)
data class Movie(@PrimaryKey val id: Int,
                 val poster_path: String? = null,
                 val adult: Boolean? = null,
                 val overview: String? = null,
                 val release_date: String? = null,
                 val genre_ids: List<Int>? = null,
                 val original_title: String? = null,
                 val original_language: String? = null,
                 val title: String? = null,
                 val backdrop_path: String? = null,
                 val popularity: Float? = null,
                 val vote_count: Int? = null,
                 val video: Boolean? = null,
                 val vote_average: Float? = null,
                 //Extra fields
                 val budget: Int? = null,
                 val homepage: String? = null,
                 val imdb_id: String? = null,
                 val revenue: Long? = null,
                 val runtime: Int? = null,
                 val status: String? = null,
                 val tagline: String? = null)
