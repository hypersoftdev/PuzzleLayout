package com.hypersoft.puzzlelayouts.di.domain.observers

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.hypersoft.puzzlelayouts.utilities.observers.SingleLiveEvent

class GeneralObserver {

    val _navDashboardLiveData = SingleLiveEvent<Int>()
    val navDashboardLiveData: LiveData<Int> get() = _navDashboardLiveData

    val _navDashboardDirectionLiveData = SingleLiveEvent<NavDirections>()
    val navDashboardDirectionLiveData: LiveData<NavDirections> get() = _navDashboardDirectionLiveData

    val _navigationDirectionsMediaImageLiveData = SingleLiveEvent<NavDirections>()
    val navigationDirectionsMediaImageLiveData: LiveData<NavDirections> get() = _navigationDirectionsMediaImageLiveData

}