package net.sheltem.aoc.y2016


suspend fun main() {
    Day06().run()
}

class Day06 : Day<String>("easter", "advent") {

    override suspend fun part1(input: List<String>): String {
        return input.toWord()
    }

    override suspend fun part2(input: List<String>): String {
        return input.toWord(true)
    }
}

private fun List<String>.toWord(lowest: Boolean = false) = (0 until this[0].length).joinToString("") { index ->
    val charMap = generateCharMap(index)
    if (lowest) {
        charMap.minBy { it.value }
    } else {
        charMap.maxBy { it.value }
    }.key
        .toString()

}

private fun List<String>.generateCharMap(index: Int): Map<Char, Int> = this
    .map { it[index] }
    .groupingBy { it }
    .eachCount()
