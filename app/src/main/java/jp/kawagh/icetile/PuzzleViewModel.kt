package jp.kawagh.icetile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {
    val gridSideLength = 9
    val puzzle = Puzzle(
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
    var x by mutableStateOf(puzzle.startX)
        private set
    var y by mutableStateOf(puzzle.startY)
        private set

    fun moveUp() {
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