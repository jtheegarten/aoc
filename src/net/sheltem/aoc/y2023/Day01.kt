package net.sheltem.aoc.y2023

fun main() {
    Day01().run()
}

private val digits = mapOf("1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9)
private val allDigits = digits + mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)

class Day01 : Day<Int>(142, 281) {

    override fun part1(input: List<String>): Int {
        return input.sumOf { it.firstAndLastDigit() }
    }

    override fun part2(input: List<String>): Int {
        return input.sumOf { it.firstAndLastDigit(allDigits) }
    }
}

fun String.firstAndLastDigit(digitsMap: Map<String, Int> = digits): Int {
    val first = digitsMap.entries.map { e -> this.indexOf(e.key) to e.value }.filter { it.first != -1 }.minBy { it.first }.second.toString()
    val last = digitsMap.entries.map { e -> this.lastIndexOf(e.key) to e.value }.filter { it.first != -1 }.maxBy { it.first }.second.toString()
    return "$first$last".toInt()
}
