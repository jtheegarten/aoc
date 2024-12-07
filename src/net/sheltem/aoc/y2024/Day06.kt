package net.sheltem.aoc.y2024

import net.sheltem.common.*

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
        .countParallel{ obstruction ->
            input.visit(obstruction).second
        }.toLong()

}

private fun List<String>.visit(obstruction: PositionInt? = null): Pair<Set<PositionInt>, Boolean> {
    var direction = Direction.NORTH

    var pos = this.indices
        .flatMap { y -> this[y].indices.map { x -> x to y } }
        .first { (x, y) -> this[y][x] == '^' }

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
            this.charAt(next) == '#' || next == obstruction -> direction = direction.turnRight()
            else -> {
                pos = next
            }
        }
    }

    return visited.map { it.first }.toSet() to loop
}
