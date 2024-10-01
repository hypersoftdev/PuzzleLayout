package com.sample.puzzlelayout.app.features.layouts.presentation.ui

import androidx.fragment.app.viewModels
import com.sample.puzzlelayout.app.features.layouts.presentation.viewmodels.ViewModelPuzzleLayouts
import com.sample.puzzlelayout.app.features.layouts.presentation.viewmodels.ViewModelPuzzleLayoutsProvider
import com.puzzle.layouts.interfaces.PuzzleLayout
import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.app.features.layouts.data.dataSource.PuzzleUtils
import com.sample.puzzlelayout.app.features.layouts.data.repository.RepoPuzzleUtils
import com.sample.puzzlelayout.app.features.layouts.domain.UseCasePuzzleLayouts
import com.sample.puzzlelayout.app.features.layouts.presentation.adapter.AdapterPuzzleLayouts
import com.sample.puzzlelayout.databinding.FragmentLayoutsBinding
import com.sample.puzzlelayout.utilities.base.fragment.BaseFragment
import com.sample.puzzlelayout.utilities.extensions.navigateTo
import com.sample.puzzlelayout.utilities.extensions.popFrom


class FragmentLayouts : BaseFragment<FragmentLayoutsBinding>(FragmentLayoutsBinding::inflate) {

    private val adapterPuzzleLayouts by lazy { AdapterPuzzleLayouts(itemClick) }

    // MVVM
    private val puzzleLayout by lazy { PuzzleUtils() }
    private val repoPuzzleUtils by lazy { RepoPuzzleUtils(puzzleLayout) }
    private val useCasePuzzleLayouts by lazy { UseCasePuzzleLayouts(repoPuzzleUtils) }
    private val viewModelPuzzleLayouts by viewModels<ViewModelPuzzleLayouts> { ViewModelPuzzleLayoutsProvider(useCasePuzzleLayouts) }

    private var mTheme: Int = 0
    private var mPuzzleLayout: PuzzleLayout? = null

    override fun onViewCreated() {
        initRecyclerView()
        initObservers()
        setupListeners()
    }

    private fun initRecyclerView() {
        binding.rcvListPuzzleLayouts.adapter = adapterPuzzleLayouts
    }

    private fun initObservers() {
        viewModelPuzzleLayouts.puzzleAllLayoutsLiveData.observe(viewLifecycleOwner) { list ->
            adapterPuzzleLayouts.setPuzzleLayouts(list) // Use your custom method
        }
        viewModelPuzzleLayouts.isSlantLiveData.observe(viewLifecycleOwner) {
            moveNext(it)
        }
    }

    private fun moveNext(isSlantPuzzleLayout: Boolean) {
        val action = if (isSlantPuzzleLayout) {
            FragmentLayoutsDirections.actionFragmentLayouts2ToFragmentLayoutWithImages(
                theme = mTheme,
                size = mPuzzleLayout?.areaCount ?: return,
                type = 0
            )
        } else {
            FragmentLayoutsDirections.actionFragmentLayouts2ToFragmentLayoutWithImages(
                theme = mTheme,
                size = mPuzzleLayout?.areaCount ?: return,
                type = 1
            )
        }
        navigateTo(R.id.fragmentLayouts2, action)
    }

    private fun setupListeners() = binding.apply {
        toolbarMediaImage.setOnClickListener { popFrom(R.id.fragmentLayouts2) }
    }

    private val itemClick: ((PuzzleLayout, theme: Int) -> Unit) = { puzzleLayout, theme ->
        mTheme = theme
        mPuzzleLayout = puzzleLayout
        viewModelPuzzleLayouts.layoutClick(puzzleLayout)
    }
}