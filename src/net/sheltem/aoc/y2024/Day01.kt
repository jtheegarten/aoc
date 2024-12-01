package net.sheltem.aoc.y2024

import net.sheltem.common.regexNumbers
import kotlin.math.absoluteValue

suspend fun main() {
    Day01().run()
}

class Day01 : Day<Long>(11, 31) {

    override suspend fun part1(input: List<String>): Long = input.toDistances().sum()

    override suspend fun part2(input: List<String>): Long = input.toSimilaritiesSum()
}

private fun List<String>.toDistances() = this.toSortedPairs().map { (it.first - it.second).absoluteValue }

private fun List<String>.toSimilaritiesSum() = this.toNumberPairs().unzip().let { (left, right) ->
    left.fold(0L) { acc, lNumber ->
        acc + (lNumber * right.count { it == lNumber })
    }
}

private fun List<String>.toSortedPairs(): List<Pair<Long, Long>> = this.toNumberPairs().unzip().let {
    it.first.sorted().zip(it.second.sorted())
}

private fun List<String>.toNumberPairs() = map { it.regexNumbers() }.map { it.first() to it.last() }
