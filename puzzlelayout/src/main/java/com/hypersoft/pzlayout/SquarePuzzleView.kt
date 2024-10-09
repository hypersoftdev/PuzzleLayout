package com.hypersoft.pzlayout

import android.content.Context
import android.util.AttributeSet
import com.hypersoft.pzlayout.view.PuzzleView

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */
class SquarePuzzleView : PuzzleView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Get the measured width and height of the view.
        val width = measuredWidth
        val height = measuredHeight

        // Determine the shorter side of the view (width or height) to make the view square.
        val length = if (width > height) height else width

        // Set the dimensions of the view to be the same for both width and height (a square).
        setMeasuredDimension(length, length)
    }
}

