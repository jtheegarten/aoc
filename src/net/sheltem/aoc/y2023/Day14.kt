package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.Direction
import net.sheltem.aoc.common.Direction.*
import net.sheltem.aoc.common.move
import java.time.Duration
import java.time.Instant

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
        this[newPosition.second][newPosition.first] = 'O'

        if (newPosition != j to i) {
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

//        println()
//        result.forEach { println(it) }
    }

    return result.reversed().mapIndexed { index, line -> index to line.count { char -> char == 'O' } }.sumOf { it.first * it.second }.toLong()
}

private fun List<String>.tilt2(cycles: Int): Long {
    val start = Instant.now()
    val border = "#".repeat(this[0].length + 2)
    val mapWithBorder = listOf(border) + this.map { "#$it#" } + listOf(border)

    val result = mapWithBorder.map { it.toCharArray() }

    val memory = mutableMapOf<String, Int>()

    var cycle = 1
    while (cycle < cycles) {
        for (direction in listOf(NORTH, WEST, SOUTH, EAST)) {
            when (direction) {
                NORTH -> {
                    for (i in mapWithBorder.indices.drop(1)) {
                        for (j in mapWithBorder[0].indices) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                WEST -> {
                    for (i in mapWithBorder.indices.drop(1)) {
                        for (j in mapWithBorder[0].indices) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                SOUTH -> {
                    for (i in mapWithBorder.indices.reversed().drop(1)) {
                        for (j in mapWithBorder[0].indices) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                EAST -> {
                    for (i in mapWithBorder.indices.drop(1)) {
                        for (j in mapWithBorder[0].indices.reversed()) {
                            result.tilt(direction, i, j)
                        }
                    }
                }

                else -> continue
            }
        }
        val current = result.joinToString("") { it.joinToString("") }
        if (!memory.contains(current)) {
            memory[current] = cycle
        } else {
            println("Repeated cycle found => ${memory[current]} to $cycle")
            break
        }
        if (cycle.mod(1000000) == 0) {
            println("Cycle $cycle after ${Duration.between(start, Instant.now()).toSeconds()}s ...")
        }
        cycle++
    }


    return result.reversed().mapIndexed { index, line -> index to line.count { char -> char == 'O' } }.sumOf { it.first * it.second }.toLong()
}
