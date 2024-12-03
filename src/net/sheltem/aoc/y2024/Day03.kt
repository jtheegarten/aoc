package net.sheltem.aoc.y2024

import net.sheltem.common.regex
import net.sheltem.common.regexNumbers

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(161, 48) {

    override suspend fun part1(input: List<String>): Long = input.joinToString().regex(mulRegex).doMults()

    override suspend fun part2(input: List<String>): Long = input.joinToString().cutDonts().regex(mulRegex).doMults()
}

private val mulRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
private val startRegex = Regex(".*?don't\\(\\)")
private val doRegex = Regex("(do\\(\\).*?don't\\(\\))|do\\(\\).*")

private fun String.cutDonts(): String {
    val start = startRegex.find(this)!!.value
    val rest = doRegex.findAll(this.drop(start.length)).map { it.value }.onEach { println(it) }

    return start.plus(rest.joinToString())
}

private fun List<String>.doMults(): Long = this.map { it.regexNumbers() }.sumOf { (first, second) -> first * second }
