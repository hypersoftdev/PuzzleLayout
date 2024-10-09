package com.sample.puzzlelayout.app.flow.entrance.ui

import androidx.fragment.app.viewModels
import com.sample.puzzlelayout.app.flow.entrance.viewmodel.ViewModelEntrance
import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.databinding.FragmentEntranceBinding
import com.sample.puzzlelayout.utilities.base.fragment.BaseFragment
import com.sample.puzzlelayout.utilities.extensions.navigateTo

class FragmentEntrance : BaseFragment<FragmentEntranceBinding>(FragmentEntranceBinding::inflate) {

    private val viewModel by viewModels<ViewModelEntrance>()

    override fun onViewCreated() {
        initObserver()
    }

    private fun initObserver() {
        viewModel.navigateLiveData.observe(viewLifecycleOwner) {
            navigateScreen()
        }
    }

    private fun navigateScreen() {
        if (checkStoragePermission()) {
            navigateTo(R.id.fragmentEntrance, R.id.action_global_fragmentHome)
        } else {
            navigateTo(R.id.fragmentEntrance, R.id.action_fragmentEntrance_to_fragmentPermission)
        }
    }
}