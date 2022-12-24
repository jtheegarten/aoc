package net.sheltem.aoc.y2022

import kotlin.math.absoluteValue

fun main() {
    Day09().run()
}

class Day09 : Day<Int>(88, 36) {

    override fun part1(input: List<String>): Int = input.toInstructions().moveMatrix(2).flatMap { it.toList() }.filter { it != 0 }.size

    override fun part2(input: List<String>): Int = input.toInstructions().moveMatrix(10).flatMap { it.toList() }.filter { it != 0 }.size
    private fun List<String>.moveMatrix(pieces: Int): Array<IntArray> {
        val ropeList = List(pieces) { Rope(500, 500) }
        val moveMatrix = Array(1000) { IntArray(1000) }
        moveMatrix[500][500] = 1

        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val start = ropeList[0]
            when (iterator.next()) {
                "R" -> start.x++
                "L" -> start.x--
                "U" -> start.y++
                "D" -> start.y--
            }
            var moved = true
            var position = 0

            while (moved && position < pieces - 1) {
                val head = ropeList[position]
                val tail = ropeList[position + 1]
                position++
                if (tail.needsToMove(head)) {
                    tail.moveAdjacent(head)
                } else {
                    moved = false
                }
            }
            if (position == pieces - 1 && moved) {
                ropeList[pieces - 1].let { moveMatrix[it.x][it.y]++ }
            }
        }

        return moveMatrix
    }
}

private fun List<String>.toInstructions() = map { it.split(" ") }.flatMap { (dir, rep) -> List(rep.toInt()) { dir } }

private class Rope(var x: Int, var y: Int) {

    fun needsToMove(other: Rope) = distance(other) > 2 || (distance(other) > 1 && (x == other.x || y == other.y))

    fun distance(other: Rope) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

    fun moveAdjacent(other: Rope) {
        y = when {
            other.y > y -> y + 1
            other.y < y -> y - 1
            else -> y
        }
        x = when {
            other.x > x -> x + 1
            other.x < x -> x - 1
            else -> x
        }
    }
}
