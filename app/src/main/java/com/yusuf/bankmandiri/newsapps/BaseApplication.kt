package com.yusuf.bankmandiri.newsapps

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        with(FuelManager.instance){
            basePath = BuildConfig.API_URL
            baseHeaders = mapOf(
                "Content-Type" to "application/json; charset=utf-8",
                "User-Agent" to "Mozilla/5.0"
            )
        }
    }

}