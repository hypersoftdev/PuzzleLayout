package com.hypersoft.pzlayout.interfaces

import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Interface representing a layout for a puzzle, defining methods and properties
 * for managing the arrangement and properties of puzzle areas and lines.
 */
interface PuzzleLayout {

    /**
     * Sets the outer bounds of the layout using a RectF object.
     *
     * @param bounds The outer bounds to set.
     */
    fun setOuterBounds(bounds: RectF)

    /**
     * Lays out the puzzle areas and lines based on the current configuration.
     */
    fun layout()

    /**
     * Returns the total count of areas within the puzzle layout.
     */
    val areaCount: Int

    /**
     * Returns a list of outer lines that define the boundary of the layout.
     */
    val outerLines: List<Line>

    /**
     * Returns a list of all lines present in the puzzle layout.
     */
    val lines: List<Line>

    /**
     * Returns the outer area of the puzzle layout as an Area object.
     */
    val outerArea: Area

    /**
     * Updates the layout based on any changes made since the last layout call.
     */
    fun update()

    /**
     * Resets the layout to its initial state, clearing any changes made.
     */
    fun reset()

    /**
     * Returns the area at a specified position.
     *
     * @param position The index of the area to retrieve.
     * @return The Area object at the specified position.
     */
    fun getArea(position: Int): Area

    /**
     * Returns the width of the puzzle layout.
     *
     * @return The width as a Float.
     */
    fun width(): Float

    /**
     * Returns the height of the puzzle layout.
     *
     * @return The height as a Float.
     */
    fun height(): Float

    /**
     * Padding applied to the layout.
     */
    var padding: Float

    /**
     * Rotation of the layout in radians.
     */
    var radian: Float

    /**
     * Generates information about the current state of the layout.
     *
     * @return An Info object containing the layout information.
     */
    fun generateInfo(): Info

    /**
     * Color of the puzzle layout, represented as an integer.
     */
    var color: Int

    /**
     * Sorts the areas within the puzzle layout according to some criteria.
     */
    fun sortAreas()

    /**
     * Class representing information about the puzzle layout, implementing Parcelable.
     */
    class Info : Parcelable {

        @JvmField
        var type: Int = 0 // Type of the puzzle layout (e.g., straight, slant)

        @JvmField
        var steps: ArrayList<Step>? = null // List of steps taken in the layout

        @JvmField
        var lineInfos: ArrayList<LineInfo>? = null // Information about the lines in the layout

        @JvmField
        var padding: Float = 0f // Padding of the layout

        @JvmField
        var radian: Float = 0f // Rotation in radians

        @JvmField
        var color: Int = 0 // Color of the layout

        @JvmField
        var left: Float = 0f // Left boundary of the layout

        @JvmField
        var top: Float = 0f // Top boundary of the layout

        @JvmField
        var right: Float = 0f // Right boundary of the layout

        @JvmField
        var bottom: Float = 0f // Bottom boundary of the layout

        // Default constructor
        constructor()

        // Constructor for creating Info object from a Parcel
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

        /**
         * Returns the width of the layout as calculated from its boundaries.
         *
         * @return The width as a Float.
         */
        fun width(): Float {
            return right - left
        }

        /**
         * Returns the height of the layout as calculated from its boundaries.
         *
         * @return The height as a Float.
         */
        fun height(): Float {
            return bottom - top
        }

        override fun describeContents(): Int {
            return 0 // No special content
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
            const val TYPE_STRAIGHT: Int = 0 // Constant for straight type layout
            const val TYPE_SLANT: Int = 1 // Constant for slant type layout

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

    /**
     * Class representing a step taken in the puzzle layout, implementing Parcelable.
     */
    class Step : Parcelable {

        @JvmField
        var type: Int = 0 // Type of the step (e.g., add line, cut equal part)

        @JvmField
        var direction: Int = 0 // Direction of the step (horizontal or vertical)

        @JvmField
        var position: Int = 0 // Position related to the step

        @JvmField
        var part: Int = 0 // Part related to the step

        @JvmField
        var hSize: Int = 0 // Horizontal size for the step

        @JvmField
        var vSize: Int = 0 // Vertical size for the step

        // Default constructor
        constructor()

        // Constructor for creating Step object from a Parcel
        protected constructor(parcel: Parcel) {
            type = parcel.readInt()
            direction = parcel.readInt()
            position = parcel.readInt()
            part = parcel.readInt()
            hSize = parcel.readInt()
            vSize = parcel.readInt()
        }

        /**
         * Returns the direction of the line as a Line.Direction enum.
         *
         * @return The direction of the line.
         */
        fun lineDirection(): Line.Direction {
            return if (direction == 0) Line.Direction.HORIZONTAL else Line.Direction.VERTICAL
        }

        override fun describeContents(): Int {
            return 0 // No special content
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
            const val ADD_LINE: Int = 0 // Constant for adding a line step
            const val ADD_CROSS: Int = 1 // Constant for adding a cross step
            const val CUT_EQUAL_PART_ONE: Int = 2 // Constant for cutting equal part one
            const val CUT_EQUAL_PART_TWO: Int = 3 // Constant for cutting equal part two
            const val CUT_SPIRAL: Int = 4 // Constant for cutting a spiral

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

    /**
     * Class representing information about a line in the puzzle layout, implementing Parcelable.
     */
    class LineInfo : Parcelable {
        var startX: Float // X coordinate of the starting point
        var startY: Float // Y coordinate of the starting point
        var endX: Float // X coordinate of the ending point
        var endY: Float // Y coordinate of the ending point

        // Constructor for creating LineInfo from a Line object
        constructor(line: Line) {
            startX = line.startPoint().x
            startY = line.startPoint().y
            endX = line.endPoint().x
            endY = line.endPoint().y
        }

        // Constructor for creating LineInfo object from a Parcel
        protected constructor(parcel: Parcel) {
            startX = parcel.readFloat()
            startY = parcel.readFloat()
            endX = parcel.readFloat()
            endY = parcel.readFloat()
        }

        override fun describeContents(): Int {
            return 0 // No special content
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
