package net.sheltem.aoc.y2022

import net.sheltem.aoc.common.Day

fun main() {
    Day01().run()
}

class Day01 : Day<Int>(24000, 45000) {

    private fun generateElvesList(input: String): List<Int> =
        input
            .split("\n\n")
            .map { elf ->
                elf.lines().sumOf { it.toIntOrNull() ?: 0 }
            }

    override fun part1(input: List<String>): Int {
        return generateElvesList(input.joinToString("\n")).max()
    }

    override fun part2(input: List<String>): Int {
        return generateElvesList(input.joinToString("\n")).sortedDescending().take(3).sum()
    }
}
