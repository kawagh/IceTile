package jp.kawagh.icetile

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

@Composable
fun PuzzleScreen() {
    var currentPositionX by remember {
        mutableStateOf(0)
    }
    var currentPositionY by remember {
        mutableStateOf(0)
    }
    var directionState by remember {
        mutableStateOf("-")
    }
    val gridSideLength = 5
    val animatedStateX = animateIntAsState(targetValue = currentPositionX)
    val animatedStateY = animateIntAsState(targetValue = currentPositionY)
    Column(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
                        change.consumeAllChanges()
                        val (x, y) = dragAmount
                        if (abs(x) > abs(y)) {
                            directionState = if (x > 0) ">" else "<"
                        } else {
                            directionState = if (y > 0) "v" else "^"
                        }
                    },
                    onDragEnd = {
                        when (directionState) {
                            ">" -> {
                                currentPositionX = gridSideLength - 1
                            }
                            "<" -> {
                                currentPositionX = 0
                            }
                            "v" -> {
                                currentPositionY = gridSideLength - 1
                            }
                            "^" -> {
                                currentPositionY = 0
                            }
                        }
                    }
                )
            }
    ) {
        Button(onClick = {
            currentPositionX = if (currentPositionX == 0) {
                gridSideLength - 1
            } else {
                0
            }
        }) {
            Text(text = "move")
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            val tileSideLength = size.minDimension / (2 * gridSideLength)
            val tileSize = Size(tileSideLength, tileSideLength)
            repeat(gridSideLength * gridSideLength) { index ->
                val row = index / 5
                val col = index % 5
                if (row == animatedStateY.value && col == animatedStateX.value) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(x = col * tileSideLength, y = row * tileSideLength),
                        size = tileSize
                    )
                } else {
                    drawRect(
                        color = Color.Cyan,
                        topLeft = Offset(x = col * tileSideLength, y = row * tileSideLength),
                        size = tileSize
                    )
                }
            }
        }
    }
}
