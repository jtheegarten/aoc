package net.sheltem.aoc.y2024

import net.sheltem.common.regexNumbers
import net.sheltem.common.splitInHalf

suspend fun main() {
    Day11().run()
}

class Day11 : Day<Long>(55312, 65601038650482) {

    override suspend fun part1(input: List<String>): Long = input.single().regexNumbers().sumOf { it.blink(25) }

    override suspend fun part2(input: List<String>): Long = input.single().regexNumbers().sumOf { it.blink(75) }

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()

    private fun Long.blink(times: Int): Long =
        when {
            cache.contains(this to times) -> cache[this to times]!!
            times == 0 -> 1L.also { cache[this to 0] = it }
            this == 0L -> 1L.blink(times - 1)
                .also { cache[0L to times] = it }

            this.toString().length % 2 == 0 -> this.toString()
                .splitInHalf()
                .map { it.toLong() }
                .sumOf { it.blink(times - 1) }.also { cache[this to times] = it }

            else -> (this * 2024).blink(times - 1)
        }
}
