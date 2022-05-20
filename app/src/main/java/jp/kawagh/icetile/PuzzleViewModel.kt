package jp.kawagh.icetile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {
    val gridSideLength = 9
    var puzzle = Puzzle(
        startX = 4, startY = 0,
        goalX = 3, goalY = 1,
        grid = """
        #...s#..#
        ...g#....
        .........
        #..#.....
        ..#......
        .#.......
        .........
        ...#.....
        ......#..
    """.trimIndent().replace(Regex("""\n"""), "")

    )

    var direction by mutableStateOf<Direction>(Direction.Down)
        private set
    var x by mutableStateOf(puzzle.startX)
        private set
    var y by mutableStateOf(puzzle.startY)
        private set

    fun resetPosition() {
        x = puzzle.startX
        y = puzzle.startY
    }

    fun parsePuzzle(input: List<String>): Puzzle {
        val (sy, sx) = input[1].split(" ").map { it.toInt() }
        val (gy, gx) = input[2].split(" ").map { it.toInt() }
        val ngrid = input.subList(3, input.size).joinToString("")
        return Puzzle(startX = sx, startY = sy, goalX = gx, goalY = gy, grid = ngrid)
    }

    fun loadPuzzle(newPuzzle: Puzzle) {
        // to invoke rendering
        x = if (x == 0) 1 else 0
        puzzle = newPuzzle
        x = newPuzzle.startX
        y = newPuzzle.startY
    }


    fun isGoal(): Boolean = x == puzzle.goalX && y == puzzle.goalY

    fun moveUp() {
        direction = Direction.Up
        while (true) {
            val nx = x
            val ny = y - 1
            if (isMovable(nx, ny)) {
                x = nx
                y = ny
            } else {
                break
            }
        }
    }

    fun moveDown() {
        direction = Direction.Down
        while (true) {
            val nx = x
            val ny = y + 1
            if (isMovable(nx, ny)) {
                x = nx
                y = ny
            } else {
                break
            }
        }
    }

    fun moveLeft() {
        direction = Direction.Left
        while (true) {
            val nx = x - 1
            val ny = y
            if (isMovable(nx, ny)) {
                x = nx
                y = ny
            } else {
                break
            }
        }
    }

    fun moveRight() {
        direction = Direction.Right
        while (true) {
            val nx = x + 1
            val ny = y
            if (isMovable(nx, ny)) {
                x = nx
                y = ny
            } else {
                break
            }
        }
    }

    private fun isMovable(x: Int, y: Int): Boolean {
        return isInside(x, y) && !isBlock(x, y)
    }

    private fun isBlock(x: Int, y: Int): Boolean {
        val ni = gridSideLength * y + x
        return puzzle.grid[ni] == '#'
    }

    private fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until gridSideLength && y in 0 until gridSideLength
    }
}

sealed class Direction {
    object Up : Direction()
    object Right : Direction()
    object Down : Direction()
    object Left : Direction()
}