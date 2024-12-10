package net.sheltem.aoc.y2024

import net.sheltem.common.PositionInt
import net.sheltem.common.intAt
import net.sheltem.common.neighbours
import net.sheltem.common.withinMap

suspend fun main() {
    Day10().run()
}

class Day10 : Day<Long>(36, 81) {

    override suspend fun part1(input: List<String>): Long = input.map { it.toCharArray().map { char -> char.digitToInt() } }.trailheads().sumOf { it.toSet().size }.toLong()

    override suspend fun part2(input: List<String>): Long = input.map { it.toCharArray().map { char -> char.digitToInt() } }.trailheads().sumOf { it.size }.toLong()

}

private fun List<List<Int>>.trailheads(): List<List<PositionInt>> = indices
    .flatMap { y -> this[y].indices.map { x -> x to y } }
    .filter { this[it.second][it.first] == 0 }
    .map { this.nines(it) }

private fun List<List<Int>>.nines(pos: PositionInt): List<PositionInt> = pos
    .neighbours()
    .filter { it.withinMap(this) && (this.intAt(pos)!! + 1) == this.intAt(it) }
    .let {
        when {
            this.intAt(pos) == 9 -> listOf(pos)
            it.isEmpty() -> emptyList()
            else -> it.flatMap { neighbour -> this.nines(neighbour) }
        }
    }
