package net.sheltem.aoc.y2023

import net.sheltem.common.Direction
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.NORTH
import net.sheltem.common.Direction.SOUTH
import net.sheltem.common.Direction.WEST
import net.sheltem.common.PositionInt
import net.sheltem.common.mapParallel
import net.sheltem.common.move
import net.sheltem.common.within

suspend fun main() {
    Day16().run()
}

class Day16 : Day<Int>(46, 51) {

    override suspend fun part1(input: List<String>): Int = input
        .beam()

    override suspend fun part2(input: List<String>): Int = input
        .toStarts()
        .mapParallel { (position, direction) ->
            input.beam(position, direction)
        }
        .max()
}


private fun List<String>.toStarts(): MutableSet<Pair<PositionInt, Direction>> =
    (this.indices.flatMap { setOf((0 to it) to EAST, (this[0].length - 1 to it) to WEST) } +
            this.first().mapIndexed { index, _ -> (index to 0) to SOUTH } +
            this.last().mapIndexed { index, _ -> (index to this.size - 1) to NORTH }).toMutableSet()


private fun List<String>.beam(
    startPosition: PositionInt = 0 to 0,
    startDirection: Direction = EAST,
    visited: MutableSet<PositionInt> = mutableSetOf(),
    splitsDone: MutableSet<PositionInt> = mutableSetOf()
): Int {

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

                else -> {}
            }

            '|' -> when (direction) {
                EAST, WEST -> {
                    direction = SOUTH
                    if (splitsDone.contains(position)) break else
                        splitsDone.add(position)
                    this.beam(position, NORTH, visited, splitsDone)
                }

                else -> {}
            }
        }
        position = position.move(direction)
    }

    return visited.size
}
