package net.sheltem.aoc.y2022

import java.util.ArrayDeque
import java.util.Deque

class Day05 : Day<String>("CMZ", "MCD") {

    private fun addLine(result: MutableMap<Int, Deque<String>>, line: String) {
        var position = 1
        while (position < line.length) {
            val char = line[position].toString()
            if (char.isNotBlank()) result[(position + 3) / 4]!!.push(char)
            position += 4
        }
    }

    private fun parseStacks(stacksDescription: String): Map<Int, Deque<String>> {
        val lines = stacksDescription.lines().reversed()
        val result = mutableMapOf<Int, Deque<String>>()
        for (i in 1..(lines.first().last().digitToInt())) {
            result[i] = ArrayDeque()
        }
        for (i in 1 until lines.size) {
            addLine(result, lines[i])
        }

        return result
    }

    private fun applyMove(move: String, stacks: Map<Int, Deque<String>>, stableOrder: Boolean = false) {
        val (amount, from, to) = move.split(" ").mapNotNull { it.toIntOrNull() }
        if (!stableOrder) {
            repeat(amount) { stacks[to]!!.push(stacks[from]!!.pop()) }
        } else {
            List(amount) { stacks[from]!!.pop() }.reversed().forEach { stacks[to]!!.push(it) }
        }
    }

    private fun solve(input: List<String>, stableOrder: Boolean = false): String {
        val (stacksDescription, instructionsDescription) = input.joinToString("\n").split("\n\n")
        val stacks = parseStacks(stacksDescription)
        instructionsDescription.lines().filter { it.isNotEmpty() }.forEach { applyMove(it, stacks, stableOrder) }

        return stacks.values.joinToString("") { it.pop() }
    }

    override fun part1(input: List<String>): String = solve(input)

    override fun part2(input: List<String>): String = solve(input, true)

}

fun main() {
    Day05().run()
}
