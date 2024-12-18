package net.sheltem.aoc.y2017

suspend fun main() {
    Day05().run()
}

class Day05 : Day<Int>(5, 10) {

    override suspend fun part1(input: List<String>): Int = input.map { it.toInt() }.toMutableList().jump()

    override suspend fun part2(input: List<String>): Int = input.map { it.toInt() }.toMutableList().jump(true)

    private fun MutableList<Int>.jump(part2: Boolean = false): Int {

        var steps = 0
        var i = 0
        while (i in this.indices) {
            val offset = this[i]
            steps++
            this[i] = if (part2 && offset >= 3) offset - 1 else offset + 1
            i += offset
        }

        return steps
    }
}
