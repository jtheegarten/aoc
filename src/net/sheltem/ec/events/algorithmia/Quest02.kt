package net.sheltem.ec.events.algorithmia

import net.sheltem.common.Direction
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.WEST
import net.sheltem.common.Grid
import net.sheltem.common.PositionInt
import net.sheltem.common.lineTo
import net.sheltem.common.takeWord
import net.sheltem.common.toGrid
import net.sheltem.common.wrappingLineTo

suspend fun main() {
    Quest02().run()
}

class Quest02 : AlgorithmiaQuest<Long>(listOf(4, 42, 10)) {

    override suspend fun part1(input: List<String>): Long = input
        .let { it[0].split(":")[1].split(",") to it[2] }
        .let { (patternWords, list) ->
            patternWords.countIn(list)
        }


    override suspend fun part2(input: List<String>): Long = input
        .let { it[0].split(":")[1].split(",") to it.drop(2) }
        .let { (patternWords, lists) ->
            lists.sumOf { list ->
                patternWords.countSymbolsIn(list)
            }
        }

    override suspend fun part3(input: List<String>): Long = input
        .let { it[0].split(":")[1].split(",").sortedByDescending { word -> word.length } to it.drop(2).toGrid<Char>() }
        .countSymbols()

    private fun List<String>.countIn(str: String): Long = this
        .sumOf { word ->
            str.windowed(word.length).count { it.startsWith(word) }
        }.toLong()

    private fun List<String>.countSymbolsIn(str: String): Long {
        val foundIndices = mutableSetOf<Int>()
        for (i in str.indices) {
            this.forEach { word ->
                if (str.substring(i).startsWith(word)) foundIndices.addAll(i..<i + word.length)
            }
        }
        for (i in str.indices) {
            this.forEach { word ->
                if (str.reversed().substring(i).startsWith(word)) {
                    foundIndices.addAll((str.length - 1) - i downTo (str.length - i - word.length))
                }
            }
        }
        return foundIndices.size.toLong()
    }

    private fun Pair<List<String>, Grid<Char>>.countSymbols(): Long {
        val (words, grid) = this
        val marked = mutableSetOf<PositionInt>()

        for (word in words) {
            for (y in 0..grid.maxY) {
                for (x in 0..grid.maxX) {
                    Direction.cardinals.forEach { dir ->
                        val line = when (dir) {
                            EAST, WEST -> (x to y).wrappingLineTo(dir, word.length, grid)
                            else -> (x to y).lineTo(dir, word.length)
                        }

                        if (line.takeWord(grid) == word) {
                            marked.addAll(line)
                        }
                    }
                }
            }
        }
        return marked.size.toLong()
    }


}
