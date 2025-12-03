package net.sheltem.aoc.y2025

import kotlin.collections.component1

suspend fun main() {
    Day02().run()
}

class Day02 : Day<Long>(1227775554, 4174379265) {

    override suspend fun part1(input: List<String>): Long = input[0]
        .parseInput()
        .flatMap { it.toInvalidIds() }
        .sum()


    override suspend fun part2(input: List<String>): Long = input[0]
        .parseInput()
        .flatMap { it.toInvalidIdsRegex() }
        .sum()


    private fun LongRange.toInvalidIds(): List<Long> = this
        .map { it.toString() }
        .filter { id ->
            id.length % 2 == 0
                    && id.take(id.length / 2) == id.substring(id.length / 2)
        }.map { it.toLong() }

    private fun LongRange.toInvalidIdsRegex(): List<Long> = this
        .filter { id -> id.toString().matches( Regex("""^(\d+)\1+""")) }

    private fun String.parseInput() = split(",")
        .map { it.split("-") }
        .map { (left, right) ->
            left.toLong()..right.toLong()
        }
}
