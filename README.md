
[![](https://jitpack.io/v/hypersoftdev/PuzzleLayout.svg)](https://jitpack.io/#hypersoftdev/PuzzleLayout)
# PuzzleLayouts

**PuzzleLayouts** is a flexible Android library that helps you build custom collage layouts easily. This library offers a variety of built-in layouts and allows you to create your own custom layouts for images.
You can check out a demo of PuzzleLayouts in action on our [YouTube channel](https://www.youtube.com/watch?v=RTJaQhUUhls&t=18s&ab_channel=CelestialBeats). Watch how easy it is to create stunning collages with just a few simple steps!

## Layout Types

PuzzleLayouts comes with two main types of layouts:

1. **Straight Layout** - Clean, straight cuts for a polished look.
2. **Slant Layout** - Dynamic, angled cuts for a more creative style.

Below are some sample layouts you can achieve with the library:

![Straight Layout Example](https://github.com/user-attachments/assets/b0a8134c-6cb4-4ab5-ae6e-aaf69ed535cb)
![Slant Layout Example](https://github.com/user-attachments/assets/3cafb2f6-82b7-4991-91bd-6bf4a6dd90bf)

---
## Step-by-Step Usage:

### 1. Dependency Addition

To use the PuzzleLayouts, follow these steps to update your Gradle files.

#### Gradle Integration

##### Step A: Add Maven Repository
In your **project-level** `build.gradle` or `settings.gradle` file, add the following repository:

```
repositories {
    google()
    mavenCentral()
    maven { url "https://jitpack.io" }
}
```

### Step B: Add Dependencies

Include the PuzzleLayouts library in your **app-level** `build.gradle` file. Replace `x.x.x` with the latest version: [![](https://jitpack.io/v/hypersoftdev/PuzzleLayout.svg)](https://jitpack.io/#hypersoftdev/PuzzleLayout)

```
implementation 'com.github.hypersoftdev:PuzzleLayout:x.x.x'
```

## Implementation

### XML Usage

You can easily integrate **PuzzleLayouts** in your XML layout:

```
<com.hypersoft.pzlayout.SquarePuzzleView
    android:id="@+id/puzzle_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/white"
    app:radian="30" />

<com.hypersoft.pzlayout.view.PuzzleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white"
    app:radian="30" />

```

### Kotlin Usage

After setting up your layout in XML, you can modify the behavior and appearance of the puzzle view in your Kotlin code:

```
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

> **Note**: Handling Actions

You can perform various transformations and actions on the pieces within the puzzle view:

```
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

### Gesture Detector Support

PuzzleLayouts also supports gesture-based interactions:

```
puzzleView.setOnPieceClickListener(this)
puzzleView.setOnPieceSelectedListener(this)

override fun onPieceClick() {
    // Perform operation when a single piece is selected
}

override fun onSwapGetPositions(pos1: Int, pos2: Int) {
    // When two images are swapped, get the positions of the images
}

override fun onPieceSelected(piece: PuzzlePiece?, position: Int) {
    // You can get the following attributes when a piece is selected
    piece?.drawable
    piece?.areaCenterPoint
    piece?.path
    piece?.height
    piece?.width
    piece?.currentDrawablePoints
    piece?.area
    piece?.matrix
    piece?.matrixAngle
}
```

> **Note**: Custom Layouts

Creating a custom layout is straightforward. Simply extend PuzzleLayout and override the layout method.

```
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

> **Important**: Some of the provided helper methods include:

```
addLine(position = 0, direction = Line.Direction.VERTICAL, ratio = 2f / 3)
cutAreaEqualPart(position = 0, hSize = 1, vSize = 1)
cutAreaEqualPart(position = 0, part = 4, direction = Line.Direction.HORIZONTAL)
addCross(position = 0, horizontalRatio = 2f / 3, verticalRatio = 1f / 3)
addCross(position = 0, ratio = 2f / 3)
cutSpiral(position = 0)
```

## Attributes

**PuzzleView** is a custom Android view that allows users to interact with puzzle pieces, offering features like selection, manipulation, and customization.

| Attribute                 | Format          | Description                                                                                     |
|---------------------------|-----------------|-------------------------------------------------------------------------------------------------|
| `setSelected(position)`    | `Int`           | Selects a puzzle piece at the specified position.                                               |
| `handlingPiecePosition`    | `Int`           | Gets the position index of the currently handling puzzle piece.                                 |
| `hasPieceSelected()`       | `Boolean`       | Checks if any puzzle piece is currently selected.                                               |
| `setAnimateDuration(duration)` | `Int`       | Sets the animation duration for puzzle piece transformations.                                   |
| `isNeedDrawLine()`         | `Boolean`       | Returns whether lines should be drawn between puzzle pieces.                                    |
| `setNeedDrawLine(needDrawLine)` | `Boolean` | Sets whether lines should be drawn between pieces.                                              |
| `getLineColor()`           | `Int`           | Gets the current line color used in the puzzle layout.                                          |
| `setLineColor(lineColor)`  | `Int`           | Sets the line color for the puzzle layout and invalidates the view for redrawing.               |
| `moveLeft()`               | `Void`          | Moves the currently selected piece 5 units to the left.                                         |
| `moveRight()`              | `Void`          | Moves the currently selected piece 5 units to the right.                                        |
| `rotatePiece()`            | `Void`          | Rotates the currently selected piece by 90 degrees.                                             |
| `zoomInPiece()`            | `Void`          | Zooms in on the currently selected piece by 10%.                                                |
| `zoomOutPiece()`           | `Void`          | Zooms out of the currently selected piece by 10%.                                               |
| `mirrorPiece()`            | `Void`          | Flips the currently selected piece horizontally.                                                |
| `flipPiece()`              | `Void`          | Flips the currently selected piece vertically.                                                  |
| `setPiecePadding(padding)` | `Float`         | Sets the padding for each puzzle piece.                                                         |
| `setPieceRadian(radian)`   | `Float`         | Sets the corner radius for puzzle pieces.                                                       |
| `setQuickMode(quickMode)`  | `Boolean`       | Enables or disables quick mode for piece manipulation.                                          |
| `setOnPieceSelectedListener(onPieceSelectedListener)` | `OnPieceSelectedListener` | Sets the listener to be notified when a piece is selected.|
| `setOnPieceClickListener(onPieceClick)` | `OnPieceClick` | Sets the listener to handle piece click events.                                      |

---


## Sample Screens
Below are some sample layouts you can achieve using the helper methods:

![Sample Layout 1](https://github.com/user-attachments/assets/c32cef1e-ef1e-40c6-844a-758e64376db2)
![Sample Layout 2](https://github.com/user-attachments/assets/94b9f06d-2a1a-47a2-8ac5-f69073427400)
![Sample Layout 3](https://github.com/user-attachments/assets/c39a48bc-a589-44f7-8378-29e5abf55ce0)



# Acknowledgements

This work would not have been possible without the invaluable contributions of [Abdul Rehman Hassan](https://github.com/CelestialBeats). His expertise, dedication, and unwavering support have been instrumental in bringing this project to fruition.

![Profile](https://github.com/hypersoftdev/PuzzleLayout/blob/master/screens/image_profile.jpg?raw=true)

We are deeply grateful for [Abdul Rehman Hassan](https://github.com/CelestialBeats) involvement and his belief in the importance of this work. His contributions have made a significant impact, and we are honored to have had the opportunity to collaborate with him.

# LICENSE

Copyright 2023 Hypersoft Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
