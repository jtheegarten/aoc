package net.sheltem.aoc.y2024

import net.sheltem.common.*
import kotlin.math.pow

suspend fun main() {
    Day17().run()
}

class Day17 : Day<String>("5,7,3,0", "117440") {

    override suspend fun part1(input: List<String>): String = input.parse().run().joinToString(",")

    override suspend fun part2(input: List<String>): String = input.parse().second.let { matchOutput(it, it) }.toString()

    private fun List<String>.parse() =
        this[0].regexNumbers().single() to this.last().regexNumbers()

    private fun matchOutput(program: List<Long>, goal: List<Long>): Long =
        (if (goal.size == 1) 0 else 8 * matchOutput(program, goal.subList(1, goal.size)))
            .let { regAStart ->
                generateSequence(regAStart) { it + 1 }
                    .first { (it to program).run() == goal }
            }

    private fun Pair<Long, List<Long>>.run(): MutableList<Long> {
        var regA = first
        var regB = 0L
        var regC = 0L

        val output = mutableListOf<Long>()
        var pointer = 0
        while (pointer >= 0 && pointer < second.size - 1) {
            val value = second[pointer + 1]
            when (second[pointer]) {
                0L -> {
                    regA = (regA / 2.0.pow(combo(value, regA, regB, regC).toDouble())).toLong()
                }

                1L -> {
                    regB = regB.xor(value)
                }

                2L -> {
                    regB = combo(value, regA, regB, regC) % 8
                }

                3L -> {
                    if (regA != 0L) {
                        pointer = value.toInt()
                        continue
                    }
                }

                4L -> {
                    regB = regB.xor(regC)
                }

                5L -> {
                    output.add(combo(value, regA, regB, regC) % 8)
                }

                6L -> {
                    regB = (regA / 2.0.pow(combo(value, regA, regB, regC).toDouble())).toLong()
                }

                7L -> {
                    regC = (regA / 2.0.pow(combo(value, regA, regB, regC).toDouble())).toLong()
                }
            }
            pointer += 2
        }
        return output
    }

    private fun combo(operand: Long, regA: Long, regB: Long, regC: Long): Long =
        when {
            operand <= 3 -> operand
            operand == 4L -> regA
            operand == 5L -> regB
            operand == 6L -> regC
            else -> -1
        }
}
