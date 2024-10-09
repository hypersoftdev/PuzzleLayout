package com.sample.puzzlelayout

import android.app.Application
import com.sample.puzzlelayout.di.setup.KoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    @OptIn(KoinExperimentalAPI::class)
    private fun initKoin() {
        val koinModules = KoinModules()
        startKoin {
            androidContext(this@MainApplication)
            modules(koinModules.mainModuleList)
            lazyModules(koinModules.backgroundModuleList)
        }
    }

}