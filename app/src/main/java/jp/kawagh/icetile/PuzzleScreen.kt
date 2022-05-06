package jp.kawagh.icetile

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun PuzzleScreen() {
    var currentPosition by remember {
        mutableStateOf(0)
    }
    val repeatTimes = 5
    val animatedState = animateIntAsState(targetValue = repeatTimes - 1 - currentPosition)
    Column(Modifier.fillMaxSize()) {
        Button(onClick = {
            currentPosition = if (currentPosition == 0) {
                repeatTimes - 1
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
            val tileSideLength = size.minDimension / (2 * repeatTimes)
            val tileSize = Size(tileSideLength, tileSideLength)
            repeat(repeatTimes) { index ->
                if (index == animatedState.value) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(x = index * tileSideLength, y = 0f),
                        size = tileSize
                    )
                } else {
                    drawRect(
                        color = Color.Cyan,
                        topLeft = Offset(x = index * tileSideLength, y = 0f),
                        size = tileSize
                    )
                }
            }
        }
    }
}
