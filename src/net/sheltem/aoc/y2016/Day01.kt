package net.sheltem.aoc.y2016

import net.sheltem.aoc.common.Direction
import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.move
import kotlin.math.absoluteValue

fun main() {
    Day01().run()
}

class Day01 : Day<Int>(12, 12) {

    override fun part1(input: List<String>): Int {
        return input[0]
            .split(", ")
            .walk()

    }

    override fun part2(input: List<String>): Int {
        return input[0]
            .split(", ")
            .walk(true)
    }
}

private fun List<String>.walk(cancelOnDuplicate: Boolean = false): Int {
    var position = PositionInt(0, 0) to Direction.NORTH
    val visitedCoordinates = mutableListOf<Pair<Int, Int>>()
    for (move in this) {
        position = if (move.startsWith("L")) {
            position.first to position.second.turnLeft()
        } else {
            position.first to position.second.turnRight()
        }
        repeat(move.drop(1).toInt()) {
            position = position.first.move(position.second) to position.second
            if (cancelOnDuplicate && visitedCoordinates.contains(position.first)) {
                return position.first.first.absoluteValue + position.first.second.absoluteValue
            }
            visitedCoordinates += position.first
        }
    }
    return position.first.first.absoluteValue + position.first.second.absoluteValue
}
