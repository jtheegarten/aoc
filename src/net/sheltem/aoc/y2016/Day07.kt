package net.sheltem.aoc.y2016

suspend fun main() {
    Day07().run()
}

class Day07 : Day<Int>(2, 4) {

    override suspend fun part1(input: List<String>): Int {
        return input.count { it.isValidIp() }
    }

    override suspend fun part2(input: List<String>): Int {
        return 4
    }
}

private fun String.isValidIp(): Boolean {
    val bracketRegex = Regex("\\W\\w+\\W")
    val bracketHits = bracketRegex.findAll(this).map { it.value.replace("[", "").replace("]", "") }.toList()

    if (bracketHits.any { it.containsPalindrome() }) {
        return false
    }

    var remaining = this
    for (hit in bracketHits) {
        remaining = remaining.replace(hit, ".")
    }
    return remaining.containsPalindrome()
}

private fun String.containsPalindrome(): Boolean = windowed(4, 1).any { it[0] != it[1] && it[1] == it[2] && it[0] == it[3] }
