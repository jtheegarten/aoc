package net.sheltem.aoc.y2024

import net.sheltem.common.PositionInt
import net.sheltem.common.intAt
import net.sheltem.common.neighbours
import net.sheltem.common.withinArrayMap

suspend fun main() {
    Day10().run()
}

class Day10 : Day<Long>(36, 81) {

    override suspend fun part1(input: List<String>): Long = input.map { it.toCharArray().map { char -> char.digitToInt() }.toTypedArray().toIntArray() }.trailheads().sumOf { it.toSet().size }.toLong()

    override suspend fun part2(input: List<String>): Long = input.map { it.toCharArray().map { char -> char.digitToInt() }.toTypedArray().toIntArray() }.trailheads().sumOf { it.size }.toLong()

}

private fun List<IntArray>.trailheads(): List<List<PositionInt>> = indices
    .flatMap { y -> this[y].indices.map { x -> x to y } }
    .filter { this.intAt(it) == 0 }
    .map { this.nines(it) }

private fun List<IntArray>.nines(pos: PositionInt): List<PositionInt> = pos
    .neighbours { next -> next.withinArrayMap(this) && (this.intAt(pos)!! + 1) == this.intAt(next) }
    .let { neighbours ->
        when {
            this.intAt(pos) == 9 -> listOf(pos)
            neighbours.isEmpty() -> emptyList()
            else -> neighbours.flatMap { this.nines(it) }
        }
    }
