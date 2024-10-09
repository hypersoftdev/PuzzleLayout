package com.hypersoft.puzzlelayouts.app.flow.entrance.ui

import androidx.fragment.app.viewModels
import com.hypersoft.puzzlelayouts.app.flow.entrance.viewmodel.ViewModelEntrance
import com.hypersoft.puzzlelayouts.R
import com.hypersoft.puzzlelayouts.databinding.FragmentEntranceBinding
import com.hypersoft.puzzlelayouts.utilities.base.fragment.BaseFragment
import com.hypersoft.puzzlelayouts.utilities.extensions.navigateTo

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