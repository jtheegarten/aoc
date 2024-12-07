package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day07().run()
}

class Day07 : Day<Long>(3749, 11387) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.regexNumbers() }
        .sumOfParallel { it.calibrate() }

    override suspend fun part2(input: List<String>): Long = input
        .map { it.regexNumbers() }
        .sumOfParallel { it.calibrate(true) }
}

private fun List<Long>.calibrate(part2: Boolean = false): Long = this
    .drop(2)
    .let {
        if (it.combine(this[1], this[0], part2)) this[0] else 0
    }

private fun List<Long>.combine(current: Long, goal: Long, part2: Boolean): Boolean = when {
    this.isEmpty() -> current == goal
    current > goal -> false
    this.drop(1).combine(current + this[0], goal, part2) -> true
    this.drop(1).combine(current * this[0], goal, part2) -> true
    part2 && this.drop(1).combine((current.toString() + this[0].toString()).toLong(), goal, part2) -> true
    else -> false
}
