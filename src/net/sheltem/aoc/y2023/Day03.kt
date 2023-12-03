package net.sheltem.aoc.y2023

import kotlin.math.max
import kotlin.math.min

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(4361, 467835) {

    override suspend fun part1(input: List<String>): Long {
        return input.toEngineNumbers().flatten().sum()
    }

    override suspend fun part2(input: List<String>): Long {
        return input.toGearRatios().sum()
    }
}

private val numberRegex = Regex("\\d+")

private fun List<String>.toEngineNumbers(): List<List<Long>> = this
    .mapIndexed { index, line ->
        val numberMatches = numberRegex.findAll(line)
        numberMatches.filter { match ->
            val startIndex = max(match.range.first - 1, 0)
            val endIndex = min(match.range.last + 1, line.length - 1)
//            println("${match.range} -> $startIndex - $endIndex")
            var valid = false
            val previousRow = this[max(index - 1, 0)].substring(startIndex, endIndex + 1)
            val nextRow = this[min(index + 1, this.size - 1)].substring(startIndex, endIndex + 1)
//            println("$previousRow && $nextRow")
            if (this[index][startIndex].isSymbol()) valid = true
            if (this[index][endIndex].isSymbol()) valid = true
            if (previousRow.any { it.isSymbol() }) valid = true
            if (nextRow.any { it.isSymbol() }) valid = true

            valid
        }.map { it.value.toLong() }
            .toList()//.also { println("$line -> $it") }
    }

private fun Char.isSymbol() = !isDigit() && this != '.'

private fun List<String>.toGearRatios(): List<Long> = this
    .mapIndexed { index, line ->
        val gearSymbolIndexes = Regex("\\*").findAll(line).map { it.range.first }
//        println(gearSymbolIndexes.toList())
        gearSymbolIndexes.mapNotNull { gearIndex ->
            val adjacentNumbers = this.findAdjacentNumbers(index, gearIndex)
            if (adjacentNumbers.size == 2) {
                adjacentNumbers.component1() * adjacentNumbers.component2()
            } else {
                null
            }
        }.toList()
    }.flatten()

private fun List<String>.findAdjacentNumbers(index: Int, gearIndex: Int): List<Long> {
    return (numberRegex.findAll(this[index - 1]).filter { gearIndex in (it.range.first - 1)..(it.range.last + 1) } + numberRegex.findAll(this[index])
        .filter { gearIndex in (it.range.first - 1)..(it.range.last + 1) } + numberRegex.findAll(this[index + 1])
        .filter { gearIndex in (it.range.first - 1)..(it.range.last + 1) }).toList().map { it.value.toLong() }
}
