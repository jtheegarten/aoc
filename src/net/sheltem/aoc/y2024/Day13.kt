package net.sheltem.aoc.y2024

import net.sheltem.common.Position
import net.sheltem.common.regexNumbers

suspend fun main() {
    Day13().run()
}

class Day13 : Day<Long>(480, 875318608908) {

    override suspend fun part1(input: List<String>): Long = input.toClawMachines().sumOf { it.run() }

    override suspend fun part2(input: List<String>): Long = input.toClawMachines(10_000_000_000_000L).sumOf(ClawMachine::run)

    private data class ClawMachine(val a: Position, val b: Position, val goal: Position) {

        companion object {
            fun from(lines: List<String>, offset: Long = 0L): ClawMachine {
                val a = lines[0].regexNumbers().zipWithNext().first()
                val b = lines[1].regexNumbers().zipWithNext().first()
                val goal = lines[2].regexNumbers().let { (it[0] + offset) to (it[1] + offset) }
                return ClawMachine(a, b, goal)
            }
        }

        // a.x * n + b.x * m == goal.x && a.y * n + b.y * m == goal.y
        fun run(): Long {
            val n = (goal.first * b.second - goal.second * b.first).toDouble() / (a.first * b.second - b.first * a.second)
            val m = (goal.first - a.first * n) / b.first
            return if (n.isNatural() && m.isNatural()) n.toLong() * 3 + m.toLong() else 0
        }

    }

    private fun List<String>.toClawMachines(offset: Long = 0) = this.filter { it.isNotBlank() }.windowed(3, 3).map { ClawMachine.from(it, offset) }
}
private fun Double.isNatural() = this % 1 == 0.0
