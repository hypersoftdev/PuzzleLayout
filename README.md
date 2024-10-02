**PuzzleLayouts - Collage Layouts Library for Android**

PuzzleLayouts is a flexible Android library that helps you build custom collage layouts easily. This library comes with a variety of built-in layouts and allows you to create your own custom layouts for images.

You can check out a demo of PuzzleLayouts in action on our [YouTube channel](https://www.youtube.com/watch?v=RTJaQhUUhls&t=18s&ab_channel=CelestialBeats).Watch how easy it is to create stunning collages with a few simple steps!

**Layout Types**

PuzzleLayouts comes with two types of layouts:

1.**Straight Layout** - clean, straight cuts for a polished look.

2.**Slant Layout** - dynamic, angled cuts for a more creative style.

Below are some sample layouts you can achieve with the library.

![straight](https://github.com/user-attachments/assets/0f04c2fa-9ac4-4475-b02f-2eaa7eac17ea) ![slant](https://github.com/user-attachments/assets/950d3f31-cd80-43dd-a2f6-8286f9509be9)

**XML Usage**

You can easily integrate PuzzleLayouts in your XML layout:
```kotlin
<com.puzzle.layouts.SquarePuzzleView
    android:id="@+id/puzzle_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/white"
    app:radian="30" />

<com.puzzle.layouts.view.PuzzleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white"
    app:radian="30" />
```
**Kotlin Usage**

After setting up your layout in XML, you can modify the behavior and appearance of the puzzle view in your Kotlin code:

```kotlin
puzzleView.isTouchEnable = true
puzzleView.needDrawLine = false
puzzleView.needDrawOuterLine = false
puzzleView.lineSize = 6
puzzleView.lineColor = Color.BLACK
puzzleView.selectedLineColor = ContextCompat.getColor(context, R.color.black)
puzzleView.setAnimateDuration(700)
puzzleView.piecePadding = 10f

puzzleView.setCanSwap(false)
puzzleView.setCanZoom(false)

```
> [!NOTE]
**Handling Actions**

You can perform various transformations and actions on the pieces within the puzzle view:

```kotlin
puzzleView.mirrorPiece()
puzzleView.flipPiece()
puzzleView.rotatePiece()
puzzleView.zoomInPiece()
puzzleView.zoomOutPiece()
puzzleView.moveLeft()
puzzleView.moveRight()
puzzleView.moveUp()
puzzleView.moveDown()
puzzleView.setLineSize(currentDegrees)
puzzleView.setPieceRadian(currentDegrees)
puzzleView.setNeedDrawLine(!puzzleView.isNeedDrawLine())
puzzleView.swipePieceFunction()

```
**Gesture Detector Support**

PuzzleLayouts also supports gesture-based interactions:

```kotlin
puzzleView.setOnPieceClickListener(this)
puzzleView.setOnPieceSelectedListener(this)
```
> [!NOTE]
**Custom Layouts**

Creating a custom layout is straightforward. Simply extend PuzzleLayout and override the layout method.

```kotlin
class FiveStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 7
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 5, Line.Direction.HORIZONTAL)
            1 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 5)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                cutAreaEqualPart(2, 3, Line.Direction.VERTICAL)
            }

            2 -> {
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4)
                cutAreaEqualPart(1, 4, Line.Direction.VERTICAL)
            }

            3 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 3)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                addLine(3, Line.Direction.VERTICAL, 1f / 2)
            }

            4 -> {
                addCross(0, 1f / 3)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 2)
            }

            5 -> cutSpiral(0)

            6 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
                cutAreaEqualPart(0, 1, 1)
            }

            else -> cutAreaEqualPart(0, 5, Line.Direction.HORIZONTAL)
        }
    }
}
```
> [!IMPORTANT]
Some of the provided helper methods include:

```kotlin
addLine(position = 0, direction = Line.Direction.VERTICAL, ratio = 2f / 3)
cutAreaEqualPart(position = 0, hSize = 1, vSize = 1)
cutAreaEqualPart(position = 0, part = 4, direction = Line.Direction.HORIZONTAL)
addCross(position = 0, horizontalRatio = 2f / 3, verticalRatio = 1f / 3)
addCross(position = 0, ratio = 2f / 3)
cutSpiral(position = 0)
```



