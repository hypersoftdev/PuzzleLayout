package com.sample.puzzlelayout.app.flow.home

import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.databinding.FragmentHomeBinding
import com.sample.puzzlelayout.utilities.base.fragment.BaseFragment
import com.sample.puzzlelayout.utilities.extensions.navigateTo
import com.sample.puzzlelayout.utilities.extensions.onBackPressedDispatcher

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