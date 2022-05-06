package jp.kawagh.icetile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {
    val gridSideLength = 5
    val puzzle = """
        -----
        ---x-
        x----
        -----
        --g--
    """.trimIndent().replace(Regex("""\n"""), "")

    var x by mutableStateOf(0)
        private set
    var y by mutableStateOf(0)
        private set

    fun moveUp() {
        while (true) {
            val nx = x
            val ny = y - 1
            val ni = gridSideLength * ny + nx
            if (isInside(nx, ny) && puzzle[ni] != 'x') {
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
            val ni = gridSideLength * ny + nx
            if (isInside(nx, ny) && puzzle[ni] != 'x') {
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
            val ni = gridSideLength * ny + nx
            if (isInside(nx, ny) && puzzle[ni] != 'x') {
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
            val ni = gridSideLength * ny + nx
            if (isInside(nx, ny) && puzzle[ni] != 'x') {
                x = nx
                y = ny
            } else {
                break
            }
        }
    }

    private fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until gridSideLength && y in 0 until gridSideLength
    }

}