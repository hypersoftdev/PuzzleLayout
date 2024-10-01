package com.sample.puzzlelayout.utilities.base.fragment

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.sample.puzzlelayout.di.setup.DIComponent

abstract class BaseFragment<T : ViewBinding>(bindingFactory: (LayoutInflater) -> T) : ParentFragment<T>(bindingFactory) {

    protected val diComponent by lazy { DIComponent() }

}