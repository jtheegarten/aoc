package net.sheltem.aoc.y2015

import net.sheltem.aoc.y2015.LogicOperation.*

fun main() {
    Day07().run()
}

class Day07 : Day<Int>(72, 0) {
    override fun part1(input: List<String>): Int = solve(input.parseInput())


    override fun part2(input: List<String>): Int = TODO()


    private fun solve(input: Pair<MutableMap<String, UShort>, MutableMap<String, LogicInstruction>>): Int {
        val valuesMap = input.first
        val instructionsMap = input.second

        while (!valuesMap.containsKey("a")) {
            for (instruction in instructionsMap) {
                val operandValues = instruction.value.operands.map { it to valuesMap[it] }
                if (operandValues.any { it.second == null }) continue

                val newValue = when (instruction.value.op) {
                    AND -> operandValues[0].second!! and operandValues[1].second!!
                    OR -> operandValues[0].second!! or operandValues[1].second!!
                    NOT -> operandValues[0].second!!.inv()
                    LSHIFT -> operandValues[0].second!! shl (operandValues[1].second!!)
                    RSHIFT -> operandValues[0].second!! shr (operandValues[1].second!!)
                }
                valuesMap[instruction.key] = newValue
            }

            if (instructionsMap.isEmpty()) break
        }

        return valuesMap["a"]!!.toInt()
    }
}

private infix fun UShort.shl(steps: UShort) = this.toLong().shl(steps.toInt()).toUShort()
private infix fun UShort.shr(steps: UShort) = this.toLong().shr(steps.toInt()).toUShort()

private fun List<String>.parseInput(): Pair<MutableMap<String, UShort>, MutableMap<String, LogicInstruction>> {
    val valuesMap = mutableMapOf<String, UShort>()
    val instructionsMap = mutableMapOf<String, LogicInstruction>()
    forEach {instruction ->
        println(instruction)
        instruction.split(" -> ").let { (what, where) ->
            when {
                what.toIntOrNull() != null -> valuesMap[where] = what.toUShort()
                what.startsWith("NOT") -> instructionsMap[where] = LogicInstruction(NOT, listOf(what.removePrefix("NOT ")))
                else -> what.split(" ").let { instructionsMap[where] = LogicInstruction(LogicOperation.valueOf(it[1]), listOf(it[0], it[2])) }
            }
        }
    }
    return valuesMap to instructionsMap
}

private class LogicInstruction(val op: LogicOperation, val operands: List<String>)

private enum class LogicOperation {
    AND,
    OR,
    NOT,
    LSHIFT,
    RSHIFT;

}
