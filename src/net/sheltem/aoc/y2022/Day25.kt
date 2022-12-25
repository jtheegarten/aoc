package net.sheltem.aoc.y2022

import kotlin.math.pow

fun main() {
    Day25().run()
}

class Day25 : Day<String>("2=-1=0", "19") {

    override fun part1(input: List<String>): String = input.sumOf { it.toLong() }.toSnafu()

    override fun part2(input: List<String>): String = "19"
}

private fun String.toLong(): Long = this.toCharArray().reversed()
    .mapIndexed { i, char ->
        5.0.pow(i).toLong() * ("=-012".indexOf(char) - 2)
    }.sum()

private fun Long.toSnafu(): String {
    var remainder = this
    var result = ""
    while (remainder > 0) {
        result = "012=-"[(remainder % 5).toInt()] + result
        remainder -= ((remainder + 2) % 5) - 2
        remainder /= 5
    }
    return result
}
