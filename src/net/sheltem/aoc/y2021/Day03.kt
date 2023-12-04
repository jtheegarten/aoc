package net.sheltem.aoc.y2021

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(198, 230) {

    override suspend fun part1(input: List<String>): Long = input
        .toBits()
        .let { it.first.toDecimal() to it.second.toDecimal() }
        .let { it.first * it.second }

    override suspend fun part2(input: List<String>): Long = input
        .let {
            it.filterBits() * it.filterBits(false)
        }
}

private fun List<String>.filterBits(mostCommon: Boolean = true): Long {
    var resultList = this

    for(index in resultList[0].indices) {

        resultList = resultList.partition { it[index] == '1' }
            .let {
                if (mostCommon && (it.first.size >= it.second.size) || !mostCommon && (it.first.size < it.second.size)) it.first else it.second
            }

        if (resultList.size == 1) return resultList.single().toDecimal()

    }
    return 0L
}

private fun List<String>.toBits(): Pair<String, String> {
    return (0 until this[0].length)
        .map { digit ->
            this.sumOf { row ->
                row[digit].digitToInt()
            }.let { if (it > size / 2) 1 else 0 }
        }.joinToString("")
        .let {
            it to it.flip()
        }
}

private fun String.toDecimal(): Long {
    var result = 0L
    indices.forEach {
        result = result.shl(1)
        if (this[it] == '1') result++
    }
    return result
}

private fun String.flip() = map { if (it == '0') '1' else '0' }.joinToString("")
