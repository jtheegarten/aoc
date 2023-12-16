package net.sheltem.aoc.y2023

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.sheltem.aoc.common.Direction
import net.sheltem.aoc.common.Direction.EAST
import net.sheltem.aoc.common.Direction.NORTH
import net.sheltem.aoc.common.Direction.SOUTH
import net.sheltem.aoc.common.Direction.WEST
import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.move
import net.sheltem.aoc.common.within

suspend fun main() {
    Day16().run()
}

class Day16 : Day<Long>(46, 51) {

    override suspend fun part1(input: List<String>): Long = input.beam().count().toLong()

    override suspend fun part2(input: List<String>): Long = input
        .toStarts()
        .map { start ->
            coroutineScope {
                async {
                    input.beam(start.first, start.second)
                }
            }
        }.awaitAll()
        .maxOf { it.size }.toLong()

}


private fun List<String>.toStarts(): MutableSet<Pair<PositionInt, Direction>> {
    val resultSet = mutableSetOf<Pair<PositionInt, Direction>>()
    for (y in indices) {
        resultSet.add((0 to y) to EAST)
        resultSet.add((this[0].indices.max() to y) to WEST)
    }
    this.first().mapIndexed { index, _ -> (index to 0) to SOUTH }.let(resultSet::addAll)
    this.last().mapIndexed { index, _ -> (index to this.indices.last()) to NORTH }.let(resultSet::addAll)

    return resultSet
}

private fun List<String>.beam(
    startPosition: PositionInt = 0 to 0,
    startDirection: Direction = EAST,
    visited: MutableSet<PositionInt> = mutableSetOf(),
    splitsDone: MutableSet<PositionInt> = mutableSetOf()
): MutableSet<PositionInt> {

    var direction = startDirection
    var position: PositionInt = startPosition

    while (position.within(this)) {
        visited.add(position)
        when (this[position.second][position.first]) {
            '/' -> direction = when (direction) {
                EAST -> NORTH
                WEST -> SOUTH
                SOUTH -> WEST
                NORTH -> EAST
                else -> throw IllegalStateException("Huh?")
            }

            '\\' -> direction = when (direction) {
                EAST -> SOUTH
                WEST -> NORTH
                SOUTH -> EAST
                NORTH -> WEST
                else -> throw IllegalStateException("Huh?")
            }

            '-' -> when (direction) {
                NORTH, SOUTH -> {
                    direction = WEST
                    if (splitsDone.contains(position)) break else
                        splitsDone.add(position)
                    this.beam(position, EAST, visited, splitsDone)
                }

                else -> direction = direction
            }

            '|' -> when (direction) {
                EAST, WEST -> {
                    direction = SOUTH
                    if (splitsDone.contains(position)) break else
                        splitsDone.add(position)
                    this.beam(position, NORTH, visited, splitsDone)
                }

                else -> direction = direction
            }
        }
        position = position.move(direction)
    }

    return visited
}
