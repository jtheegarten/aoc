package net.sheltem.aoc.y2015

import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.y2015.Action.*
import java.lang.Integer.max

fun main() {
    Day06().run()
}

class Day06 : Day<Int>(998996, 1001996) {
    override fun part1(input: List<String>): Int = buildLightMap().followInstructions(input.map { it.toInstruction() }).values.count { it }

    override fun part2(input: List<String>): Int = buildIntLightMap().followIntInstructions(input.map { it.toInstruction() }).values.sum()
}


private fun String.toInstruction(): Instruction {
    val (minX, minY, maxX, maxY) = Regex("\\d+").findAll(this).toList().map { it.value.toInt() }
    return when {
        startsWith("turn on") -> Instruction(minX to minY, maxX to maxY, ON)
        startsWith("turn off") -> Instruction(minX to minY, maxX to maxY, OFF)
        startsWith("toggle") -> Instruction(minX to minY, maxX to maxY, TOGGLE)
        else -> error("Unknown input")
    }
}

private fun MutableMap<PositionInt, Boolean>.followInstructions(instructions: List<Instruction>) = apply {
    instructions.forEach {
        for (x in it.start.first..it.end.first) {
            for (y in it.start.second..it.end.second) {
                this.compute(x to y) { _, old -> old!!.applyAction(it.action) }
            }
        }
    }
}

private fun MutableMap<PositionInt, Int>.followIntInstructions(instructions: List<Instruction>) = apply {
    instructions.forEach {inst ->
        for (x in inst.start.first..inst.end.first) {
            for (y in inst.start.second..inst.end.second) {
                this.compute(x to y) { _, old -> old!!.applyAction(inst.action) }
            }
        }
    }
}

private fun buildIntLightMap(): MutableMap<PositionInt, Int> =
    buildMap {
        for (x in 0..999) {
            for (y in 0..999) {
                put(x to y, 0)
            }
        }
    }.toMutableMap()


private fun buildLightMap(): MutableMap<PositionInt, Boolean> =
    buildMap {
        for (x in 0..999) {
            for (y in 0..999) {
                put(x to y, false)
            }
        }
    }.toMutableMap()

private class Instruction(val start: PositionInt, val end: PositionInt, val action: Action)

private fun Boolean.applyAction(action: Action) =
    when (action) {
        ON -> true
        OFF -> false
        TOGGLE -> !this
    }

private fun Int.applyAction(action: Action) =
    when (action) {
        ON -> this + 1
        OFF -> max(this - 1, 0)
        TOGGLE -> this + 2
    }

private enum class Action {
    ON,
    OFF,
    TOGGLE;
}
