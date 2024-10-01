package com.puzzle.layouts.interfaces

import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable

interface PuzzleLayout {
    fun setOuterBounds(bounds: RectF)

    fun layout()

    val areaCount: Int

    val outerLines: List<Line>

    val lines: List<Line>

    val outerArea: Area

    fun update()

    fun reset()

    fun getArea(position: Int): Area

    fun width(): Float

    fun height(): Float

    var padding: Float

    var radian: Float

    fun generateInfo(): Info

    var color: Int

    fun sortAreas()

    class Info : Parcelable {
        @JvmField
        var type: Int = 0

        @JvmField
        var steps: ArrayList<Step>? = null

        @JvmField
        var lineInfos: ArrayList<LineInfo>? = null

        @JvmField
        var padding: Float = 0f

        @JvmField
        var radian: Float = 0f

        @JvmField
        var color: Int = 0

        @JvmField
        var left: Float = 0f

        @JvmField
        var top: Float = 0f

        @JvmField
        var right: Float = 0f

        @JvmField
        var bottom: Float = 0f

        constructor()

        protected constructor(parcel: Parcel) {
            type = parcel.readInt()
            steps = parcel.createTypedArrayList(Step.CREATOR)
            lineInfos = parcel.createTypedArrayList(LineInfo.CREATOR)
            padding = parcel.readFloat()
            radian = parcel.readFloat()
            color = parcel.readInt()
            left = parcel.readFloat()
            top = parcel.readFloat()
            right = parcel.readFloat()
            bottom = parcel.readFloat()
        }

        fun width(): Float {
            return right - left
        }

        fun height(): Float {
            return bottom - top
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeInt(type)
            parcel.writeTypedList(steps)
            parcel.writeTypedList(lineInfos)
            parcel.writeFloat(padding)
            parcel.writeFloat(radian)
            parcel.writeInt(color)
            parcel.writeFloat(left)
            parcel.writeFloat(top)
            parcel.writeFloat(right)
            parcel.writeFloat(bottom)
        }

        companion object {
            const val TYPE_STRAIGHT: Int = 0
            const val TYPE_SLANT: Int = 1

            @JvmField
            val CREATOR: Parcelable.Creator<Info> = object : Parcelable.Creator<Info> {
                override fun createFromParcel(parcel: Parcel): Info {
                    return Info(parcel)
                }

                override fun newArray(size: Int): Array<Info?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    class Step : Parcelable {
        @JvmField
        var type: Int = 0

        @JvmField
        var direction: Int = 0

        @JvmField
        var position: Int = 0

        @JvmField
        var part: Int = 0

        @JvmField
        var hSize: Int = 0

        @JvmField
        var vSize: Int = 0

        constructor()

        protected constructor(parcel: Parcel) {
            type = parcel.readInt()
            direction = parcel.readInt()
            position = parcel.readInt()
            part = parcel.readInt()
            hSize = parcel.readInt()
            vSize = parcel.readInt()
        }

        fun lineDirection(): Line.Direction {
            return if (direction == 0) Line.Direction.HORIZONTAL else Line.Direction.VERTICAL
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeInt(type)
            parcel.writeInt(direction)
            parcel.writeInt(position)
            parcel.writeInt(part)
            parcel.writeInt(hSize)
            parcel.writeInt(vSize)
        }

        companion object {
            const val ADD_LINE: Int = 0
            const val ADD_CROSS: Int = 1
            const val CUT_EQUAL_PART_ONE: Int = 2
            const val CUT_EQUAL_PART_TWO: Int = 3
            const val CUT_SPIRAL: Int = 4

            @JvmField
            val CREATOR: Parcelable.Creator<Step> = object : Parcelable.Creator<Step> {
                override fun createFromParcel(parcel: Parcel): Step {
                    return Step(parcel)
                }

                override fun newArray(size: Int): Array<Step?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    class LineInfo : Parcelable {
        var startX: Float
        var startY: Float
        var endX: Float
        var endY: Float

        constructor(line: Line) {
            startX = line.startPoint().x
            startY = line.startPoint().y
            endX = line.endPoint().x
            endY = line.endPoint().y
        }

        protected constructor(parcel: Parcel) {
            startX = parcel.readFloat()
            startY = parcel.readFloat()
            endX = parcel.readFloat()
            endY = parcel.readFloat()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeFloat(startX)
            parcel.writeFloat(startY)
            parcel.writeFloat(endX)
            parcel.writeFloat(endY)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<LineInfo> = object : Parcelable.Creator<LineInfo> {
                override fun createFromParcel(parcel: Parcel): LineInfo {
                    return LineInfo(parcel)
                }

                override fun newArray(size: Int): Array<LineInfo?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
