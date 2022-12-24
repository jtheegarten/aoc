package net.sheltem.aoc.y2022



fun main() {
    Day20().run()
}

class Day20 : Day<Long>(3L, 1623178306L) {

    override fun part1(input: List<String>): Long = input.map { it.toLong() }.mixed().findSignal()

    override fun part2(input: List<String>): Long = input.map { it.toLong() }.mixed(811589153, 10).findSignal()
}

private fun List<Long>.mixed(key: Long = 1, repeats: Int = 1): List<Long> {
    val indexed = withIndex().map { it.index to it.value * key }.toMutableList()
    repeat(repeats) {
        for (i in indices) {
            val (index, element) = indexed.withIndex().first { it.value.first == i }
            indexed.removeAt(index)
            val newIndex = (index + element.second).mod(size - 1)
            indexed.add(newIndex, element)
        }
    }
    return indexed.map { it.second }
}

private fun List<Long>.findSignal() =
    indexOfFirst { it == 0L }.let { zeroIndex -> listOf(1000, 2000, 3000).sumOf { this[(it + zeroIndex) % this.size] } }
