package net.sheltem.aoc.y2023

import net.sheltem.common.Direction
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.NORTH
import net.sheltem.common.Direction.SOUTH
import net.sheltem.common.Direction.WEST
import net.sheltem.common.move

suspend fun main() {
    Day14().run()
}

class Day14 : Day<Long>(136, 64) {

    override suspend fun part1(input: List<String>): Long = input.tilt()

    override suspend fun part2(input: List<String>): Long = input.tilt2(1_000_000_000)

}

private fun List<CharArray>.tilt(direction: Direction, i: Int, j: Int) {
    val char = this[i][j]
    if (char == 'O') {
        val distanceRolled =
            generateSequence((j to i).move(direction)) { it.move(direction) }
                .takeWhile { this[it.second][it.first] == '.' }
                .map { this[it.second][it.first] }
                .joinToString("")
                .let { charsAbove ->
                    val indexOfStop = charsAbove.indexOfFirst { it != '.' }
                    if (indexOfStop != -1) {
                        indexOfStop
                    } else {
                        charsAbove.length
                    }
                }
        val newPosition = (j to i).move(direction, distanceRolled)

        if (newPosition != j to i) {
            this[newPosition.second][newPosition.first] = 'O'
            this[i][j] = '.'
        }
    }
}

private fun List<String>.tilt(): Long {
    val border = "#".repeat(this[0].length + 2)
    val mapWithBorder = listOf(border) + this.map { "#$it#" } + listOf(border)

    val result = mapWithBorder.map { it.toCharArray() }

    for (i in mapWithBorder.indices.drop(2)) {
        for (j in mapWithBorder[0].indices) {
            result.tilt(NORTH, i, j)
        }
    }

    return result.reversed().mapIndexed { index, line -> index to line.count { char -> char == 'O' } }.sumOf { it.first * it.second }.toLong()
}

private fun List<String>.tilt2(runs: Int): Long {
    val border = "#".repeat(this[0].length + 2)
    val mapWithBorder = listOf(border) + this.map { "#$it#" } + listOf(border)

    val result = mapWithBorder.map { it.toCharArray() }

    val memory = mutableMapOf<String, Int>()
    var cycleFound = false

    var run = 1
    while (run <= runs) {
        for (direction in listOf(NORTH, WEST, SOUTH, EAST)) {
            when (direction) {
                NORTH -> {
                    for (i in mapWithBorder.indices.dropBorder()) {
                        for (j in mapWithBorder[0].indices.dropBorder()) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                WEST -> {
                    for (i in mapWithBorder.indices.dropBorder()) {
                        for (j in mapWithBorder[0].indices.dropBorder()) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                SOUTH -> {
                    for (i in mapWithBorder.indices.reversed().dropBorder()) {
                        for (j in mapWithBorder[0].indices.dropBorder()) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                EAST -> {
                    for (i in mapWithBorder.indices.dropBorder()) {
                        for (j in mapWithBorder[0].indices.reversed().dropBorder()) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                else -> continue
            }
        }
        val current = result.joinToString("") { it.joinToString("") }
        if (!memory.contains(current)) {
            memory[current] = run
        } else if (!cycleFound) {
            println("Repeated cycle found => ${memory[current]} to $run")
            val remainder = (runs - run).mod(run - memory[current]!!)
            run = runs - remainder
            cycleFound = true
            println("New run: $run")
        }
        run += 1
    }


    return result.reversed().mapIndexed { index, line -> index to line.count { char -> char == 'O' } }.sumOf { it.first * it.second }.toLong()
}

private fun IntProgression.dropBorder() = drop(1).dropLast(1)
