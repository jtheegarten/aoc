package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day07().run()
}

class Day07 : Day<Long>(3749, 11387) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.regexNumbers() }
        .sumOf { it.calibrate() }

    override suspend fun part2(input: List<String>): Long = input
        .map { it.regexNumbers() }
        .sumOf { it.calibrate(true) }
}

private fun List<Long>.calibrate(part2: Boolean = false): Long = this.drop(1).combine(this[0], part2)

private fun List<Long>.combine(goal: Long, part2: Boolean): Long = when {
    this.size == 1 && this[0] == goal -> goal
    this.size == 1 && this[0] != goal -> 0
    (listOf(this[0] + this[1]) + this.drop(2)).combine(goal, part2) == goal -> goal
    (listOf(this[0] * this[1]) + this.drop(2)).combine(goal, part2) == goal -> goal
    part2 && (listOf((this[0].toString() + this[1].toString()).toLong()) + this.drop(2)).combine(goal, part2) == goal -> goal
    else -> 0
}
