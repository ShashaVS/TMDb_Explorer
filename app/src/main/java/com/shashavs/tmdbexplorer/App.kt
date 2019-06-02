package com.shashavs.tmdbexplorer

import android.app.Activity
import android.app.Application
import com.shashavs.tmdbexplorer.dagger.component.DaggerAppComponent
import com.shashavs.tmdbexplorer.dagger.module.AppModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
         DaggerAppComponent.builder()
             .applicationBind(this)
             .appModule(AppModule())
             .build()
             .inject(this)
    }
}