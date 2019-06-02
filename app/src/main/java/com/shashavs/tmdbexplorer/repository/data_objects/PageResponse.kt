package com.shashavs.tmdbexplorer.repository.data_objects

data class PaginationData<T>(val page: Int,
                             val total_pages: Int,
                             val total_results: Int,
                             val dates: Dates,
                             val results: List<T>?)

data class Dates(val maximum: String,
                 val minimum: String)