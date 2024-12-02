package net.sheltem.aoc.y2024

import net.sheltem.common.regexNumbers

suspend fun main() {
    Day02().run()
}

class Day02 : Day<Long>(2, 4) {

    override suspend fun part1(input: List<String>): Long = input.toReports().count { it.isSafe() }.toLong()

    override suspend fun part2(input: List<String>): Long = input.toReports().count { it.isSafe() || it.isSafeWithDampener() }.toLong()
}

private fun List<String>.toReports() = map { it.regexNumbers() }

private fun List<Long>.isSafeWithDampener(): Boolean =
    indices
        .map { index ->
            this.toMutableList().also { it.removeAt(index) }
        }.any { it.isSafe() }

private fun List<Long>.isSafe() =
    zipWithNext()
        .let { pairs ->
            pairs.all { (left, right) ->
                left > right && ((left - right) in 1..3)
            } ||
                    pairs.all { (left, right) ->
                        right > left && ((right - left) in 1..3)
                    }
        }
