package net.sheltem.aoc.y2015

suspend fun main() {
    Day10().run()
}

class Day10 : Day<Long>(82350, 1166642) {
    override suspend fun part1(input: List<String>): Long = generateSequence(input[0]) { it.lookAndSay() }.take(41).last().length.toLong()

    override suspend fun part2(input: List<String>): Long = generateSequence(input[0]) { it.lookAndSay() }.take(51).last().length.toLong()
}

private fun String.lookAndSay() = Regex("(\\d)\\1{0,100}").findAll(this).map { it.value.length.toString() + it.value[0] }.joinToString("")
