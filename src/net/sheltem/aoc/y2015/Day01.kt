package net.sheltem.aoc.y2015

suspend fun main() {
    Day01().run()
}

class Day01 : Day<Int>(3, 0) {
    override suspend fun part1(input: List<String>): Int = input.first().let { instructions -> instructions.count { it == '(' } - instructions.count { it == ')' } }

    override suspend fun part2(input: List<String>): Int {
        var position = 0
        input.first().forEachIndexed { index, c ->
            when (c) {
                '(' -> position++
                ')' -> position--

            }
            if (position == -1) return index + 1
        }
        return 0
    }
}
