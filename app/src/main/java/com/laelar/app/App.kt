package com.laelar.app

import android.app.Application
import com.laelar.app.assets.Injection
import com.laelar.core.assets.Resources.load
import com.naulian.anhance.applicationScope
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()

        applicationScope {
            load(this@App, Injection.map)
        }
    }
}