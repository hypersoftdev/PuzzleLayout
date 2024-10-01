package com.puzzle.layouts.interfaces

import android.graphics.PointF

interface Line {
    enum class Direction {
        HORIZONTAL, VERTICAL
    }

    fun length(): Float

    fun startPoint(): PointF

    fun endPoint(): PointF

    fun lowerLine(): Line

    fun upperLine(): Line

    fun attachStartLine(): Line

    fun attachEndLine(): Line

    fun setLowerLine(lowerLine: Line)

    fun setUpperLine(upperLine: Line)

    fun direction(): Direction

    fun slope(): Float

    fun contains(x: Float, y: Float, extra: Float): Boolean

    fun prepareMove()

    fun move(offset: Float, extra: Float): Boolean

    fun update(layoutWidth: Float, layoutHeight: Float)

    fun minX(): Float

    fun maxX(): Float

    fun minY(): Float

    fun maxY(): Float

    fun offset(x: Float, y: Float)
}
