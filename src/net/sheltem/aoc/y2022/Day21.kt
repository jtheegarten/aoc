package net.sheltem.aoc.y2022


import net.sheltem.aoc.common.MathOperation
import net.sheltem.aoc.common.MathOperation.ADD
import net.sheltem.aoc.common.MathOperation.Companion.fromSign
import net.sheltem.aoc.common.MathOperation.DIVIDE
import net.sheltem.aoc.common.MathOperation.MULTIPLY
import net.sheltem.aoc.common.MathOperation.SUBSTRACT
import kotlin.math.absoluteValue

suspend fun main() {
    Day21().run()
}

class Day21 : Day<Long>(152, 301) {

    override suspend fun part1(input: List<String>): Long = input.toMonkey().value.toLong()

    override suspend fun part2(input: List<String>): Long = input.humanNumber()
}


private fun List<String>.toMonkey(): MonkeyNode = this
    .map { it.split(": ") }
    .associate { it.first() to it[1] }
    .let { MathMonkey.from(it["root"]!!, it) }

private fun List<String>.humanNumber(): Long {
    val inputMap = this.map { it.split(": ") }.associate { it.first() to it[1] }
    var max = Long.MAX_VALUE
    var min = 1L

    while (true) {
        val halfDiff = max - (max - min) / 2

//        println("$min | $halfDiff | $max")

        val tests = listOf(min, halfDiff, max)
            .map { checkValue ->
                val checkMap = inputMap.toMutableMap()
                checkMap["humn"] = checkValue.toString()
                (MathMonkey.from(checkMap["root"]!!, checkMap) as MathMonkey)
                    .let { checkValue to (it.left diff it.right) }

            }.sortedBy { it.second }.take(2)

        val result = tests.firstOrNull { it.second == 0.0 }
        if (result != null) {
            return result.first
        } else {
            min = tests.minOf { it.first }
            max = tests.maxOf { it.first }
        }
    }

}

private data class MathMonkey(val op: MathOperation, val left: MonkeyNode, val right: MonkeyNode) : MonkeyNode {

    override val value = op.execute(left, right)

    companion object {
        fun from(instruction: String, instructionsMap: Map<String, String>): MonkeyNode =
            if (instruction.toLongOrNull() != null) ValueMonkey(instruction.toDouble())
            else instruction.split(" ")
                .let { MathMonkey(fromSign(it[1]), from(instructionsMap[it[0]]!!, instructionsMap), from(instructionsMap[it[2]]!!, instructionsMap)) }
    }
}

private data class ValueMonkey(override val value: Double) : MonkeyNode

private interface MonkeyNode {
    val value: Double

    infix operator fun plus(other: MonkeyNode) = value + other.value
    infix operator fun minus(other: MonkeyNode) = value - other.value
    infix operator fun div(other: MonkeyNode) = value / other.value
    infix operator fun times(other: MonkeyNode) = value * other.value
    infix fun diff(other: MonkeyNode) = (value - other.value).absoluteValue
}

private fun MathOperation.execute(left: MonkeyNode, right: MonkeyNode) =
    when (this) {
        ADD -> left + right
        SUBSTRACT -> left - right
        MULTIPLY -> left * right
        DIVIDE -> left / right
    }
