package net.sheltem.aoc.y2024

import net.sheltem.common.*
import net.sheltem.common.Direction.*
import kotlin.math.pow

suspend fun main() {
    Day18().run(true)
}

class Day18 : Day<String>("146", "15") {

    override suspend fun part1(input: List<String>): String = input.map { it.toPos() }.runMaze(1024, 70, 70)!!.size.toString()

    override suspend fun part2(input: List<String>): String {
        val bytes = input.map { it.toPos() }

        (1024..input.lastIndex)
            .fold(bytes.runMaze(1024, 70, 70)) { acc, i ->
                when {
                    (acc == null) -> return input[i - 2]
                    acc.contains(bytes[i - 1]) -> bytes.runMaze(i, 70, 70)
                    else -> acc
                }
            }
        return ""
    }

    private fun String.toPos() = this.split(",").let { (l, r) -> l.toInt() to r.toInt() }

    private fun List<PositionInt>.runMaze(bytes: Int, maxX: Int, maxY: Int): List<PositionInt>? =
        List(maxY + 1) { ".".repeat(maxX + 1) }
            .toGrid()
            .apply {
                for (pos in this@runMaze.take(bytes)) {
                    this[pos] = '#'
                }
            }.let { grid ->
                grid.aStar(0 to 0, maxX to maxY, { grid.contains(it) && grid[it] != '#' })
            }
}
