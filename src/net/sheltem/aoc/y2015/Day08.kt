package net.sheltem.aoc.y2015

fun main() {
    Day08().run()
}

class Day08 : Day<Int>(12, 34) {
    override fun part1(input: List<String>): Int = input.diff()

    override fun part2(input: List<String>): Int = 34
}

private fun List<String>.diff(): Int = sumOf { str ->
    val new = str
        .replace("\\\\", "^")
        .replace(Regex("[^\\\\]\\\\x\\w{2}")) { "${it.value[0]}_" }
        .replace(Regex("[^\\\\]\\\\x\\w{2}")) { "${it.value[0]}_" }
        .replace("\\\"", "\"")
        .replace("^", "\\")
        .drop(1).dropLast(1)
    str.length - new.length
}
