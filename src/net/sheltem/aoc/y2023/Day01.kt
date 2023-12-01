package net.sheltem.aoc.y2023

fun main() {
    Day01().run()
}

private val digits = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9,
    "oneight" to 18, "eightwo" to 82,  "nineight" to 98, "twone" to 21, "threeight" to 28, "eighthree" to 83,  "fiveight" to 58, "sevenine" to 79)

class Day01 : Day<Int>(142, 281) {

    override fun part1(input: List<String>): Int {
        return input.sumOf { it.extractDigits() }
    }

    override fun part2(input: List<String>): Int {
        return input.map { it.replaceWrittenNumbers() }.sumOf { it.extractDigits() }
    }
}

fun String.extractDigits(): Int = this.filter { it.isDigit() }.let {
    "${it.first()}${it.last()}"
}.toInt()

fun String.replaceWrittenNumbers(): String {
    var s = this
    var map = s.writtenNumberMap()
    do {
        s = s.replaceFirstWrittenNumber(map)
        map = s.writtenNumberMap()
    } while (map.isNotEmpty())

    return s
}

fun String.writtenNumberMap(): Map<Int, String> = mutableMapOf(
    indexOf("one") to "one",
    indexOf("two") to "two",
    indexOf("three") to "three",
    indexOf("four") to "four",
    indexOf("five") to "five",
    indexOf("six") to "six",
    indexOf("seven") to "seven",
    indexOf("eight") to "eight",
    indexOf("nine") to "nine",
    indexOf("oneight") to "oneight",
    indexOf("eightwo") to "eightwo",
    indexOf("nineight") to "nineight",
    indexOf("twone") to "twone",
    indexOf("threeight") to "threeight",
    indexOf("eighthree") to "eighthree",
    indexOf("fiveight") to "fiveight",
    indexOf("sevenine") to "sevenine",
).also { it.remove(-1) }

fun String.replaceFirstWrittenNumber(map: Map<Int, String>): String {
    val toReplace = map.entries.minByOrNull { it.key }?.value
    return if (toReplace != null) this.replace(toReplace, digits[toReplace].toString())
    else this
}
