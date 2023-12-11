package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.manhattan

suspend fun main() {
    Day11().run()
}

class Day11 : Day<Long>(374, 82000210) {

    override suspend fun part1(input: List<String>): Long = input.toUniverseMap(1).pairUp().sumOf { it.manhattan() }

    override suspend fun part2(input: List<String>): Long = input.toUniverseMap(999_999).pairUp().sumOf { it.manhattan() }
}

private fun List<PositionInt>.pairUp(): List<Pair<PositionInt, PositionInt>> {
    val visited = mutableListOf<PositionInt>()
    val result = mutableListOf<Pair<PositionInt, PositionInt>>()

    for (i in indices) {
        visited.add(this[i])
        this.filterNot { visited.contains(it) }
            .map {
                this[i] to it
            }.let(result::addAll)
    }

    return result
}

private fun List<String>.toUniverseMap(expand: Int): List<PositionInt> {
    val galaxies = this.flatMapIndexed { y, s ->
        s.mapIndexedNotNull { x, char ->
            if (char == '#') x to y else null
        }
    }

    val xRange = galaxies.minOf { it.first }..galaxies.maxOf { it.first }
    val yRange = galaxies.minOf{ it.second }..galaxies.maxOf { it.second }

    val emptyX = xRange - galaxies.map { it.first }.toSet()
    val emptyY = yRange - galaxies.map { it.second }.toSet()

    return galaxies.map {galaxy ->
        val offsetX = emptyX.count { it < galaxy.first } * expand
        val offsetY = emptyY.count { it < galaxy.second } * expand
        (galaxy.first + offsetX) to (galaxy.second + offsetY)
    }
}
