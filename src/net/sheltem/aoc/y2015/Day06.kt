package net.sheltem.aoc.y2015

import net.sheltem.common.PositionInt
import net.sheltem.aoc.y2015.Action.OFF
import net.sheltem.aoc.y2015.Action.ON
import net.sheltem.aoc.y2015.Action.TOGGLE
import java.lang.Integer.max

suspend fun main() {
    Day06().run()
}

class Day06 : Day<Int>(998996, 1001996) {
    override suspend fun part1(input: List<String>): Int = buildLightMap().followInstructions(input.map { it.toInstruction() }).sumOf { row -> row.count { it } }

    override suspend fun part2(input: List<String>): Int = buildIntLightMap().followIntInstructions(input.map { it.toInstruction() }).sumOf { it.sum() }
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

private fun Array<BooleanArray>.followInstructions(instructions: List<Instruction>) = apply {
    instructions.forEach {
        for (x in it.start.first..it.end.first) {
            for (y in it.start.second..it.end.second) {
                this[y][x] = this[y][x].applyAction(it.action)
            }
        }
    }
}

private fun Array<IntArray>.followIntInstructions(instructions: List<Instruction>) = apply {
    instructions.forEach {inst ->
        for (x in inst.start.first..inst.end.first) {
            for (y in inst.start.second..inst.end.second) {
                this[y][x] = this[y][x].applyAction(inst.action)
            }
        }
    }
}

private fun buildIntLightMap(): Array<IntArray> = Array(1000) { IntArray(1000) { 0 } }


private fun buildLightMap(): Array<BooleanArray> = Array(1000) { BooleanArray(1000) { false } }

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
