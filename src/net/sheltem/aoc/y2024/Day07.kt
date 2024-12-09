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

private fun List<Long>.calibrate(part2: Boolean = false): Long =
    this
        .drop(1)
        .let {
            if (it.combine(1, this[1], this[0], part2)) this[0] else 0
        }

private fun List<Long>.combine(index: Int, current: Long, goal: Long, part2: Boolean): Boolean =
    when {
        index == size -> current == goal
        current > goal -> false
        combine(index + 1, current + this[index], goal, part2) -> true
        combine(index + 1, current * this[index], goal, part2) -> true
        part2 && combine(index + 1, (current.toString() + this[index].toString()).toLong(), goal, part2) -> true
        else -> false
    }
