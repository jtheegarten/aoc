package net.sheltem.aoc.y2017

import net.sheltem.aoc.common.numericRegex

suspend fun main() {
    Day02().run()
}

class Day02 : Day<Long>(18, 9) {

    override suspend fun part1(input: List<String>): Long = input.sumOf {
        numericRegex.findAll(it)
            .mapNotNull { number ->
                number.value.toLongOrNull()
            }//.also { print(it.toList()) }
            .let { list ->
                list.max() - list.min()
            }//.also { println(" -> $it") }
    }

    override suspend fun part2(input: List<String>): Long = TODO()
}
