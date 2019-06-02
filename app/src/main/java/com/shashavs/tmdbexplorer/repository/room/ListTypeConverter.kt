package com.shashavs.tmdbexplorer.repository.room

import android.arch.persistence.room.TypeConverter

class ListTypeConverter {

    @TypeConverter
    fun listToString(data: List<Int>?)= data?.joinToString(";") ?: ""

    @TypeConverter
    fun stringToList(data: String) : List<Int> {
        return if(data.isEmpty()) {
            emptyList()
        } else {
            data.split(";").map { it.toInt() }
        }
    }
}