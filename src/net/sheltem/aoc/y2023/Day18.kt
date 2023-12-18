package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.Direction
import net.sheltem.aoc.common.Direction.EAST
import net.sheltem.aoc.common.Direction.NORTH
import net.sheltem.aoc.common.Direction.SOUTH
import net.sheltem.aoc.common.Direction.WEST
import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.gaussArea
import net.sheltem.aoc.common.move
import net.sheltem.aoc.y2023.Day18.Trench

suspend fun main() {
    Day18().run()
}

@OptIn(ExperimentalStdlibApi::class)
class Day18 : Day<Long>(62, 952408144115) {

    override suspend fun part1(input: List<String>): Long = input.toTrenches { line ->
        line.split(" ")
            .let { (dir, len) ->
                dir.toDirection() to len.toInt()
            }
    }.calculateArea()

    override suspend fun part2(input: List<String>): Long = input.toTrenches { line ->
        line.split(" ")
            .last()
            .dropLast(1)
            .drop(2)
            .let {
                it.last().toDirection() to it.dropLast(1).hexToInt()
            }
    }.calculateArea()

    data class Trench(val position: PositionInt, val length: Int)

}

private fun List<String>.toTrenches(parser: (String) -> Pair<Direction, Int>): List<Trench> {
    val result = mutableListOf<Trench>()
    var currentPosition = 0 to 0

    this.forEach { line ->
        val (dir, length) = parser(line)
        currentPosition = currentPosition.move(dir, length)
        result.add(Trench(currentPosition, length))
    }

    return result
}

private fun List<Trench>.calculateArea(): Long = this
    .sumOf { it.length }
    .let { border -> border + (this.map { it.position }.gaussArea() + 1 - border / 2) }

private fun String.toDirection() = Direction.from(this)
private fun Char.toDirection() = when (this) {
    '0' -> EAST
    '1' -> SOUTH
    '2' -> WEST
    '3' -> NORTH
    else -> throw IllegalStateException("$this is not a valid direction")
}