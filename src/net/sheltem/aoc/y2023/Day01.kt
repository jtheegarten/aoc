package net.sheltem.aoc.y2023

suspend fun main() {
    Day01().run()
}
private val digits = ('1'..'9').map { it.toString() }.zip(1..9).toMap()
private val allDigits = digits + listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").zip(1..9).toMap()

class Day01 : Day<Int>(142, 281) {

    override suspend fun part1(input: List<String>): Int {
        return input.sumOf { it.firstAndLastDigit() }
    }

    override suspend fun part2(input: List<String>): Int {
        return input.sumOf { it.firstAndLastDigit(allDigits) }
    }
}

private fun String.firstAndLastDigit(digitsMap: Map<String, Int> = digits): Int {
    val first = digitsMap
        .entries
        .map { e -> this.indexOf(e.key) to e.value }
        .filter { it.first != -1 }
        .minBy { it.first }.second.toString()

    val last = digitsMap
        .entries
        .map { e -> this.lastIndexOf(e.key) to e.value }
        .filter { it.first != -1 }
        .maxBy { it.first }.second.toString()

    return "$first$last".toInt()
}
