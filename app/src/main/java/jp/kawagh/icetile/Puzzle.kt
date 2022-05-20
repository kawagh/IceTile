package jp.kawagh.icetile


data class Puzzle(
    val startX: Int,
    val startY: Int,
    val goalX: Int,
    val goalY: Int,
    val grid: String
)

fun generateRandomPuzzle(): Puzzle {
    val height = 9
    val width = 9
    val blockCount = 10
    val gridArray = Array(height) { CharArray(width) { '.' } }
    val choices = (0 until height * width).shuffled().take(blockCount + 2)
    val sx = choices[0] / width
    val sy = choices[0] % width
    val gx = choices[1] / width
    val gy = choices[1] % width
    gridArray[sy][sx] = 's'
    gridArray[gy][gx] = 'g'
    for (index in choices.subList(2, choices.size)) {
        val x = index / width
        val y = index % width
        gridArray[x][y] = '#'
    }
    val grid = gridArray.joinToString("") { it.joinToString("") }
    return Puzzle(sx, sy, gx, gy, grid)
}