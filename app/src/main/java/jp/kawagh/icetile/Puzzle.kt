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
    val sx = choices[0] % width
    val sy = choices[0] / width
    val gx = choices[1] % width
    val gy = choices[1] / width
    gridArray[sy][sx] = 's'
    gridArray[gy][gx] = 'g'
    for (index in choices.subList(2, choices.size)) {
        val x = index % width
        val y = index / width
        gridArray[y][x] = '#'
    }
    val grid = gridArray.joinToString("") { it.joinToString("") }
    return Puzzle(sx, sy, gx, gy, grid)
}

fun solvePuzzle(puzzle: Puzzle): SolvingResult<Int> {
    val dist = Array(9) { IntArray(9) { -1 } }
    val queue = mutableListOf(Triple(puzzle.startX, puzzle.startY, 0))
    dist[puzzle.startY][puzzle.startX] = 0

    val prev: MutableMap<Pair<Int, Int>, Pair<Int, Int>> = mutableMapOf(
        Pair(puzzle.startX, puzzle.startY) to
                Pair(-1, -1)
    )
    val dx = listOf(0, 1, 0, -1)
    val dy = listOf(1, 0, -1, 0)
    while (queue.isNotEmpty()) {
        val (orgX, orgY, d) = queue.removeFirst()
        var x = orgX
        var y = orgY
        for (i in dx.indices) {
            var nx = orgX + dx[i]
            var ny = orgY + dy[i]
            var moved = false
            while (nx in 0..8 && ny in 0..8 && puzzle.grid[9 * ny + nx] != '#') {
                x = nx
                y = ny
                nx = x + dx[i]
                ny = y + dy[i]
                moved = true
            }
            if (moved && dist[y][x] == -1) {
                queue.add(Triple(x, y, d + 1))
                dist[y][x] = d + 1
                prev[Pair(x, y)] = Pair(orgX, orgY)
            }
        }
    }
    return if (dist[puzzle.goalY][puzzle.goalX] == -1) {
        SolvingResult.Fail
    } else {
        SolvingResult.Success(dist[puzzle.goalY][puzzle.goalX])
//        val route = mutableListOf(prev.getValue(Pair(puzzle.goalX, puzzle.goalY)))
//        while (route.last() != Pair(puzzle.startX, puzzle.startY)) {
//            val pv = prev.getValue(route.last())
//            route.add(pv)
//        }
//        SolvingResult.Success(route.size)
    }
}

sealed class SolvingResult<Int> {
    data class Success(val steps: Int) : SolvingResult<Int>()
    object Fail : SolvingResult<Int>()
}
