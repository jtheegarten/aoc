package net.sheltem.aoc.y2022

import net.sheltem.aoc.common.Day
import net.sheltem.aoc.common.lastAsInt

class Day10 : Day<Int>(13140, 0) {
    override fun part1(input: List<String>): Int = input.filter { it.isNotBlank() }.toSignalStrengths().sumRegisters(listOf(20, 60, 100, 140, 180, 220))

    override fun part2(input: List<String>): Int = input.filter { it.isNotBlank() }.toSignalStrengths().printCRT()

}

private fun List<Int>.printCRT(): Int {
    asSequence().mapIndexed { index, value ->
        if (index % 40 in ((value)..value + 2)) "\u2588" else " "
    }.drop(1).windowed(40, 40).forEach { it.joinToString("").let(::println) }
    return 0
}

private fun List<Int>.sumRegisters(targets: List<Int>): Int {
    var result = 0
    for (target in targets) {
        result += this[target] * target
    }
    return result
}

fun main() {
    Day10().run()
}

private fun List<String>.toSignalStrengths(): MutableList<Int> {
    val register = mutableListOf<Int>()
    register.add(1)
    var wasNoop = true
    for (instruction in this) {
        val curVal = register.last()
        when {
            instruction == "noop" -> {
                if (wasNoop) {
                    register.add(curVal)
                }
                wasNoop = true
            }

            instruction.startsWith("addx") -> {
                if (wasNoop) {
                    register.add(curVal)
                }
                register.add(curVal)
                val addition = instruction.lastAsInt(" ")
                register.add(curVal + addition)
                wasNoop = false
            }
        }
    }
    return register
}
