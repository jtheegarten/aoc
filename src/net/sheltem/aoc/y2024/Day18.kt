package net.sheltem.aoc.y2024

import net.sheltem.common.*
import net.sheltem.common.Direction.*
import kotlin.math.pow

suspend fun main() {
    Day18().run(true)
}

class Day18 : Day<String>("146", "15") {

    override suspend fun part1(input: List<String>): String = input.map { it.toPos() }.runMaze(1024, 70, 70).toString()

    override suspend fun part2(input: List<String>): String = input.map { it.toPos() }.let { byteList ->
        byteList[generateSequence (1024) { it + 1 }
            .first { byteList.runMaze(it, 70, 70) == 0L }.toInt() - 1]
    }.toString()

    private fun String.toPos() = this.split(",").let { (l, r) -> l.toInt() to r.toInt() }

    private fun List<PositionInt>.runMaze(bytes: Int, maxX: Int, maxY: Int): Long {
        val grid = List(maxY + 1) { ".".repeat(maxX + 1) }.toGrid()
        this.take(bytes)
            .forEach { pos ->
                grid[pos] = '#'
            }

        val visited = HashMap<PositionInt, Int>()
        val queue = ArrayDeque<Pair<PositionInt, Int>>()
        val goal = maxX to maxY
        queue.add((0 to 0) to 0)

        while (queue.isNotEmpty()) {
            val (current, cost) = queue.removeFirst()
            if (visited.getOrDefault(current, Int.MAX_VALUE) <= cost) continue

            if (current == goal) return cost.toLong()
            visited[current] = cost


            listOf(EAST, SOUTH, WEST, NORTH)
                .map { current move it }
                .filter { grid.contains(it) && grid[it] != '#' }
                .forEach { queue.add(it to (cost + 1)) }
        }

        return 0L
    }
}
