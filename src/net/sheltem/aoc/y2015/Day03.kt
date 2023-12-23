package net.sheltem.aoc.y2015

import net.sheltem.common.Direction8
import net.sheltem.common.PositionInt
import net.sheltem.common.move

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Int>(4, 3) {
    override suspend fun part1(input: List<String>): Int = input.first().map { Direction8.fromCaret(it) }.travel().keys.size

    override suspend fun part2(input: List<String>): Int = input.first().map { Direction8.fromCaret(it) }.travelTogether().keys.size
}


private fun List<Direction8>.travel(): Map<PositionInt, Int> {
    var position = (0 to 0)
    val positionMap = mutableMapOf(position to 1)
    forEach {
        position = position.move(it)
        positionMap.merge(position, 1) { a, b -> a + b }
    }
    return positionMap
}

private fun List<Direction8>.travelTogether(): Map<PositionInt, Int> {
    var santa = (0 to 0)
    var robo = (0 to 0)
    val positionMap = mutableMapOf(santa to 2)
    forEachIndexed { i, dir ->
        if (i % 2 == 0) {
            robo = robo.move(dir)
            positionMap.merge(robo, 1) { a, b -> a + b }
        } else {
            santa = santa.move(dir)
            positionMap.merge(santa, 1) { a, b -> a + b }
        }
    }
    return positionMap
}
