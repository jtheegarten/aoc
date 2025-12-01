package net.sheltem.aoc.y2025

suspend fun main() {
    Day01().run()
}

class Day01 : Day<Int>(3, 6) {

    override suspend fun part1(input: List<String>): Int = input
        .map { Instruction(it[0] == 'L', it.substring(1).toInt()) }
        .countZero(50)


    override suspend fun part2(input: List<String>): Int = input
        .map { Instruction(it[0] == 'L', it.substring(1).toInt()) }
        .countZero(50, true)


    data class Instruction(val left: Boolean, val length: Int) {
        val sign = if (left) -1 else 1
    }

    private fun List<Instruction>.countZero(start: Int, part2: Boolean = false) =
        sequence {
            for (instruction in this@countZero) {
                if (part2) {
                    repeat(instruction.length) { yield(instruction.sign) }
                } else {
                    yield(instruction.sign * instruction.length)
                }
            }
        }.runningFold(start) { acc, clicks -> acc + clicks }
            .count { it % 100 == 0 }
}
