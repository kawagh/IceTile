package jp.kawagh.icetile

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PuzzleScreen(viewModel: PuzzleViewModel = PuzzleViewModel()) {
    val tileColorMap = mapOf('.' to Color.Cyan, 'g' to Color.White, '#' to Color.Gray)
    var resourceIndex by remember {
        mutableStateOf(0)
    }
    val resources = listOf(
        R.raw.p0,
        R.raw.p1,
        R.raw.p2,
        R.raw.p3,
        R.raw.p4,
        R.raw.p5,
        R.raw.p6,
        R.raw.p7,
        R.raw.p8,
        R.raw.p9,
    )
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
    val context = LocalContext.current
    val loadNextPuzzle: () -> Unit = {
        resourceIndex = resourceIndex.plus(1).rem(resources.size)
        val puzzleTextLines =
            context.resources.openRawResource(resources[resourceIndex]).bufferedReader()
                .use { it.readLines() }
        viewModel.loadPuzzle(viewModel.parsePuzzle(puzzleTextLines))
    }
    val scope = rememberCoroutineScope()
    val dataStore = AppDataStore(context)
    val isFirstPlay = dataStore.getValue.collectAsState(initial = false).value

    if (viewModel.isGoal()) {
        LaunchedEffect(Unit) {
            delay(500)
            loadNextPuzzle()
        }
    }
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
        if (isFirstPlay) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Tutorial") },
                text = { Text("Swipe screen to the goal\nYou cannot stop until reaching block.") },
                confirmButton = {
                    TextButton(onClick = { scope.launch { dataStore.saveFirstPlay() } }) {
                        Text("OK")
                    }
                }
            )
        }
        Text(
            "Q${resourceIndex + 1}/${resources.size}",
            fontSize = MaterialTheme.typography.h4.fontSize
        )
        Spacer(modifier = Modifier.size(10.dp))
        Canvas(
            modifier = Modifier
                .size(350.dp)
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
        Spacer(modifier = Modifier.size(10.dp))

        Row {
            Button(onClick = { viewModel.resetPosition() }) {
                Text("reset")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = {
                loadNextPuzzle()
            }) {
                Text("next")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = { viewModel.loadPuzzle(generateRandomSolvablePuzzle()) }) {
                Text("generate")
            }
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
