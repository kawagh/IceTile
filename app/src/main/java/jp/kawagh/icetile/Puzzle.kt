package jp.kawagh.icetile

data class Puzzle(
    val startX: Int,
    val startY: Int,
    val goalX: Int,
    val goalY: Int,
    val grid: String
)