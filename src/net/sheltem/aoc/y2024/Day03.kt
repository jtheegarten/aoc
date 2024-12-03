package net.sheltem.aoc.y2024

import net.sheltem.common.regex
import net.sheltem.common.regexNumbers

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(161, 48) {

    override suspend fun part1(input: List<String>): Long = input.joinToString().regex(mulRegex).doMults()

    override suspend fun part2(input: List<String>): Long = input.joinToString().replace(cutRegex, "").regex(mulRegex).doMults()
}

private val mulRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
private val cutRegex = Regex("don't\\(\\).*?do\\(\\)")

private fun List<String>.doMults(): Long = this.map { it.regexNumbers() }.sumOf { (first, second) -> first * second }
