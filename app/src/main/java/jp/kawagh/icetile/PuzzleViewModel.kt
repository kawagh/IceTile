package jp.kawagh.icetile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {
    var x by mutableStateOf(0)
        private set
    var y by mutableStateOf(0)
        private set

    fun moveUp() {
        y = 0
    }

    fun moveDown() {
        y = 4
    }

    fun moveLeft() {
        x = 0
    }

    fun moveRight() {
        x = 4
    }
}