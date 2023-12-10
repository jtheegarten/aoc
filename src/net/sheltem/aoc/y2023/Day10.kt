package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.Direction
import net.sheltem.aoc.common.Direction.*
import net.sheltem.aoc.common.move

suspend fun main() {
    Day10().run()
}

class Day10 : Day<Long>(8, 10) {

    override suspend fun part1(input: List<String>): Long = input.findLoop().first / 2 + 1

    override suspend fun part2(input: List<String>): Long = input.findLoop().second.inside(input)
}

private fun List<String>.findLoop(): Pair<Long, List<Pair<Int, Int>>> {
    val start = this.mapIndexed { index, s -> s.indexOf("S") to index }.single { it.first != -1 }

    var steps = 0L
    var currentPosition = start.move(SOUTH)
    var lastMove = SOUTH

    val loop = mutableListOf(currentPosition)

    do {
        steps += 1
        val currentChar = this[currentPosition.second][currentPosition.first]
        currentPosition = when (currentChar) {
            '|' -> when (lastMove) {
                SOUTH -> {
                    currentPosition.move(SOUTH)
                }

                NORTH -> {
                    currentPosition.move(NORTH)
                }

                else -> {
                    throwException(currentPosition, currentChar, lastMove)
                }
            }

            '-' -> when (lastMove) {
                EAST -> {
                    currentPosition.move(EAST)
                }

                WEST -> {
                    currentPosition.move(WEST)
                }

                else -> {
                    throwException(currentPosition, currentChar, lastMove)
                }
            }

            'L' -> when (lastMove) {
                WEST -> {
                    lastMove = NORTH
                    currentPosition.move(NORTH)
                }

                SOUTH -> {
                    lastMove = EAST
                    currentPosition.move(EAST)
                }

                else -> {
                    throwException(currentPosition, currentChar, lastMove)
                }
            }

            'J' -> when (lastMove) {
                EAST -> {
                    lastMove = NORTH
                    currentPosition.move(NORTH)
                }

                SOUTH -> {
                    lastMove = WEST
                    currentPosition.move(WEST)
                }

                else -> {
                    throwException(currentPosition, currentChar, lastMove)
                }
            }

            '7' -> when (lastMove) {
                NORTH -> {
                    lastMove = WEST
                    currentPosition.move(WEST)
                }

                EAST -> {
                    lastMove = SOUTH
                    currentPosition.move(SOUTH)
                }

                else -> {
                    throwException(currentPosition, currentChar, lastMove)
                }
            }

            'F' -> when (lastMove) {
                WEST -> {
                    lastMove = SOUTH
                    currentPosition.move(SOUTH)
                }

                NORTH -> {
                    lastMove = EAST
                    currentPosition.move(EAST)
                }

                else -> {
                    throwException(currentPosition, currentChar, lastMove)
                }
            }

            '.' -> throwException(currentPosition, currentChar, lastMove)
            else -> throwException(currentPosition, currentChar, lastMove)
        }
        loop.add(currentPosition)

//        println("$currentPosition = $currentChar => $lastMove")
    } while (this[currentPosition.second][currentPosition.first] != 'S')

//    println(steps)
    return steps to loop
}

private fun List<Pair<Int, Int>>.inside(inputMap: List<String>): Long {
    val newMap = mutableListOf<String>()
    newMap.forEach(::println)
    for (i in inputMap.indices) {
        newMap.add("")
        for (j in inputMap[0].indices) {
            newMap[i] = newMap[i] + if (this.contains(j to i)) inputMap[i][j] else "."
        }
        newMap[i] = newMap[i].replace("S", "|")
    }

    var count = 0L

    for (i in newMap.indices) {
        var inside = false
        var lastImportantChar = '.'
        for (j in newMap[0].indices) {
            val currentChar = newMap[i][j]
            when (currentChar) {
                '.' -> {
                    lastImportantChar = '.'
                    if (inside) {
                        count++
                    }
                }
                '|' -> inside = !inside
                '-' -> inside = inside
                else -> {
                    if (lastImportantChar == '.') {
                        lastImportantChar = currentChar
                        inside = !inside
                    } else {
                        if (
                            lastImportantChar == 'J' && currentChar == 'L' ||
                            lastImportantChar == 'L' && currentChar == 'J' ||
                            lastImportantChar == '7' && currentChar == 'F' ||
                            lastImportantChar == 'F' && currentChar == '7'
                        ) {
                            inside = !inside
                        }
                        lastImportantChar = '.'
                    }
                }
            }
        }
    }
    return count
}

private fun throwException(currentPosition: Pair<Int, Int>, currentChar: Char, lastMove: Direction): Nothing {
    throw IllegalStateException("Impossible Situation: [${currentPosition.second}][${currentPosition.first}] => $currentChar | lastMove =$lastMove ")
}
