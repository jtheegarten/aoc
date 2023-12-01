package net.sheltem.aoc.y2016

import net.sheltem.aoc.common.Direction.EAST
import net.sheltem.aoc.common.Direction.NORTH
import net.sheltem.aoc.common.Direction.SOUTH
import net.sheltem.aoc.common.Direction.WEST
import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.move

suspend fun main() {
    Day02().run()
}

val numberPad = listOf(listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9"))
val sillyPad = listOf(listOf("X", "X", "1", "X", "X"), listOf("X", "2", "3", "4", "X"), listOf("5", "6", "7", "8", "9"), listOf("X", "A", "B", "C", "X"), listOf("X", "X", "D", "X", "X"))

class Day02 : Day<String>("1985", "5DB3") {

    override suspend fun part1(input: List<String>): String {
        return input.inputNumbers()

    }

    override suspend fun part2(input: List<String>): String {
        return input.inputNumbers(PositionInt(0, 2), sillyPad)
    }
}

private fun List<String>.inputNumbers(start: PositionInt = PositionInt(1, 1), keyPad: List<List<String>> = numberPad): String {
    var result = ""
    var position = start
    for (numberInput in this) {
        position = numberInput.move(position, keyPad)
        result += keyPad[position.second][position.first]
    }
    return result
}

private fun String.move(position: Pair<Int, Int>, keyPad: List<List<String>>): Pair<Int, Int> {
    var currentPosition = position
    for (direction in this) {
        currentPosition = when (direction) {
            'U' -> {
                val newPosition = currentPosition.move(NORTH)
                if (newPosition.second < 0 || keyPad[newPosition.second][newPosition.first] == "X") {
                    currentPosition
                } else {
                    newPosition
                }
            }

            'D' -> {
                val newPosition = currentPosition.move(SOUTH)
                if (newPosition.second > (keyPad.size - 1) || keyPad[newPosition.second][newPosition.first] == "X") {
                    currentPosition
                } else {
                    newPosition
                }
            }

            'L' -> {
                val newPosition = currentPosition.move(WEST)
                if (newPosition.first < 0 || keyPad[newPosition.second][newPosition.first] == "X") {
                    currentPosition
                } else {
                    newPosition
                }
            }
            'R' -> {
                val newPosition = currentPosition.move(EAST)
                if (newPosition.first > (keyPad[0].size - 1) || keyPad[newPosition.second][newPosition.first] == "X") {
                    currentPosition
                } else {
                    newPosition
                }
            }
            else -> currentPosition
        }
    }
    return currentPosition
}
