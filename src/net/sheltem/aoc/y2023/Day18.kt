package net.sheltem.aoc.y2023

import net.sheltem.common.Direction
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.NORTH
import net.sheltem.common.Direction.SOUTH
import net.sheltem.common.Direction.WEST
import net.sheltem.common.PositionInt
import net.sheltem.common.gaussArea
import net.sheltem.common.manhattan
import net.sheltem.common.move
import net.sheltem.common.toDirection

suspend fun main() {
    Day18().run()
}

@OptIn(ExperimentalStdlibApi::class)
class Day18 : Day<Long>(62, 952408144115) {

    override suspend fun part1(input: List<String>): Long = input
        .map { line ->
            line.split(" ")
                .let { (dir, len) ->
                    dir.toDirection() to len.toInt()
                }
        }
        .toTrenches()
        .calculateArea()

    override suspend fun part2(input: List<String>): Long = input
        .map { line ->
            line.split("(#")
                .last()
                .dropLast(1)
                .let {
                    it.last().toDirection() to it.dropLast(1).hexToInt()
                }
        }
        .toTrenches()
        .calculateArea()
}

private fun List<Pair<Direction, Int>>.toTrenches(): List<PositionInt> =
    this.scan(0 to 0) { acc, (dir, length) -> acc.move(dir, length) }

private fun List<PositionInt>.calculateArea(): Long =
    this.windowed(2, 1)
        .sumOf { (left, right) -> left manhattan right }
        .let { border -> border + (this.gaussArea() + 1 - border / 2) }

private fun Char.toDirection() = listOf(EAST, SOUTH, WEST, NORTH)[this.digitToInt()]
