package net.sheltem.aoc.y2024

import net.sheltem.common.regexNumbers
import kotlin.math.absoluteValue

suspend fun main() {
    Day01().run()
}

class Day01 : Day<Long>(11, 31) {

    override suspend fun part1(input: List<String>): Long = input.toLists()
        .let { (first, second) ->
            first.sorted().zip(second.sorted())
        }.sumOf { (it.first - it.second).absoluteValue }

    override suspend fun part2(input: List<String>): Long = input.toLists()
        .let { (left, right) ->
            left.fold(0L) { acc, lNumber ->
                acc + (lNumber * right.count { it == lNumber })
            }
        }
//        .let { (left, right) ->
//            left.sumOf { lNumber -> lNumber * right.count { lNumber == it } }
//        }
}

fun List<String>.toLists() =
    map {
        val (n, m) = it.regexNumbers()
        n to m
    }.unzip()
