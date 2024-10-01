package com.sample.puzzlelayout.di.setup

import com.sample.puzzlelayout.di.domain.manager.InternetManager
import com.sample.puzzlelayout.di.domain.observers.GeneralObserver
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.lazyModule
import org.koin.dsl.module

class KoinModules {

    /* ________________________________ Main Thread Modules ________________________________ */

    private val mainModules = module {
        single { InternetManager(androidContext()) }
    }

    /* ________________________________ Background Thread Modules ________________________________ */

    @OptIn(KoinExperimentalAPI::class)
    private val domainModules = lazyModule {
        single { GeneralObserver() }
    }

    /* ________________________________ Modules List ________________________________ */

    val mainModuleList = listOf(mainModules)
    val backgroundModuleList = listOf(domainModules)

}