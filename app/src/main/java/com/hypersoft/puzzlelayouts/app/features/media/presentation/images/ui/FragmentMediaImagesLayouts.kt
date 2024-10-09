package com.hypersoft.puzzlelayouts.app.features.media.presentation.images.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.pzlayout.utils.PuzzlePiece
import com.hypersoft.pzlayout.view.PuzzleView
import com.hypersoft.puzzlelayouts.R
import com.hypersoft.puzzlelayouts.app.features.layouts.data.dataSource.PuzzleUtils
import com.hypersoft.puzzlelayouts.app.features.layouts.data.repository.RepoPuzzleUtils
import com.hypersoft.puzzlelayouts.app.features.layouts.domain.UseCasePuzzleLayouts
import com.hypersoft.puzzlelayouts.app.features.layouts.presentation.adapter.AdapterPuzzleLayoutsPieces
import com.hypersoft.puzzlelayouts.app.features.layouts.presentation.viewmodels.ViewModelPuzzleLayouts
import com.hypersoft.puzzlelayouts.app.features.layouts.presentation.viewmodels.ViewModelPuzzleLayoutsProvider
import com.hypersoft.puzzlelayouts.app.features.media.data.images.dataSources.MediaStoreMediaImages
import com.hypersoft.puzzlelayouts.app.features.media.data.images.repository.RepositoryMediaImages
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.useCases.UseCaseMediaImageDetail
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels.ViewModelMediaImageDetail
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels.ViewModelMediaImageDetailProvider
import com.hypersoft.puzzlelayouts.databinding.FragmentMediaImagesLayoutsBinding
import com.hypersoft.puzzlelayouts.utilities.base.fragment.BaseFragment
import com.hypersoft.puzzlelayouts.utilities.extensions.onBackPressedDispatcher
import com.hypersoft.puzzlelayouts.utilities.extensions.popFrom
import com.hypersoft.puzzlelayouts.utilities.extensions.showToast

class FragmentMediaImagesLayouts : BaseFragment<FragmentMediaImagesLayoutsBinding>(FragmentMediaImagesLayoutsBinding::inflate), PuzzleView.OnPieceClick, PuzzleView.OnPieceSelectedListener {

    private val mediaStoreMediaImages by lazy { MediaStoreMediaImages(context?.contentResolver) }
    private val repositoryMediaImages by lazy { RepositoryMediaImages(mediaStoreMediaImages) }
    private val useCaseMediaImageDetail by lazy { UseCaseMediaImageDetail(repositoryMediaImages) }
    private val viewModelMediaImageDetail by activityViewModels<ViewModelMediaImageDetail> { ViewModelMediaImageDetailProvider(useCaseMediaImageDetail) }

    // MVVM
    private val puzzleLayout by lazy { PuzzleUtils() }
    private val repoPuzzleUtils by lazy { RepoPuzzleUtils(puzzleLayout) }
    private val useCasePuzzleLayouts by lazy { UseCasePuzzleLayouts(repoPuzzleUtils) }
    private val viewModelPuzzleLayouts by viewModels<ViewModelPuzzleLayouts> { ViewModelPuzzleLayoutsProvider(useCasePuzzleLayouts) }

    private var mList: List<ItemMediaImagePhoto> = mutableListOf()
    private val adapterPuzzleLayoutsPieces by lazy { AdapterPuzzleLayoutsPieces(itemClick) }

    override fun onViewCreated() {
        registerBackPress()
        initRecyclerView()
        initObservers()
        setupListeners()
        initListener()
    }

    private fun initRecyclerView() {
        binding.rcvListPuzzleLayouts.adapter = adapterPuzzleLayoutsPieces
    }

    private fun initObservers() {
        viewModelMediaImageDetail.clickedImagesLiveData.observe(viewLifecycleOwner) {
            mList = it
            fetchLayouts(it)
            checkImageSizeAndSetLayouts(it)
        }
        viewModelPuzzleLayouts.puzzleLayoutLiveData.observe(viewLifecycleOwner) { list ->
            initView(list)
        }
        viewModelPuzzleLayouts.puzzleLayoutsLiveData.observe(viewLifecycleOwner) { list ->
            adapterPuzzleLayoutsPieces.setPuzzleLayouts(list)
        }
    }

