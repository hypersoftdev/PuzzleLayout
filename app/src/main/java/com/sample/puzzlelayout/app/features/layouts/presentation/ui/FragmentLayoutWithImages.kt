package com.sample.puzzlelayout.app.features.layouts.presentation.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sample.puzzlelayout.app.features.layouts.presentation.viewmodels.ViewModelPuzzleLayouts
import com.sample.puzzlelayout.app.features.layouts.presentation.viewmodels.ViewModelPuzzleLayoutsProvider
import com.puzzle.layouts.interfaces.PuzzleLayout
import com.puzzle.layouts.utils.PuzzlePiece
import com.puzzle.layouts.view.PuzzleView
import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.app.features.layouts.data.dataSource.PuzzleUtils
import com.sample.puzzlelayout.app.features.layouts.data.repository.RepoPuzzleUtils
import com.sample.puzzlelayout.app.features.layouts.domain.UseCasePuzzleLayouts
import com.sample.puzzlelayout.databinding.FragmentLayoutWithImagesBinding
import com.sample.puzzlelayout.utilities.base.fragment.BaseFragment
import com.sample.puzzlelayout.utilities.extensions.popFrom
import com.sample.puzzlelayout.utilities.extensions.showToast


class FragmentLayoutWithImages : BaseFragment<FragmentLayoutWithImagesBinding>(FragmentLayoutWithImagesBinding::inflate),
    PuzzleView.OnPieceClick, PuzzleView.OnPieceSelectedListener {

    private val args by navArgs<FragmentLayoutWithImagesArgs>()

    // MVVM
    private val puzzleLayout by lazy { PuzzleUtils() }
    private val repoPuzzleUtils by lazy { RepoPuzzleUtils(puzzleLayout) }
    private val useCasePuzzleLayouts by lazy { UseCasePuzzleLayouts(repoPuzzleUtils) }
    private val viewModelPuzzleLayouts by viewModels<ViewModelPuzzleLayouts> { ViewModelPuzzleLayoutsProvider(useCasePuzzleLayouts) }

    override fun onViewCreated() {
        initObservers()
        getData()
        setupListeners()
        initListener()
    }

    private fun initObservers() {
        viewModelPuzzleLayouts.puzzleLayoutLiveData.observe(viewLifecycleOwner) { list ->
            initView(list)
        }
    }

    private fun setupListeners() = binding.apply {
        toolbarMediaImage.setOnClickListener { popFrom(R.id.fragmentLayoutWithImages) }
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

    private fun initView(list: PuzzleLayout) {
        binding.puzzleView.apply {
            setPuzzleLayout(list)

            isTouchEnable = true
            needDrawLine = false
            needDrawOuterLine = false
            lineSize = 6
            lineColor = Color.BLACK
            selectedLineColor = ContextCompat.getColor(context, R.color.black)
            setAnimateDuration(700)
            piecePadding = 10f
            setOnPieceClickListener(this@FragmentLayoutWithImages)
            setOnPieceSelectedListener(this@FragmentLayoutWithImages)

            post {
                loadPhotoFromRes(list)
            }
        }
    }

    private fun loadPhotoFromRes(list: PuzzleLayout) {
        val pieces: MutableList<Bitmap> = ArrayList()

        val resIds = intArrayOf(
            R.drawable.demo1, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5
        )

        // Determine the number of images to load
        val count = if (resIds.size > list.areaCount) list.areaCount else resIds.size

        for (i in 0 until count) {
            val target: CustomTarget<Bitmap> = object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap?>?) {
                    pieces.add(bitmap)
                    if (pieces.size == count) {
                        if (resIds.size < list.areaCount) {
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
            Glide.with(this)
                .asBitmap()
                .load(resIds[i])
                .into(target)
        }
    }


    private fun getData() {
        viewModelPuzzleLayouts.getPuzzleLayout(args.type, args.size, args.theme)
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

}