package net.sheltem.aoc.y2024

import net.sheltem.common.regexNumbers
import net.sheltem.common.splitInHalf

suspend fun main() {
    Day11().run()
}

class Day11 : Day<Long>(55312, 65601038650482) {

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()

    override suspend fun part1(input: List<String>): Long = input.also { cache.clear() }.single().regexNumbers().sumOf { it.blink(25) }

    override suspend fun part2(input: List<String>): Long = input.also { cache.clear() }.single().regexNumbers().sumOf { it.blink(75) }


    private fun Long.blink(times: Int): Long =
        cache.getOrPut(this to times) {
            when {
                times == 0 -> 1L
                this == 0L -> 1L.blink(times - 1)

                this.toString().length % 2 == 0 -> this.toString()
                    .splitInHalf()
                    .map { it.toLong() }
                    .sumOf { it.blink(times - 1) }

                else -> (this * 2024).blink(times - 1)
            }
        }
}
