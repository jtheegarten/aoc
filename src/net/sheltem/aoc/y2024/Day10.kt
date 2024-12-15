package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day10().run()
}

class Day10 : Day<Long>(36, 81) {

    override suspend fun part1(input: List<String>): Long = input.toGrid().transform { _, c -> c.digitToInt() }.trailheads().sumOf { it.toSet().size }.toLong()

    override suspend fun part2(input: List<String>): Long = input.toGrid().transform { _, c -> c.digitToInt() }.trailheads().sumOf { it.size }.toLong()

    private fun Grid<Int>.trailheads(): List<List<PositionInt>> =
        allCoordinates
            .filter { this[it] == 0 }
            .map { this.nines(it) }

    private fun Grid<Int>.nines(pos: PositionInt): List<PositionInt> = pos
        .neighbours { next -> this.contains(next) && this[pos]!! + 1 == this[next] }
        .let { neighbours ->
            when {
                this[pos] == 9 -> listOf(pos)
                neighbours.isEmpty() -> emptyList()
                else -> neighbours.flatMap { this.nines(it) }
            }
        }
}
