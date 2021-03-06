package jp.kawagh.icetile

import org.junit.Assert.*

import org.junit.Test

class PuzzleTest {

    @Test
    fun testSolvePuzzle() {
        val puzzle = Puzzle(
            startX = 0, startY = 0,
            goalX = 8, goalY = 8,
            grid = """
        s........
        .........
        .........
        .........
        .........
        .........
        .........
        .........
        ........g
    """.trimIndent().replace(Regex("""\n"""), "")
        )
        assertTrue(
            solvePuzzle(puzzle).toString(), solvePuzzle(puzzle) == SolvingResult.Success(2)
        )
    }

    @Test
    fun `One Move to goal`() {
        val puzzle = Puzzle(
            startX = 0, startY = 0,
            goalX = 0, goalY = 8,
            grid = """
        s........
        .........
        .........
        .........
        .........
        .........
        .........
        .........
        g........
    """.trimIndent().replace(Regex("""\n"""), "")
        )
        assertTrue(
            solvePuzzle(puzzle).toString(), solvePuzzle(puzzle) == SolvingResult.Success(1)
        )
    }
}