package net.sheltem.aoc.y2017

suspend fun main() {
    Day01().run()
}

class Day01 : Day<Long>(9, 6) {

    override suspend fun part1(input: List<String>): Long {
        return input[0].filterNumbers().sum()
    }

    override suspend fun part2(input: List<String>): Long {
        return input[0].filterNumbers(false).sum()
    }
}

private fun String.filterNumbers(simple: Boolean = true) = (if (simple) (1) else this.length / 2).let { offset ->
    mapIndexedNotNull { index, char ->
        if (char == this[(index + offset).mod(this.length)]) char.toString().toLong()
        else null
    }
}
