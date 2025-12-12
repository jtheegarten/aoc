package net.sheltem.aoc.y2025

import net.sheltem.common.count
import net.sheltem.common.multiply


suspend fun main() {
    Day12().run()
}

class Day12 : Day<Long>(3, 2) {

    override suspend fun part1(input: List<String>): Long = input
        .parse()
        .naiveFit()


    override suspend fun part2(input: List<String>): Long = 2

    private fun Pair<List<Int>, List<Pair<String, List<Int>>>>.naiveFit(): Long {
        val (shapes, trees) = this
        return trees.map { (field, parcels) ->
            val fieldSize = field.split("x").map { it.toLong() }.multiply()
            fieldSize >= parcels.mapIndexed { index, num -> num * shapes[index] }.sum()
        }.count{ it }
            .toLong()
    }

    private fun List<String>.parse(): Pair<List<Int>, List<Pair<String, List<Int>>>> {
        return this.take(30)
            .windowed(5, 5)
            .map { shape ->
                shape.sumOf { it.count("#") }
            } to this.filter { it.contains("x") }
            .map { line ->
                val (left, right ) = line.split(": ")
                left to right.split(" ").map { it.toInt() }
            }
    }
}
