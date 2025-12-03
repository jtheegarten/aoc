package net.sheltem.aoc.y2025

import kotlin.collections.map
import kotlin.text.split

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(357, 3121910778619) {

    override suspend fun part1(input: List<String>): Long = input
        .toIntList()
        .sumOf { it.toMaxJoltage() }
        .toLong()


    override suspend fun part2(input: List<String>): Long = input
        .toIntList()
        .sumOf { it.toMegaJoltage() }

    private fun List<Int>.toMaxJoltage(): Int {
        val max = this.max()
        val maxIndex = this.indexOf(max)
        return if (maxIndex < this.size - 1) {
            max * 10 + this.drop(maxIndex + 1).max()
        } else {
            this.dropLast(1).max() * 10 + max
        }
    }

    private fun List<Int>.toMegaJoltage(): Long = (11 downTo 0)
        .fold("" to 0) { (joltage, prevIndex), pos ->
            val maxDigit = this.drop(prevIndex).dropLast(pos).withIndex().maxBy { it.value }
            joltage + maxDigit.value.toString() to maxDigit.index + 1 + prevIndex
        }.first.toLong()

    private fun List<String>.toIntList() = map { line -> line.split("").filterNot { it.isEmpty() }.map { it.toInt() } }

}
