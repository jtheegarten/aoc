package net.sheltem.aoc.y2015

fun main() {
    Day01().run()
}

class Day01 : Day<Int>(3, 0) {
    override fun part1(input: List<String>): Int = input.first().let { instructions -> instructions.count { it == '(' } - instructions.count { it == ')' } }

    override fun part2(input: List<String>): Int {
        var position = 0
        val iter = input.first().iterator()
        for (i in input.first().indices) {
            when (input.first()[i]) {
                '(' -> position++
                ')' -> position--

            }
            if (position == -1) return i + 1
        }
        return 0
    }
}
