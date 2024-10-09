package com.hypersoft.puzzlelayouts.app.flow.home

import com.hypersoft.puzzlelayouts.R
import com.hypersoft.puzzlelayouts.databinding.FragmentHomeBinding
import com.hypersoft.puzzlelayouts.utilities.base.fragment.BaseFragment
import com.hypersoft.puzzlelayouts.utilities.extensions.navigateTo
import com.hypersoft.puzzlelayouts.utilities.extensions.onBackPressedDispatcher

class FragmentHome : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun onViewCreated() {
        registerBackPress()
        setupListeners()
    }

    private fun registerBackPress() {
        onBackPressedDispatcher { activity?.finish() }
    }

    private fun setupListeners() = binding.apply {
        mbtGallery.setOnClickListener { navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_nav_graph_media) }
        mbtLayouts.setOnClickListener { navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_nav_graph_layouts) }
        toolbarMediaImage.setOnClickListener { activity?.finish() }
    }

}