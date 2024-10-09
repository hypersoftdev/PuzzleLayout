package com.sample.puzzlelayout.di.setup

import com.sample.puzzlelayout.di.domain.manager.InternetManager
import com.sample.puzzlelayout.di.domain.observers.GeneralObserver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent : KoinComponent {

    // Managers
    val internetManager by inject<InternetManager>()

    // Observers
    val generalObserver by inject<GeneralObserver>()

}