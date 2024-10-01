package com.puzzle.layouts.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import com.puzzle.layouts.interfaces.Area
import com.puzzle.layouts.interfaces.Line
import com.puzzle.layouts.interfaces.PuzzleLayout
import com.puzzle.layouts.utils.MatrixUtils.generateMatrix
import com.puzzle.layouts.utils.PuzzleLayoutParser.parse
import com.puzzle.layouts.utils.PuzzlePiece
import com.puzzle.layouts.R
import kotlin.math.abs
import kotlin.math.sqrt

@Suppress("unused")
open class PuzzleView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private enum class ActionMode {
        NONE, DRAG, ZOOM, MOVE, SWAP
    }

    @JvmField
    var lineSize = 0

    @JvmField
    var needDrawLine = false

    @JvmField
    var needDrawOuterLine = false

    @JvmField
    var isTouchEnable: Boolean = true

    @JvmField
    var lineColor = 0

    @JvmField
    var selectedLineColor = 0

    @JvmField
    var piecePadding = 0f

    private var currentMode = ActionMode.NONE
    private val puzzlePieces: MutableList<PuzzlePiece> = ArrayList()
    private val needChangePieces: MutableList<PuzzlePiece> = ArrayList()
    private val areaPieceMap: MutableMap<Area, PuzzlePiece> = HashMap()
    private var puzzleLayout: PuzzleLayout? = null
    private var initialInfo: PuzzleLayout.Info? = null
    private var bounds: RectF? = null
    private var duration = 0
    private var handlingLine: Line? = null
    private var handlingPiece: PuzzlePiece? = null
    private var replacePiece: PuzzlePiece? = null
    private var previousHandlingPiece: PuzzlePiece? = null
    private var linePaint: Paint? = null
    private var selectedAreaPaint: Paint? = null
    private var handleBarPaint: Paint? = null
    private var downX = 0f
    private var downY = 0f
    private var previousDistance = 0f
    private var midPoint: PointF? = null
    private var handleBarColor = 0
    private var pieceRadian = 0f
    private var needResetPieceMatrix = true
    private var quickMode = false
    private var canDrag = true
    private var canMoveLine = true
    private var canZoom = true
    private var canSwap = true
    private var currentPiece: PuzzlePiece? = null
    private val matrix: Matrix? = null
    private val previousMatrix: Matrix? = null
    private val tempMatrix: Matrix? = null
    private var swipeActivated: Boolean = false
    private var temp: Drawable? = null
    private var tempPath: String? = null
    private var swipeTempPiece: PuzzlePiece? = null
    private var onPieceSelectedListener: OnPieceSelectedListener? = null
    private var onPieceClick: OnPieceClick? = null
    private var tempPathPos = -1
    private var tempPath2Pos = -1
    private val toMovePuzzlePieces: MutableList<PuzzlePiece?> = ArrayList()
    private var lastHandlingPiece: PuzzlePiece? = null
    private var sizeChangedOnce = false
    private var reset = true
    private val pieceAppliedEditing: MutableList<PuzzlePiece?> = ArrayList()

    private val switchToSwapAction = Runnable {
        if (!canSwap) return@Runnable
        currentMode = ActionMode.SWAP
        invalidate()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PuzzleView)
        lineSize = ta.getInt(R.styleable.PuzzleView_line_size, 4)
        lineColor = ta.getColor(R.styleable.PuzzleView_line_color, Color.WHITE)
        selectedLineColor = ta.getColor(R.styleable.PuzzleView_selected_line_color, Color.parseColor("#99BBFB"))
        handleBarColor = ta.getColor(R.styleable.PuzzleView_handle_bar_color, Color.parseColor("#99BBFB"))
        piecePadding = ta.getDimensionPixelSize(R.styleable.PuzzleView_piece_padding, 0).toFloat()
        needDrawLine = ta.getBoolean(R.styleable.PuzzleView_need_draw_line, false)
        needDrawOuterLine = ta.getBoolean(R.styleable.PuzzleView_need_draw_outer_line, false)
        duration = ta.getInt(R.styleable.PuzzleView_animation_duration, 300)
        pieceRadian = ta.getFloat(R.styleable.PuzzleView_radian, 0f)

        ta.recycle()

        bounds = RectF()

        // init some paint
        linePaint = Paint()
        linePaint?.isAntiAlias = true
        linePaint?.color = lineColor
        linePaint?.strokeWidth = lineSize.toFloat()
        linePaint?.style = Paint.Style.STROKE
        linePaint?.strokeJoin = Paint.Join.ROUND
        linePaint?.strokeCap = Paint.Cap.SQUARE

        selectedAreaPaint = Paint()
        selectedAreaPaint?.isAntiAlias = true
        selectedAreaPaint?.style = Paint.Style.STROKE
        selectedAreaPaint?.strokeJoin = Paint.Join.ROUND
        selectedAreaPaint?.strokeCap = Paint.Cap.ROUND
        selectedAreaPaint?.color = selectedLineColor
        selectedAreaPaint?.strokeWidth = lineSize.toFloat()

        handleBarPaint = Paint()
        handleBarPaint?.isAntiAlias = true
        handleBarPaint?.style = Paint.Style.FILL
        handleBarPaint?.color = handleBarColor
        handleBarPaint?.strokeWidth = (lineSize * 3).toFloat()

        midPoint = PointF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d("onSizeChanged ", "Called $sizeChangedOnce")
        super.onSizeChanged(w, h, oldw, oldh)
        resetPuzzleBounds()
        areaPieceMap.clear()
        if (puzzlePieces.size != 0) {
            for (i in puzzlePieces.indices) {
                val piece = puzzlePieces[i]
                val area = puzzleLayout?.getArea(i)
                area?.let {
                    piece.area = it
                    areaPieceMap[it] = piece
                    if (needResetPieceMatrix) {
                        if (pieceAppliedEditing.contains(piece)) {
                            Log.d("SamePieceDetected", "onSizeChanged: ")
                        } else {
                            piece.set(generateMatrix(piece, 0f))
                        }
                    } else {
                        piece.fillArea(this, true)
                    }
                }
            }
        }
        invalidate()
        if (reset) {
            sizeChangedOnce = false
        }
    }

    fun doNotAutoResetFalse() {
        reset = false
    }

    fun resetAutoFalse() {
        reset = true
    }

    private fun resetPuzzleBounds() {
        Log.d("Debugging ", "resetPuzzleBounds")

        bounds?.left = paddingLeft.toFloat()
        bounds?.top = paddingTop.toFloat()
        bounds?.right = (width - paddingRight).toFloat()
        bounds?.bottom = (height - paddingBottom).toFloat()

        if (puzzleLayout != null) {
            puzzleLayout?.reset()
            bounds?.let { puzzleLayout?.setOuterBounds(it) }
            puzzleLayout?.layout()
            puzzleLayout?.padding = piecePadding
            puzzleLayout?.radian = pieceRadian

            if (initialInfo != null) {
                val size = initialInfo?.lineInfos?.size
                size?.let {
                    for (i in 0 until it) {
                        initialInfo?.lineInfos?.let { li ->
                            val lineInfo = li[i]
                            puzzleLayout?.lines?.let { lines ->
                                val line = lines[i]
                                line.startPoint().x = lineInfo.startX
                                line.startPoint().y = lineInfo.startY
                                line.endPoint().x = lineInfo.endX
                                line.endPoint().y = lineInfo.endY
                            }
                        }
                    }
                }
            }

            puzzleLayout?.sortAreas()
            puzzleLayout?.update()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d("Debugging ", "onDraw")

        //    Log.d("MovingHandlingLine ","onDraw");
        if (puzzleLayout == null) {
            return
        }

        linePaint?.strokeWidth = lineSize.toFloat()
        selectedAreaPaint?.strokeWidth = lineSize.toFloat()
        handleBarPaint?.strokeWidth = (lineSize * 3).toFloat()

        // draw pieces
        puzzleLayout?.areaCount?.let { ac ->
            for (i in 0 until ac) {
                if (i >= puzzlePieces.size) {
                    break
                }
                val piece = puzzlePieces[i]
                if (piece == handlingPiece && currentMode == ActionMode.SWAP) {
                    continue
                }
                if (puzzlePieces.size > i) {
                    piece.draw(canvas, quickMode)
                }
            }
        }

        // draw outer bounds
        if (needDrawOuterLine) {
            puzzleLayout?.outerLines?.let { ol ->
                for (outerLine in ol) {
                    drawLine(canvas, outerLine)
                }
            }
        }

        // draw slant lines
        if (needDrawLine) {
            puzzleLayout?.lines?.let { lines ->
                for (line in lines) {
                    drawLine(canvas, line)
                }
            }
        }

        // draw selected area
        if (handlingPiece != null && currentMode != ActionMode.SWAP) {
            handlingPiece?.let { drawSelectedArea(canvas, it) }
        }

        // draw swap piece
        if (handlingPiece != null && currentMode == ActionMode.SWAP) {
            handlingPiece?.draw(canvas, 128, quickMode)
            replacePiece?.let { drawSelectedArea(canvas, it) }
        }
    }

    private fun drawSelectedArea(canvas: Canvas, piece: PuzzlePiece) {
        Log.d("Debugging ", "drawSelectedArea")

        val area = piece.area
        // draw select area
        selectedAreaPaint?.let { canvas.drawPath(area.areaPath, it) }

        // draw handle bar
        for (line in area.lines) {
            if (puzzleLayout?.lines?.contains(line) == true) {
                val handleBarPoints = area.getHandleBarPoints(line)
                handleBarPaint?.let {
                    canvas.drawLine(
                        handleBarPoints[0].x, handleBarPoints[0].y, handleBarPoints[1].x,
                        handleBarPoints[1].y, it
                    )
                    canvas.drawCircle(
                        handleBarPoints[0].x, handleBarPoints[0].y, (lineSize * 3 / 2).toFloat(),
                        it
                    )
                    canvas.drawCircle(
                        handleBarPoints[1].x, handleBarPoints[1].y, (lineSize * 3 / 2).toFloat(),
                        it
                    )
                }
            }
        }
    }

    private fun drawLine(canvas: Canvas, line: Line) {
        linePaint?.let {
            canvas.drawLine(line.startPoint().x, line.startPoint().y, line.endPoint().x, line.endPoint().y, it)
        }
    }

    fun setPuzzleLayout(puzzleLayout: PuzzleLayout) {
        clearPieces()

        this.puzzleLayout = puzzleLayout

        bounds?.let { puzzleLayout.setOuterBounds(it) }
        puzzleLayout.layout()

        invalidate()
    }

    fun setPuzzleLayout(info: PuzzleLayout.Info) {
        this.initialInfo = info
        clearPieces()

        this.puzzleLayout = parse(info)

        this.piecePadding = info.padding
        this.pieceRadian = info.radian
        setBackgroundColor(info.color)

        invalidate()
    }

    fun getPuzzleLayout(): PuzzleLayout? {
        return puzzleLayout
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isTouchEnable) {
            return super.onTouchEvent(event)
        }
        gestureDetector.onTouchEvent(event)
        currentPiece = findHandlingPiece()
        //
        pieceAppliedEditing.add(currentPiece)
        //
        lastHandlingPiece = currentPiece
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y

                decideActionMode(event)
                prepareAction()
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                previousDistance = calculateDistance(event)
                calculateMidPoint(event, midPoint)

                decideActionMode(event)
            }

            MotionEvent.ACTION_MOVE -> {
                performAction(event)

                if ((abs((event.x - downX).toDouble()) > 10 || abs((event.y - downY).toDouble()) > 10)
                    && currentMode != ActionMode.SWAP
                ) {
                    removeCallbacks(switchToSwapAction)
                }
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                finishAction(event)
                currentMode = ActionMode.NONE
                removeCallbacks(switchToSwapAction)
            }
        }
        invalidate()
        return true
    }

    private fun decideActionMode(event: MotionEvent) {
        for (piece in puzzlePieces) {
            if (piece.isAnimateRunning) {
                currentMode = ActionMode.NONE
                return
            }
        }

        if (event.pointerCount == 1) {
            handlingLine = findHandlingLine()
            if (handlingLine != null && canMoveLine) {
                currentMode = ActionMode.MOVE
            } else {
                handlingPiece = findHandlingPiece()

                if (handlingPiece != null && canDrag) {
                    currentMode = ActionMode.DRAG

                    Log.d("HandlingPositions 3", handlingPiecePosition.toString())
                    postDelayed(switchToSwapAction, 500)
                }
            }
        } else if (event.pointerCount > 1) {
            if (handlingPiece != null && handlingPiece?.contains(event.getX(1), event.getY(1)) == true && currentMode == ActionMode.DRAG && canZoom) {
                currentMode = ActionMode.ZOOM
            }
        }
    }

    @Suppress("unused")
    private fun prepareAction() {
        when (currentMode) {
            ActionMode.NONE -> {}
            ActionMode.DRAG -> handlingPiece?.record()
            ActionMode.ZOOM -> handlingPiece?.record()
            ActionMode.MOVE -> {
                handlingLine?.prepareMove()
                needChangePieces.clear()
                needChangePieces.addAll(findNeedChangedPieces())
                for (piece in needChangePieces) {
                    piece.record()
                    piece.setPreviousMoveX(downX)
                    piece.setPreviousMoveY(downY)
                }
            }

            else -> {}
        }
    }

    private fun performAction(event: MotionEvent) {
        when (currentMode) {
            ActionMode.NONE -> {}
            ActionMode.DRAG -> dragPiece(handlingPiece, event)
            ActionMode.ZOOM -> zoomPiece(handlingPiece, event)
            ActionMode.SWAP -> {
                Log.d("HandlingPositions 4", handlingPiecePosition.toString())
                dragPiece(handlingPiece, event)
                replacePiece = findReplacePiece(event)
            }

            ActionMode.MOVE -> moveLine(handlingLine, event)
        }
    }

    private fun finishAction(event: MotionEvent) {
        when (currentMode) {
            ActionMode.NONE -> {}
            ActionMode.DRAG -> {
                if (previousHandlingPiece == handlingPiece && abs((downX - event.x).toDouble()) < 3 && abs((downY - event.y).toDouble()) < 3) {
                    handlingPiece = null
                }
                previousHandlingPiece = handlingPiece
            }

            ActionMode.ZOOM -> {
                handlingPiece?.let {
                    if (!it.isFilledArea) {
                        if (it.canFilledArea()) {
                            it.moveToFillArea(this)
                        } else {
                            //it.fillArea(this, false);
                        }
                    }
                    previousHandlingPiece = it
                }
            }

            ActionMode.MOVE -> {}
            ActionMode.SWAP -> if (handlingPiece != null && replacePiece != null) {
                swapPiece()
                Log.d("HandlingPositions 5", handlingPiecePosition.toString())
                handlingPiece = null
                replacePiece = null
                previousHandlingPiece = null
            }
        }
        // trigger listener
        if (handlingPiece != null && onPieceSelectedListener != null) {
            onPieceSelectedListener?.onPieceSelected(
                handlingPiece,
                puzzlePieces.indexOf(handlingPiece)
            )
        }

        handlingLine = null
        needChangePieces.clear()
    }

    private fun swapPiece() {
        handlingPiece?.let {
            val temp = it.getDrawable()
            val tempPath = it.path
            replacePiece?.let { rp ->
                rp.getDrawable().let { it1 -> it.setDrawable(it1) }
                it.path = rp.path
            }
            replacePiece?.setDrawable(temp)
            replacePiece?.path = tempPath

            it.fillArea(this, true)
            replacePiece?.fillArea(this, true)

            tempPathPos = handlingPiecePosition
            onPieceClick?.onSwapGetPositions(tempPathPos, tempPath2Pos)
        }
    }

    private fun moveLine(line: Line?, event: MotionEvent?) {
        if (line == null || event == null) return
        val needUpdate = if (line.direction() == Line.Direction.HORIZONTAL) {
            line.move(event.y - downY, 80f)
        } else {
            line.move(event.x - downX, 80f)
        }
        if (needUpdate) {
            puzzleLayout?.update()
            puzzleLayout?.sortAreas()
            updatePiecesInArea(line, event)
        }
    }

    private fun updatePiecesInArea(line: Line, event: MotionEvent) {
        for (i in needChangePieces.indices) {
            needChangePieces[i].updateWith(event, line)
        }
    }

    private fun zoomPiece(piece: PuzzlePiece?, event: MotionEvent?) {
        if (piece == null || event == null || event.pointerCount < 2) return
        val scale = calculateDistance(event) / previousDistance
        val matrixValues = FloatArray(9) // Matrix values array
        piece.matrix.getValues(matrixValues)
        var zoomMinMax = matrixValues[0]
        if (abs(zoomMinMax.toDouble()).toInt() == 0) {
            zoomMinMax = matrixValues[1]
        }
        if (abs(zoomMinMax.toDouble()) < 0.3) {
            if (scale > 1) {
                midPoint?.let { piece.zoomAndTranslate(scale, scale, it, event.x - downX, event.y - downY) }
            }
        } else if (abs(zoomMinMax.toDouble()) > 4.5) {
            if (scale < 1) {
                midPoint?.let { piece.zoomAndTranslate(scale, scale, it, event.x - downX, event.y - downY) }
            }
        } else {
            midPoint?.let { piece.zoomAndTranslate(scale, scale, it, event.x - downX, event.y - downY) }
        }
    }

    private fun dragPiece(piece: PuzzlePiece?, event: MotionEvent?) {
        if (piece == null || event == null) return
        piece.translate(event.x - downX, event.y - downY)
        toMovePuzzlePieces.add(handlingPiece)
    }

    fun replace(bitmap: Bitmap?, path: String?) {
        val bitmapDrawable = BitmapDrawable(resources, bitmap)
        bitmapDrawable.setAntiAlias(true)
        bitmapDrawable.isFilterBitmap = true

        replace(bitmapDrawable, path)
    }

    fun replace(bitmapDrawable: Drawable?, path: String?) {
        if (handlingPiece == null) {
            return
        }

        handlingPiece?.path = path ?: return
        handlingPiece?.setDrawable(bitmapDrawable ?: return)
        handlingPiece?.set(generateMatrix(handlingPiece ?: return, 0f))

        invalidate()
    }

    fun flipVertically() {
        if (handlingPiece == null) {
            return
        }
        handlingPiece?.postFlipVertically()
        handlingPiece?.record()

        invalidate()
    }

    fun flipHorizontally() {
        if (handlingPiece == null) {
            return
        }

        handlingPiece?.postFlipHorizontally()
        handlingPiece?.record()

        invalidate()
    }

    fun rotate(degree: Float) {
        if (handlingPiece != null) {
            handlingPiece?.postRotate(degree)
            handlingPiece?.record()
            invalidate()
        } else {
            handlingPiece = puzzlePieces[0]
            handlingPiece?.postRotate(degree)
            handlingPiece?.record()
            invalidate()
        }
    }

    private fun findHandlingPiece(): PuzzlePiece? {
        for (piece in puzzlePieces) {
            if (piece.contains(downX, downY)) {
                return piece
            }
        }
        return null
    }

    private fun findHandlingLine(): Line? {
        puzzleLayout?.let {
            for (line in it.lines) {
                if (line.contains(downX, downY, 40f)) {
                    return line
                }
            }
        }
        return null
    }

    private fun findReplacePiece(event: MotionEvent): PuzzlePiece? {
        tempPath2Pos = handlingPiecePosition
        for (piece in puzzlePieces) {
            if (piece.contains(event.x, event.y)) {
                return piece
            }
        }
        return null
    }

    private fun findNeedChangedPieces(): List<PuzzlePiece> {
        if (handlingLine == null) return ArrayList()

        val needChanged: MutableList<PuzzlePiece> = ArrayList()

        for (piece in puzzlePieces) {
            if (piece.contains(handlingLine)) {
                needChanged.add(piece)
            }
        }

        return needChanged
    }

    private fun calculateDistance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)

        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun calculateMidPoint(event: MotionEvent, point: PointF?) {
        point?.x = (event.getX(0) + event.getX(1)) / 2
        point?.y = (event.getY(0) + event.getY(1)) / 2
    }

    fun reset() {
        clearPieces()
        if (puzzleLayout != null) {
            puzzleLayout?.reset()
        }
    }

    fun clearPieces() {
        clearHandlingPieces()
        puzzlePieces.clear()

        invalidate()
    }

    fun clearHandlingPieces() {
        handlingLine = null
        handlingPiece = null
        replacePiece = null
        needChangePieces.clear()

        invalidate()
    }

    fun addPieces(bitmaps: List<Bitmap?>) {
        for (bitmap in bitmaps) {
            addPiece(bitmap)
        }

        postInvalidate()
    }

    fun addDrawablePieces(drawables: List<Drawable?>) {
        for (drawable in drawables) {
            addPiece(drawable)
        }

        postInvalidate()
    }

    fun addPiece(bitmap: Bitmap?) {
        val bitmapDrawable = BitmapDrawable(resources, bitmap)
        bitmapDrawable.setAntiAlias(true)
        bitmapDrawable.isFilterBitmap = true

        addPiece(bitmapDrawable, null)
    }

    @JvmOverloads
    fun addPiece(bitmap: Bitmap?, initialMatrix: Matrix?, path: String? = "") {
        val bitmapDrawable = BitmapDrawable(resources, bitmap)
        bitmapDrawable.setAntiAlias(true)
        bitmapDrawable.isFilterBitmap = true

        addPiece(bitmapDrawable, initialMatrix, path)
    }

    @JvmOverloads
    fun addPiece(drawable: Drawable?, initialMatrix: Matrix? = null, path: String? = "") {
        val position = puzzlePieces.size

        puzzleLayout?.let {
            if (position >= it.areaCount) {
                Log.e(
                    TAG, "addPiece: can not add more. the current puzzle layout can contains "
                            + it.areaCount
                            + " puzzle piece."
                )
                return
            }
        }

        val area = puzzleLayout?.getArea(position)
        area?.setPadding(piecePadding)

        val piece = PuzzlePiece(drawable ?: return, area ?: return, Matrix())

        val matrix = if (initialMatrix != null) Matrix(initialMatrix) else generateMatrix(area, drawable, 0f)
        piece.set(matrix)

        piece.setAnimateDuration(duration)
        piece.path = path ?: return

        puzzlePieces.add(piece)
        areaPieceMap[area] = piece

        setPiecePadding(piecePadding)
        setPieceRadian(pieceRadian)

        invalidate()
    }

    fun setSelected(position: Int) {
        post(Runnable {
            if (position >= puzzlePieces.size) return@Runnable
            handlingPiece = puzzlePieces[position]
            previousHandlingPiece = handlingPiece

            if (onPieceSelectedListener != null) {
                onPieceSelectedListener?.onPieceSelected(handlingPiece, position)
            }
            invalidate()
        })
    }

    val handlingPiecePosition: Int
        get() {
            if (handlingPiece == null) {
                return -1
            }
            return puzzlePieces.indexOf(handlingPiece)
        }

    // can be null
    fun hasPieceSelected(): Boolean {
        return handlingPiece != null
    }

    fun setAnimateDuration(duration: Int) {
        this.duration = duration
        for (piece in puzzlePieces) {
            piece.setAnimateDuration(duration)
        }
    }

    fun isNeedDrawLine(): Boolean {
        return needDrawLine
    }

    fun setNeedDrawLine(needDrawLine: Boolean) {
        this.needDrawLine = needDrawLine
        handlingPiece = null
        previousHandlingPiece = null
        invalidate()
    }

    fun isNeedDrawOuterLine(): Boolean {
        return needDrawOuterLine
    }

    fun setNeedDrawOuterLine(needDrawOuterLine: Boolean) {
        this.needDrawOuterLine = needDrawOuterLine
        invalidate()
    }

    fun getLineColor(): Int {
        return lineColor
    }

    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
        linePaint?.color = lineColor
        invalidate()
    }

    fun getLineSize(): Int {
        return lineSize
    }

    fun setLineSize(lineSize: Int) {
        this.lineSize = lineSize
        invalidate()
    }

    fun getSelectedLineColor(): Int {
        return selectedLineColor
    }

    fun setSelectedLineColor(selectedLineColor: Int) {
        this.selectedLineColor = selectedLineColor
        selectedAreaPaint?.color = selectedLineColor
        invalidate()
    }

    fun getHandleBarColor(): Int {
        return handleBarColor
    }

    fun setHandleBarColor(handleBarColor: Int) {
        this.handleBarColor = handleBarColor
        handleBarPaint?.color = handleBarColor
        invalidate()
    }

    fun clearHandling() {
        handlingPiece = null
        handlingLine = null
        replacePiece = null
        previousHandlingPiece = null
        needChangePieces.clear()
    }

    fun setPiecePadding(padding: Float) {
        this.piecePadding = padding
        if (puzzleLayout != null) {
            puzzleLayout?.padding = padding
            val size = puzzlePieces.size
            for (i in 0 until size) {
                val puzzlePiece = puzzlePieces[i]
                if (puzzlePiece.canFilledArea()) {
                    puzzlePiece.moveToFillArea(null)
                } else {
                    //puzzlePiece.fillArea(this, true);
                }
            }
        }

        invalidate()
    }

    fun setPieceRadian(radian: Float) {
        this.pieceRadian = radian
        if (puzzleLayout != null) {
            puzzleLayout?.radian = radian
        }

        invalidate()
    }

    fun setQuickMode(quickMode: Boolean) {
        this.quickMode = quickMode
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        if (puzzleLayout != null) {
            puzzleLayout?.color = color
        }
    }

    fun setNeedResetPieceMatrix(needResetPieceMatrix: Boolean) {
        this.needResetPieceMatrix = needResetPieceMatrix
    }

    fun getPiecePadding(): Float {
        return piecePadding
    }

    fun getPieceRadian(): Float {
        return pieceRadian
    }

    fun getPuzzlePieces(): List<PuzzlePiece?> {
        val size = puzzlePieces.size
        val pieces: MutableList<PuzzlePiece?> = ArrayList(size)
        puzzleLayout?.sortAreas()
        for (i in 0 until size) {
            val area = puzzleLayout?.getArea(i)
            val piece = areaPieceMap[area]
            pieces.add(piece)
        }

        return pieces
    }

    fun setMyPiecePadding(padding: Float) {
        this.piecePadding = padding
        if (puzzleLayout != null) {
            puzzleLayout?.padding = padding
            val size = puzzlePieces.size
            for (i in 0 until size) {
                val puzzlePiece = puzzlePieces[i]
                if (puzzlePiece.canFilledArea()) {
                    puzzlePiece.moveToFillArea(null)
                } else {
                    puzzlePiece.fillArea(this, true)
                }
            }
        }

        invalidate()
    }

    fun canDrag(): Boolean {
        return canDrag
    }

    fun setCanDrag(canDrag: Boolean) {
        this.canDrag = canDrag
    }

    fun canMoveLine(): Boolean {
        return canMoveLine
    }

    fun setCanMoveLine(canMoveLine: Boolean) {
        this.canMoveLine = canMoveLine
    }

    fun canZoom(): Boolean {
        return canZoom
    }

    fun setCanZoom(canZoom: Boolean) {
        this.canZoom = canZoom
    }

    fun canSwap(): Boolean {
        return canSwap
    }

    fun setCanSwap(canSwap: Boolean) {
        this.canSwap = canSwap
    }

    fun setOnPieceSelectedListener(onPieceSelectedListener: OnPieceSelectedListener?) {
        this.onPieceSelectedListener = onPieceSelectedListener
    }

    fun setOnPieceClickListener(onPieceClick: OnPieceClick?) {
        this.onPieceClick = onPieceClick
    }


    interface OnPieceSelectedListener {
        fun onPieceSelected(piece: PuzzlePiece?, position: Int)
    }

    fun moveLeft() {
        if (currentPiece != null) {
            currentPiece?.record()
            currentPiece?.translate(-5f, 0f)
            invalidate()
        } else {
            currentPiece = puzzlePieces[0]
            currentPiece?.record()
            currentPiece?.translate(-5f, 0f)
            invalidate()
        }
    }

    fun moveRight() {
        if (currentPiece != null) {
            currentPiece?.record()
            currentPiece?.translate(5f, 0f)
            invalidate()
        } else {
            currentPiece = puzzlePieces[0]
            currentPiece?.record()
            currentPiece?.translate(5f, 0f)
            invalidate()
        }
    }

    fun moveUp() {
        if (currentPiece != null) {
            currentPiece?.record()
            currentPiece?.translate(0f, -5f)
            invalidate()
        } else {
            currentPiece = puzzlePieces[0]
            currentPiece?.record()
            currentPiece?.translate(0f, -5f)
            invalidate()
        }
    }

    fun moveDown() {
        if (currentPiece != null) {
            currentPiece?.record()
            currentPiece?.translate(0f, 5f)
            invalidate()
        } else {
            currentPiece = puzzlePieces[0]
            currentPiece?.record()
            currentPiece?.translate(0f, 5f)
            invalidate()
        }
    }

    fun rotatePiece() {
        if (currentPiece != null) {
            currentPiece?.record()
            rotate(90f)
            pieceAppliedEditing.add(currentPiece)
            invalidate()
        } else {
            currentPiece = puzzlePieces[0]
            currentPiece?.record()
            rotate(90f)
            pieceAppliedEditing.add(currentPiece)
            invalidate()
        }
    }

    fun zoomInPiece() {
        currentPiece?.let { currentPiece ->
            val matrixValues = FloatArray(9) // Matrix values array
            currentPiece.matrix.getValues(matrixValues)
            val zoomMinMax = matrixValues[0]
            if (abs(zoomMinMax.toDouble()) > 4.5) {
            } else {
                currentPiece.record()
                currentPiece.zoom(1.1f, 1.1f, currentPiece.areaCenterPoint)
                invalidate()
            }
        }
    }

    fun zoomOutPiece() {
        currentPiece?.let { currentPiece ->
            val matrixValues = FloatArray(9) // Matrix values array
            currentPiece.matrix.getValues(matrixValues)
            var zoomMinMax = matrixValues[0]
            if (abs(zoomMinMax.toDouble()).toInt() == 0) {
                zoomMinMax = matrixValues[1]
            }
            if (abs(zoomMinMax.toDouble()) < 0.3) {
            } else {
                currentPiece.record()
                currentPiece.zoom(0.9f, 0.9f, currentPiece.areaCenterPoint)
                invalidate()
            }
        }
    }

    fun mirrorPiece() {
        currentPiece?.let { currentPiece ->
            currentPiece.record()
            currentPiece.postFlipHorizontally()
            pieceAppliedEditing.add(currentPiece)
            invalidate()
        }
    }

    fun flipPiece() {
        currentPiece?.let { currentPiece ->
            currentPiece.record()
            currentPiece.postFlipVertically()
            pieceAppliedEditing.add(currentPiece)
            invalidate()
        }
    }

    fun swipePieceFunction() {
        if (currentPiece != null) {
            swipeTempPiece = currentPiece
        }
    }

    fun swipePieceFunctionPerform() {
        if (currentPiece != null) {
            temp = currentPiece?.getDrawable()
            tempPath = currentPiece?.path
            currentPiece?.setDrawable(swipeTempPiece?.getDrawable() ?: return)
            currentPiece?.path = swipeTempPiece?.path ?: return
            swipeTempPiece?.setDrawable(temp ?: return)
            tempPath?.let { swipeTempPiece?.path = it }

            handlingPiece?.fillArea(this, true)
            swipeTempPiece?.fillArea(this, true)

            swipeActivated = false
        }
    }

    fun clearPieceAppliedEditing() {
        pieceAppliedEditing.clear()
    }

    fun setSizeChangedOnce(value: Boolean) {
        sizeChangedOnce = value
    }

    val gestureDetector: GestureDetector = GestureDetector(object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            onPieceClick?.onPieceClick()
            return true
        }
    })

    init {
        context?.let { init(it, attrs) }
    }

    interface OnPieceClick {
        fun onPieceClick()

        fun onSwapGetPositions(pos1: Int, pos2: Int)
    }

    companion object {
        private const val TAG = "PuzzleView"
    }
}