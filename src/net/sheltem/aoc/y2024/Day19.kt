package net.sheltem.aoc.y2024

suspend fun main() {
    Day19().run()
}

class Day19 : Day<Long>(6, 16) {

    override suspend fun part1(input: List<String>): Long = input
        .also { cache.clear() }
        .parse()
        .let { (supply, patterns) -> patterns.count { supply.validate(it) >= 1L } }
        .toLong()

    override suspend fun part2(input: List<String>): Long = input
        .also { cache.clear() }
        .parse()
        .let { (supply, patterns) -> patterns.sumOf { supply.validate(it) } }

    private val cache = HashMap<String, Long>()

    private fun List<String>.validate(pattern: String): Long {
        return when {
            pattern.isEmpty() -> 1L
            cache.contains(pattern) -> cache[pattern]!!
            else -> this.sumOf {
                if (pattern.startsWith(it))
                    this.validate(pattern.drop(it.length))
                else 0L
            }.also { cache[pattern] = it }
        }
    }

    private fun List<String>.parse() = let { this[0].split(", ") to this.drop(2) }
}
