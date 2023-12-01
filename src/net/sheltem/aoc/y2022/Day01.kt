package net.sheltem.aoc.y2022

suspend fun main() {
    Day01().run()
}

class Day01 : Day<Int>(24000, 45000) {

    private fun generateElvesList(input: String): List<Int> =
        input
            .split("\n\n")
            .map { elf ->
                elf.lines().sumOf { it.toIntOrNull() ?: 0 }
            }

    override suspend fun part1(input: List<String>): Int {
        return generateElvesList(input.joinToString("\n")).max()
    }

    override suspend fun part2(input: List<String>): Int {
        return generateElvesList(input.joinToString("\n")).sortedDescending().take(3).sum()
    }
}
