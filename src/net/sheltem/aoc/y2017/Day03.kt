package net.sheltem.aoc.y2017

import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.NORTH
import net.sheltem.common.Direction.SOUTH
import net.sheltem.common.Direction.WEST
import net.sheltem.common.manhattan
import net.sheltem.common.move
import kotlin.math.abs

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Long>(31, 9) {

    override suspend fun part1(input: List<String>): Long = input[0].toLong().distanceToCenter()

    override suspend fun part2(input: List<String>): Long = 5
}


private fun Long.distanceToCenter(): Long {

    var root = 1L
    while (root * root < this) {
        root += 1L
    }
    val remainder = (root * root) - this

    println("$root => $remainder")

    val distanceToCenterColumn = root / 2 - (remainder)

    return root + ((root / 2) + (root - abs(remainder))) - 1
}

private fun Long.distanceToCentre(): Long {
    var number = 1L
    var position = 0 to 0
    var direction = EAST
    val numberMap = mutableMapOf(number to position)

    while (true) {
        number += 1
        position = position.move(direction)

        direction = if (
            numberMap.values.maxOf { it.first } < position.first ||
            numberMap.values.maxOf { it.second } < position.second ||
            numberMap.values.minOf { it.first } > position.first ||
            numberMap.values.minOf { it.second } > position.second
        ) {
            when (direction) {
                EAST -> NORTH
                NORTH -> WEST
                WEST -> SOUTH
                SOUTH -> EAST
                else -> direction
            }
        } else {
            direction
        }
        numberMap[number] = position

        if (number.mod(1000) == 0) {
            println("Placed number $number")
        }
        if (this == number) break
    }

    return numberMap[this]!! manhattan (0 to 0)
}
