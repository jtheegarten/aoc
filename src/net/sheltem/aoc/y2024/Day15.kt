package net.sheltem.aoc.y2024

import net.sheltem.common.*
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.WEST
import java.util.*

suspend fun main() {
    Day15().run()
}

class Day15 : Day<Long>(10092, 9021) {

    override suspend fun part1(input: List<String>): Long = input.parse().execute()

    override suspend fun part2(input: List<String>): Long = input.parse(true).execute()


    private fun List<String>.parse(double: Boolean = false): Pair<Grid<Char>, List<Direction>> {
        val separator = indexOfFirst { it.isBlank() }
        val map = take(separator).map { if (!double) it else it.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.") }
        val instructions = drop(separator + 1).joinToString("").map { Direction.from(it.toString()) }

        return map.toGrid() to instructions
    }

    private fun Pair<Grid<Char>, List<Direction>>.execute(): Long {
        val (grid, instructions) = this
        var pos = grid.find('@')

        grid[pos] = '.'

        instructions.forEach { dir ->
            val next = pos move dir
            when (grid[next]) {
                '.' -> pos = next
                '#' -> Unit
                '[', ']' -> {
                    val checked = mutableSetOf<PositionInt>()
                    val stack = Stack<PositionInt>().apply { add(next) }

                    while (stack.isNotEmpty()) {
                        val stackPos = stack.pop()
                        if (checked.add(stackPos)) {

                            when (grid[stackPos]) {
                                '[' -> stack.add(stackPos move EAST )
                                ']' -> stack.add(stackPos move WEST)
                            }

                            val nextStackPos = stackPos move dir

                            if (grid[nextStackPos]!! in "[]") {
                                stack.add(nextStackPos)
                            } else if (grid[nextStackPos] == '#') {
                                return@forEach
                            }
                        }
                    }

                    checked.associateWith { grid[it]!! }
                        .onEach { (position, _) -> grid[position] = '.' }
                        .forEach { (position, char) -> grid[position move dir] = char }

                    pos = next
                }

                'O' -> {
                    var box = next
                    while (grid[box move dir] == 'O') {
                        box = box move dir
                    }
                    if (grid[box move dir] != '#') {
                        grid[box move dir] = 'O'
                        grid[pos move dir] = '.'
                        pos = next
                    }
                }
            }
        }

        return grid.transform { coord, c ->
            if (c in "[O") coord.score() else 0
        }.allFields.sum()
    }

    private fun PositionInt.score(): Long = first.toLong() + (second * 100)
}

/*
    private fun Set<PositionInt>.score(): Long = sumOf { it.score() }

    private fun Pair<List<String>, List<Direction>>.execute(): Pair<PositionInt, Set<PositionInt>> {
        val (map, dirs) = this

        var pos = 0 to 0
        val boxes = mutableSetOf<PositionInt>()
        val walls = mutableSetOf<PositionInt>()

        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when (c) {
                    '#' -> walls.add(x to y)
                    'O' -> boxes.add(x to y)
                    '@' -> pos = x to y
                }
            }
        }

        for (dir in dirs) {
            val next = pos.move(dir)
            val forwardLine = next.lineTo(dir, map.size).filter { it.within(map) }.asSequence().takeWhile { !walls.contains(it) }
            val gapPos = forwardLine.firstOrNull { !boxes.contains(it) }
            when {
                gapPos == next -> pos = next
                gapPos != null -> {
                    boxes.remove(forwardLine.first())
                    boxes.add(gapPos)
                    pos = next
                }
            }
        }

        return pos to boxes
    }
 */
