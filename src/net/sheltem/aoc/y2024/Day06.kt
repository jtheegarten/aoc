package net.sheltem.aoc.y2024

import net.sheltem.common.Direction
import net.sheltem.common.PositionInt
import net.sheltem.common.move
import net.sheltem.common.within

suspend fun main() {
    Day06().run()
}

class Day06 : Day<Long>(41, 6) {

    override suspend fun part1(input: List<String>): Long = input
        .visit()
        .first
        .size
        .toLong()

    override suspend fun part2(input: List<String>): Long = input
        .visit()
        .first
        .count{ obstruction ->
            input.visit(obstruction).second
        }.toLong()

}

private fun List<String>.visit(obstruction: PositionInt? = null): Pair<Set<PositionInt>, Boolean> {
    val map = this.map { it.toCharArray() }
    var direction = Direction.NORTH

    var pos = map.indices
        .flatMap { y -> map[y].indices.map { x -> x to y } }
        .first { (x, y) -> map[y][x] == '^' }

    val visited = mutableSetOf<Pair<PositionInt, Direction>>()
    var loop = false

    while(true) {

        if (visited.contains(pos to direction)) {
            loop = true
            break
        } else {
            visited.add(pos to direction)
        }

        val next = pos.move(direction)
        when {
            !next.within(this) -> break
            map[next.second][next.first] == '#' || next == obstruction -> direction = direction.turnRight()
            else -> {
                pos = next
            }
        }
    }

    return visited.map { it.first }.toSet() to loop
}
