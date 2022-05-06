package jp.kawagh.icetile


data class Puzzle(
    val startX: Int,
    val startY: Int,
    val goalX: Int,
    val goalY: Int,
    val grid: String
)

fun generateRandomPuzzle(): Puzzle {
    val content = List(79) { listOf('.', '#').random() }.joinToString("")
    val grid = "s" + content + "g"
    return Puzzle(
        startX = 0,
        startY = 0,
        goalX = 9,
        goalY = 9,
        grid = grid,
    )
}