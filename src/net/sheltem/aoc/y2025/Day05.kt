package net.sheltem.aoc.y2025

import kotlin.math.max
import kotlin.math.min

suspend fun main() {
    Day05().run()
}

class Day05 : Day<Long>(3, 14) {

    override suspend fun part1(input: List<String>): Long = input
        .parse()
        .let { (db, numbers) ->
            numbers.count { it in db }.toLong()
        }

    override suspend fun part2(input: List<String>): Long = input
        .parse()
        .first
        .ranges
        .sumOf { it.last - (it.first - 1) }

    private fun List<String>.parse(): Pair<Database, List<Long>> {
        val db = Database()
        val (ranges, numbers) = this.partition { it.contains("-") }
        ranges.map { it.toRange() }.sortedBy { it.first }.forEach { db.add(it) }

        return db to numbers.mapNotNull { it.toLongOrNull() }
    }

    private class Database() {
        val ranges = mutableSetOf<LongRange>()

        operator fun contains(number: Long): Boolean = ranges.any { it.contains(number) }

        fun add(range: LongRange) {
            when {
                ranges.any { range.first in it } -> {
                    val other = ranges.first { range.first in it }
                    ranges.remove(other)
                    ranges.add(other.first..max(range.last, other.last))
                }

                ranges.any { range.last in it } -> {
                    val other = ranges.first { range.last in it }
                    ranges.remove(other)
                    ranges.add(min(range.first, other.first)..other.last)
                }

                else -> ranges.add(range)
            }
        }
    }

    private fun String.toRange(): LongRange = split("-").let { (start, end) -> start.toLong()..end.toLong() }
}
