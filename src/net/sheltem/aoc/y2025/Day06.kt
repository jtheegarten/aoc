package net.sheltem.aoc.y2025

import net.sheltem.common.multiply

suspend fun main() {
    Day06().run()
}

class Day06 : Day<Long>(4277556, 3263827) {

    override suspend fun part1(input: List<String>): Long = input
        .parse()
        .sumOf { it.result() }

    override suspend fun part2(input: List<String>): Long = input
        .parse(true)
        .sumOf { it.result() }

    private class ProblemSet(val numbers: List<Long>, val operation: Boolean) {
        fun result() = if (operation) numbers.sum() else numbers.multiply()
    }

    private fun List<String>.parse(part2: Boolean = false): List<ProblemSet> =
        if (!part2) {
            map { it.trim() }
                .filterNot { it.isEmpty() }
                .map { it.split(Regex("\\s+")) }
                .let { splitLines ->
                    (0..<splitLines[0].size).map { i ->
                        val problem = splitLines.map { it[i] }
                        val numbers = problem.dropLast(1).map { it.toLong() }
                        ProblemSet(numbers, problem.last() == "+")
                    }
                }
        } else {
            filterNot { it.isEmpty() }
                .let { it.dropLast(1) to it.last().split(Regex("\\s+")).toMutableList() }
                .let { (lines, ops) ->
                    val problems = mutableListOf<ProblemSet>()
                    var cur = mutableListOf<Long>()

                    for (i in lines.maxOf { it.length } - 1 downTo -1) {
                        val cols = lines.mapNotNull { it.getOrNull(i) }

                        if (cols.all { it == ' ' }) {
                            if (cur.isNotEmpty()) {
                                problems.add(ProblemSet(cur, ops.dropLast(problems.size).last().trim() == "+"))
                                cur = mutableListOf()
                            }
                        } else {
                            val num = cols.filter { it.isDigit() }.joinToString("").toLong()
                            cur.add(num)
                        }
                    }
                    return problems
                }
        }
}
