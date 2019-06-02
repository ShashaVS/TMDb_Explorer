package com.shashavs.tmdbexplorer.dagger.module

import android.arch.persistence.room.Room
import android.content.Context
import com.shashavs.tmdbexplorer.App
import com.shashavs.tmdbexplorer.R
import com.shashavs.tmdbexplorer.dagger.scope.AppScope
import com.shashavs.tmdbexplorer.repository.room.AppDatabase
import com.shashavs.tmdbexplorer.repository.AppRepository
import com.shashavs.tmdbexplorer.repository.Repository
import com.shashavs.tmdbexplorer.repository.retrofit.ApiService
import com.shashavs.tmdbexplorer.repository.retrofit.Api
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @AppScope
    @Provides
    fun getContext(app: App) = app.applicationContext

    @AppScope
    @Provides
    fun getApiService(context: Context): ApiService = Api().getApiService(context.getString(R.string.base_url))

    @AppScope
    @Provides
    fun getAppDatabase(context: Context): AppDatabase
            = Room.databaseBuilder(context, AppDatabase::class.java,"movie_database.db").build()

    @AppScope
    @Provides
    fun getRepository(apiService: ApiService, appDatabase: AppDatabase): Repository = AppRepository(apiService, appDatabase)

}