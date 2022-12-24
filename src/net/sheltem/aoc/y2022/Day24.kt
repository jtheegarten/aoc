package net.sheltem.aoc.y2022

import net.sheltem.aoc.common.Day
import net.sheltem.aoc.common.Direction8
import net.sheltem.aoc.common.Direction8.*
import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.move
import net.sheltem.aoc.y2022.Field.*
import java.lang.Integer.min

fun main() {
    Day24().run()
}

private typealias FieldMap = List<List<MutableList<Field>>>

class Day24 : Day<Int>(18, 54) {

    override fun part1(input: List<String>): Int = input.toFieldMap().travel()

    override fun part2(input: List<String>): Int = input.toFieldMap().travel(3)
}

private fun FieldMap.travel(trips: Int = 1): Int {
    val start = first().indexOfFirst { it.contains(Empty) } to 0
    val end = last().indexOfFirst { it.contains(Empty) } to (size - 1)

    var cost = 0
    var (frontier, currentState) = traverseMap(start, end)
    cost += frontier[end]!!
    for (i in 2..trips) {
        val tempStart = if (i % 2 == 0) end else start
        val tempEnd = if (i % 2 == 0) start else end
        val runResult = currentState.traverseMap(tempStart, tempEnd)
        frontier = runResult.first
        currentState = runResult.second
        cost += frontier[tempEnd]!!
    }

    return cost
}

private fun FieldMap.traverseMap(
    start: PositionInt,
    end: PositionInt
): Pair<MutableMap<PositionInt, Int>, FieldMap> {
    var frontier = mutableMapOf(start to 0)
    var currentState = this

    while (!frontier.containsKey(end)) {
        currentState = currentState.blizzards()

        val moves = mutableMapOf<PositionInt, Int>()


        frontier.forEach { (location, cost: Int) ->
            (Direction8.cardinalValues() + NEUTRAL).forEach {
                val (newX, newY) = location.move(it)
                if (newX in this[0].indices && newY in indices) {
                    if (currentState[newY][newX].all { field -> field is Empty }) {
                        moves.merge(newX to newY, cost + 1) { a, b -> min(a, b) }
                    }
                }
            }
        }

        frontier = moves
    }
    return frontier to currentState
}

private fun FieldMap.printMap() = forEach { row ->
    row.forEach { column ->
        if (column.size == 1) {
            print(column.single().icon)
        } else {
            print('*')
        }
    }
    println()
}

private fun FieldMap.blizzards(): FieldMap {

    val newMap = emptyFieldMap(first().size, size)

    forEachIndexed { y, row ->
        row.forEachIndexed { x, column ->
            column.forEach { field ->
                if (field == Wall) newMap[y][x].add(Wall)
            }
        }
    }

    forEachIndexed { y, row ->
        row.forEachIndexed { x, column ->
            column.forEach { field ->
                if (field is Blizzard) newMap.move(x to y, field)
            }
        }
    }

    forEachIndexed { y, row ->
        row.forEachIndexed { x, _ ->
            if (newMap[y][x].isEmpty()) newMap[y][x].add(Empty)
        }
    }

    return newMap

}

private fun emptyFieldMap(width: Int, height: Int) = List(height) {
    List(width) { mutableListOf<Field>() }
}

private fun FieldMap.move(position: PositionInt, field: Blizzard) {
    val (newX, newY) = position.move(field.direction)

    if (this[newY][newX].size == 1 && this[newY][newX].all { content -> content is Wall }) {
        when (field.direction) {
            NORTH -> this[size - 2][newX].add(field)
            SOUTH -> this[1][newX].add(field)
            EAST -> this[newY][1].add(field)
            WEST -> this[newY][first().size - 2].add(field)
            else -> error("Direction not supported!")
        }
    } else {
        this[newY][newX].add(field)
    }
}

private fun List<String>.toFieldMap(): FieldMap =
    map { line ->
        buildList {
            line.forEach { char ->
                when (char) {
                    '#' -> add(mutableListOf(Wall))
                    '.' -> add(mutableListOf(Empty))
                    '^' -> add(mutableListOf(Blizzard(NORTH)))
                    '>' -> add(mutableListOf(Blizzard(EAST)))
                    'v' -> add(mutableListOf(Blizzard(SOUTH)))
                    '<' -> add(mutableListOf(Blizzard(WEST)))
                }
            }
        }
    }

private sealed class Field(val icon: Char) {
    object Wall : Field('#')
    object Empty : Field('.')
    data class Blizzard(val direction: Direction8) : Field(
        when (direction) {
            NORTH -> '^'
            SOUTH -> 'v'
            EAST -> '>'
            WEST -> '<'
            else -> '?'
        }
    )
}