    private fun setupListeners() = binding.apply {
        toolbarMediaImage.setOnClickListener { popFrom(R.id.fragmentMediaImagesLayouts) }
        pmirror.setOnClickListener { mirror() }
        pflip.setOnClickListener { flip() }
        protate.setOnClickListener { rotate() }
        pzoomplus.setOnClickListener { zoomPlus() }
        pzoomminus.setOnClickListener { zoomMinus() }
        pleft.setOnClickListener { left() }
        pright.setOnClickListener { right() }
        pup.setOnClickListener { up() }
        pdown.setOnClickListener { down() }
        btnCorner.setOnClickListener { corner() }
    }

    private fun initListener() = binding.apply {
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.puzzleView.setPieceRadian(progress.toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun fetchLayouts(it: List<ItemMediaImagePhoto>) {
        viewModelPuzzleLayouts.getPuzzleLayouts(it.size)
    }

    private fun checkImageSizeAndSetLayouts(it: List<ItemMediaImagePhoto>) {
        when (it.size) {
            1 -> {
                val selected = it[0]
                val imageList = listOf(selected, selected)
                viewModelPuzzleLayouts.getPuzzleLayout(1, imageList.size, 0)
            }

            2 -> {
                viewModelPuzzleLayouts.getPuzzleLayout(1, it.size, 0)
            }

            3 -> {
                viewModelPuzzleLayouts.getPuzzleLayout(1, it.size, 0)
            }

            4 -> {
                viewModelPuzzleLayouts.getPuzzleLayout(1, it.size, 0)
            }

            5 -> {
                viewModelPuzzleLayouts.getPuzzleLayout(1, it.size, 0)
            }

            else -> {

            }
        }
    }

    private fun initView(list: PuzzleLayout) {
        binding.puzzleView.apply {
            setPuzzleLayout(list)

            isTouchEnable = true
            needDrawLine = false
            needDrawOuterLine = false
            lineSize = 6
            lineColor = Color.BLACK
            selectedLineColor = ContextCompat.getColor(context, R.color.black)
            setHandleBarColor(ContextCompat.getColor(context, R.color.black))
            setAnimateDuration(700)
            piecePadding = 10f
            setOnPieceClickListener(this@FragmentMediaImagesLayouts)
            setOnPieceSelectedListener(this@FragmentMediaImagesLayouts)

            post {
                loadPhotoFromRes(list)
            }
        }
    }

    private fun loadPhotoFromRes(list: PuzzleLayout) {
        val pieces: MutableList<Bitmap> = ArrayList()

        // Determine the number of images to load
        val count = if (mList.size > list.areaCount) list.areaCount else mList.size

        for (i in 0 until count) {
            val target: CustomTarget<Bitmap> = object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap?>?) {
                    pieces.add(bitmap)
                    if (pieces.size == count) {
                        if (mList.size < list.areaCount) {
                            for (q in 0 until list.areaCount) {
                                binding.puzzleView.addPiece(pieces[i % count])
                            }
                        } else {
                            binding.puzzleView.addPieces(pieces)
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the case where the view is no longer visible or memory needs to be cleared
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // Handle errors (e.g., log or display a fallback drawable)
                }
            }

            // Load image resource using Glide
            Glide.with(this).asBitmap().load(mList[i].uri).into(target)
        }
    }

    private fun mirror() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.mirrorPiece()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun flip() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.flipPiece()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun rotate() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.rotatePiece()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun zoomPlus() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.zoomInPiece()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun zoomMinus() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.zoomOutPiece()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun left() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.moveLeft()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun right() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.moveRight()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun up() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.moveUp()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun down() = binding.apply {
        if (puzzleView.handlingPiecePosition != -1) {
            puzzleView.moveDown()
        } else {
            context?.showToast(R.string.selectsingleimage)
        }
    }

    private fun corner() = binding.apply {
        seekbar.visibility = View.VISIBLE
        seekbar.max = 100
        seekbar.progress = puzzleView.getPieceRadian().toInt()
    }

    override fun onPieceClick() {

    }

    override fun onSwapGetPositions(pos1: Int, pos2: Int) {

    }

    override fun onPieceSelected(piece: PuzzlePiece?, position: Int) {

    }

    private val itemClick: ((PuzzleLayout, theme: Int) -> Unit) = { puzzleLayout, theme ->
        viewModelPuzzleLayouts.getPuzzleLayout(1, mList.size, theme)
    }

    private fun registerBackPress() {
        onBackPressedDispatcher { popFrom(R.id.fragmentMediaImagesLayouts) }
    }

}