package net.sheltem.aoc.y2015

suspend fun main() {
    Day08().run()
}

class Day08 : Day<Int>(12, 19) {
    override suspend fun part1(input: List<String>): Int = input.fold(0) { acc, line ->
        val mem = line.replace("\\\\", "?").replace("\\\"", "?").replace("""\\x..""".toRegex(), "?")
        acc + line.length - mem.substring(1, mem.length - 1).length
    }

    override suspend fun part2(input: List<String>) = input.fold(0) { acc, line ->
        val mem = '"' + line.replace("\\", "\\\\").replace("\"", "\\\"") + '"'
        acc + mem.length - line.length
    }
}
