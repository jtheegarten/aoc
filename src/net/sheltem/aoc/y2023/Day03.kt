package net.sheltem.aoc.y2023

import kotlin.math.max
import kotlin.math.min

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(4361, 467835) {

    override suspend fun part1(input: List<String>): Long = input
        .toEngineNumbers()
        .sum()

    override suspend fun part2(input: List<String>): Long = input
        .toGearRatios()
        .sum()
}

private val numberRegex = Regex("\\d+")

private fun List<String>.toEngineNumbers(): List<Long> = this
    .mapIndexed { index, line ->
        numberRegex
            .findAll(line)
            .filter { match ->
                val startIndex = max(match.range.first - 1, 0)
                val endIndex = min(match.range.last + 2, line.length - 1)
                val previousRow = this[max(index - 1, 0)].substring(startIndex, endIndex)
                val thisRow = this[index].substring(startIndex, endIndex)
                val nextRow = this[min(index + 1, this.size - 1)].substring(startIndex, endIndex)

                (previousRow + thisRow + nextRow).any { it.isSymbol() }

            }.map { it.value.toLong() }
            .toList()
    }.flatten()

private fun Char.isSymbol() = !isDigit() && this != '.'

private fun List<String>.toGearRatios(): List<Long> = this
    .mapIndexed { index, line ->
        Regex("\\*")
            .findAll(line)
            .map { it.range.first }
            .mapNotNull { gearIndex ->
                val adjacentNumbers = this.findAdjacentNumbers(index, gearIndex)
                if (adjacentNumbers.size == 2) {
                    adjacentNumbers.component1() * adjacentNumbers.component2()
                } else {
                    null
                }
            }.toList()
    }.flatten()

private fun List<String>.findAdjacentNumbers(index: Int, gearIndex: Int): List<Long> =
    (numberRegex.findAll(this[index - 1]).toList()
            + numberRegex.findAll(this[index]).toList()
            + numberRegex.findAll(this[index + 1]).toList())
        .filter { gearIndex in it.numberRange() }
        .map { it.value.toLong() }

private fun MatchResult.numberRange() = (range.first - 1)..(range.last + 1)
