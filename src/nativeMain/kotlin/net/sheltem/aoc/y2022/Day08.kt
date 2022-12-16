package net.sheltem.aoc.y2022

import Day

class Day08 : Day<Int>(21, 8) {
    override fun part1(input: List<String>): Int = input.trees().countVisible()

    override fun part2(input: List<String>): Int = input.trees().toScenicScores().max()

}

private fun Array<Array<Int>>.countVisible(): Int = countEdges() + countInsides()

private fun List<String>.trees(): Array<Array<Int>> = map { it.toTreeLine() }.toTypedArray()

private fun String.toTreeLine() = this.toCharArray().map { it.digitToInt() }.toTypedArray()

private fun Array<Array<Int>>.countEdges() = (size + this[0].size - 2) * 2

private fun Array<Array<Int>>.countInsides(): Int {
    var visibles = 0
    for (i in 1 until (this.size - 1)) {
        for (j in 1 until (this[i].size - 1)) {
            if (isVisible(i, j, this)) visibles++
        }
    }
    return visibles
}

private fun scenicScore(i: Int, j: Int, array: Array<Array<Int>>) =
    array[i][j].let { currentTree ->
        array[i].take(j).reversed().viewDistance(currentTree)
            .times(array[i].takeLast(array[i].size - (j + 1)).viewDistance(currentTree))
            .times(array.take(i).map { it[j] }.reversed().viewDistance(currentTree))
            .times(array.takeLast(array.size - (i + 1)).map { it[j] }.viewDistance(currentTree))
    }

private fun List<Int>.viewDistance(maxHeight: Int) = indexOfFirst { it >= maxHeight }.let { if (it == -1) size else it + 1 }

private fun isVisible(i: Int, j: Int, array: Array<Array<Int>>): Boolean =
    array[i][j].let { currentTree ->
        array[i].take(j).all { it < currentTree }
                || array[i].takeLast(array[i].size - (j + 1)).all { it < currentTree }
                || array.take(i).map { it[j] }.all { it < currentTree }
                || array.takeLast(array.size - (i + 1)).map { it[j] }.all { it < currentTree }
    }

private fun Array<Array<Int>>.toScenicScores(): List<Int> {
    val scenicScores = mutableListOf<Int>()
    for (i in indices) {
        for (j in this[i].indices) {
            scenicScores.add(scenicScore(i, j, this))
        }
    }
    return scenicScores
}
