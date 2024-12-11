package net.sheltem.aoc.y2024

import net.sheltem.common.regexNumbers

suspend fun main() {
    Day11().run()
}

class Day11 : Day<Long>(55312, 65601038650482) {

    override suspend fun part1(input: List<String>): Long = input.single().regexNumbers().blink(25)

    override suspend fun part2(input: List<String>): Long = input.single().regexNumbers().blink(75)

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()

    private fun String.splitInHalf(): List<String> =
        (length / 2).let { mid -> listOf(substring(0, mid), substring(mid)) }

    private fun List<Long>.blink(times: Int): Long = this.sumOf {
        it.blink(times)
    }

    private tailrec fun Long.blink(times: Int): Long = when {
        cache.contains(this to times) -> cache[this to times]!!
        times == 0 -> {
            cache[this to 0] = 1L
            1L
        }

        this == 0L -> {
            1L.blink(times - 1)
                .also { cache[0L to times] = it }
        }

        this.toString().length % 2 == 0 -> {
            val (left, right) = this.toString().splitInHalf().map { it.toLong() }
            (left.blink(times - 1) + right.blink(times - 1)).also { cache[this to times] = it }
        }

        else -> {
            (this * 2024).blink(times - 1)
        }
    }
}
