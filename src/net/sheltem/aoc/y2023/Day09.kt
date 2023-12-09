package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.regexNumbers

suspend fun main() {
    Day09().run()
}

class Day09 : Day<Long>(114, 2) {

    override suspend fun part1(input: List<String>): Long = input.toChart().sumOf { line ->
        line.extrapolate()
            .sumOf { it.last() }
    }

    override suspend fun part2(input: List<String>): Long = input.toChart().map { it.reversed() }.sumOf { line ->
        line.extrapolate()
            .sumOf { it.last() }
    }
}

private fun List<String>.toChart() = map { line -> line.regexNumbers() }

private fun List<Long>.extrapolate(): List<List<Long>> =
    generateSequence(this) { number ->
        number.zipWithNext().map { it.second - it.first }
    }.takeWhile { line ->
        !line.all { it == 0L }
    }.toList()

//        input.toChart().sumOf { line ->
//        line.extrapolate()
//            .map { it.first() }
//            .reversed()
//            .fold(0L) { acc, number -> number - acc }
