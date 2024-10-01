package com.puzzle.layouts.interfaces

import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF

interface Area {
    fun left(): Float

    fun top(): Float

    fun right(): Float

    fun bottom(): Float

    fun centerX(): Float

    fun centerY(): Float

    fun width(): Float

    fun height(): Float

    val centerPoint: PointF

    fun contains(point: PointF?): Boolean

    fun contains(x: Float, y: Float): Boolean

    fun contains(line: Line?): Boolean

    val areaPath: Path

    val areaRect: RectF

    val lines: List<Line>

    fun getHandleBarPoints(line: Line): Array<PointF>

    fun radian(): Float

    fun setRadian(radian: Float)

    val paddingLeft: Float

    val paddingTop: Float

    val paddingRight: Float

    val paddingBottom: Float

    fun setPadding(padding: Float)

    fun setPadding(paddingLeft: Float, paddingTop: Float, paddingRight: Float, paddingBottom: Float)
}





