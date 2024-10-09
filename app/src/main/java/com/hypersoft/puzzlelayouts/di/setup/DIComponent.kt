package com.hypersoft.puzzlelayouts.di.setup

import com.hypersoft.puzzlelayouts.di.domain.manager.InternetManager
import com.hypersoft.puzzlelayouts.di.domain.observers.GeneralObserver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent : KoinComponent {

    // Managers
    val internetManager by inject<InternetManager>()

    // Observers
    val generalObserver by inject<GeneralObserver>()

}