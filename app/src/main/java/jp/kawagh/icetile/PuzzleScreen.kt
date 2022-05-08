package jp.kawagh.icetile

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun PuzzleScreen(viewModel: PuzzleViewModel = PuzzleViewModel()) {
    val tileColorMap = mapOf('.' to Color.Cyan, 'g' to Color.White, '#' to Color.Gray)
    val rotateMap = mapOf(
        Direction.Up to 0f,
        Direction.Right to 90f,
        Direction.Down to 180f,
        Direction.Left to 270f
    )
    var directionState by remember {
        mutableStateOf("-")
    }
    val animatedStateX = animateIntAsState(targetValue = viewModel.x)
    val animatedStateY = animateIntAsState(targetValue = viewModel.y)
    Column(
        Modifier
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
                            ">" -> viewModel.moveRight()
                            "<" -> viewModel.moveLeft()
                            "v" -> viewModel.moveDown()
                            "^" -> viewModel.moveUp()
                        }
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .background(Color.Black),
        ) {
            val tileSideLength = size.minDimension / (viewModel.gridSideLength)
            viewModel.puzzle.grid.forEachIndexed { index, c ->
                val row = index / viewModel.gridSideLength
                val col = index % viewModel.gridSideLength
                if (row == animatedStateY.value && col == animatedStateX.value) {
                    // drawCurrentPosition
                    drawTile(Color.Blue, col, row, tileSideLength)
                    val rotatePivot = Offset(
                        col * tileSideLength + tileSideLength / 2,
                        row * tileSideLength + tileSideLength / 2,
                    )
                    rotate(degrees = 45f + rotateMap[viewModel.direction]!!, pivot = rotatePivot) {
                        drawRect(
                            Color.Yellow,
                            topLeft = Offset(
                                col * tileSideLength + tileSideLength / 4,
                                row * tileSideLength + tileSideLength / 4
                            ),
                            size = Size(tileSideLength / 4, tileSideLength / 4),
                        )
                    }
                } else {
                    if (tileColorMap.containsKey(c)) {
                        drawTile(tileColorMap[c]!!, col, row, tileSideLength)
                    }
                }
            }
        }
        Text(text = "state: x:${viewModel.x},y:${viewModel.y}")
        Text(text = "${viewModel.puzzle.grid.length}")
        Button(onClick = { viewModel.generatePuzzle() }) {
            Text("gen")
        }
    }
}

private fun DrawScope.drawTile(color: Color, col: Int, row: Int, tileSideLength: Float) {
    drawRect(
        color = color,
        topLeft = Offset(
            x = col * tileSideLength,
            y = row * tileSideLength
        ),
        size = Size(tileSideLength, tileSideLength)
    )
}
