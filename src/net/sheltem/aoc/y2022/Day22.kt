package net.sheltem.aoc.y2022


import net.sheltem.aoc.y2022.Facing.DOWN
import net.sheltem.aoc.y2022.Facing.LEFT
import net.sheltem.aoc.y2022.Facing.RIGHT
import net.sheltem.aoc.y2022.Facing.UP

suspend fun main() {
    Day22().run(true)
}

class Day22 : Day<Int>(6032, 19) {
    override suspend fun part1(input: List<String>): Int = input.toMaze().let { it.second.traverse(it.first) }.let { (x, y, facing) -> 4 * x + 1000 * y + facing.value }
    override suspend fun part2(input: List<String>): Int = input.toCube().let { it.second.traverse(it.first) }.let { (x, y, facing) -> 4 * x + 1000 * y + facing.value }
}

private fun Cube.traverse(instructions: String): Triple<Int, Int, Facing> {
    var position = CubePosition(0, 0, 0, RIGHT)
    var remainingInstructions = instructions

    while (true) {
        val steps = remainingInstructions.takeWhile { it.isDigit() }.toInt()
        remainingInstructions = remainingInstructions.dropWhile { it.isDigit() }

        repeat(steps) {
            val newPosition = this.move(position)
            if (faces[newPosition.cube].content[newPosition.y][newPosition.x] == '.') position = newPosition
        }

        if (remainingInstructions.isEmpty()) break

        val turn = remainingInstructions.first()
        remainingInstructions = remainingInstructions.drop(1)

        position = position.turn(turn)

    }

    val xAndY = when(position.cube) {
        0 -> position.x + 50 to position.y
        1 -> position.x + 100 to position.y
        2 -> position.x + 50 to position.y + 50
        3 -> position.x to position.y + 100
        4 -> position.x + 50 to position.y + 100
        5 -> position.x to position.y + 150
        else -> 0 to 0
    }

    return Triple(xAndY.first + 1, xAndY.second + 1, position.facing)
}

private fun Cube.move(position: CubePosition): CubePosition {
    var newPosition = position.facing.applyOn(position.x to position.y).let { CubePosition(it.first, it.second, position.cube, position.facing) }
    if (newPosition.x !in 0 until 50 || newPosition.y !in 0 until 50) {
        newPosition = position.translocateTo(faces[position.cube].translocations[position.facing]!!)
    }
    return newPosition
}

private fun List<String>.toCube(): Pair<String, Cube> {
    val input = this.map { it.trim() }
    val trueInput = buildList {
        for (i in 0 until 50) {
            add(input[i] + input[i + 50] + input[i + 100] + input[i + 150])
        }
    }

    val cubeMaps = List(6) { Array(50) { CharArray(50) } }
    trueInput.forEachIndexed { lineIndex, line ->
        val cubeLine = line.windowed(50, 50)
        (cubeMaps.indices).forEach {
            cubeMaps[it][lineIndex] = cubeLine[it].toCharArray()
        }
    }
//    cubeMaps.forEachIndexed { index, map ->
//        println("net.sheltem.aoc.y2022.Cube $index")
//        map.forEach {
//            println(it)
//        }
//    }
    val cubeTranslocations = listOf(
        mapOf(UP to Translocation(5, RIGHT), RIGHT to Translocation(1, RIGHT), LEFT to Translocation(3, RIGHT), DOWN to Translocation(2, DOWN)),
        mapOf(UP to Translocation(5, UP), RIGHT to Translocation(4, LEFT), LEFT to Translocation(0, LEFT), DOWN to Translocation(2, LEFT)),
        mapOf(UP to Translocation(0, UP), RIGHT to Translocation(1, UP), LEFT to Translocation(3, DOWN), DOWN to Translocation(4, DOWN)),
        mapOf(UP to Translocation(2, RIGHT), RIGHT to Translocation(4, RIGHT), LEFT to Translocation(0, RIGHT), DOWN to Translocation(5, DOWN)),
        mapOf(UP to Translocation(2, UP), RIGHT to Translocation(1, LEFT), LEFT to Translocation(3, LEFT), DOWN to Translocation(5, LEFT)),
        mapOf(UP to Translocation(3, UP), RIGHT to Translocation(4, UP), LEFT to Translocation(0, DOWN), DOWN to Translocation(1, DOWN))
    )

    return last() to Cube(cubeMaps.mapIndexed { index, content -> CubeFace(content, cubeTranslocations[index]) })
}

private fun List<String>.toMaze(): Pair<String, Array<CharArray>> {
    val width = this.maxOf { it.length }
    return last() to dropLast(2).map { it.toCharArrayWithWidth(width) }.toTypedArray()
}

private fun String.toCharArrayWithWidth(width: Int) = (if (this.length == width) this else this + " ".repeat(width - this.length)).toCharArray()

private fun Array<CharArray>.traverse(instructions: String): Triple<Int, Int, Facing> {
    var facing = RIGHT
    var position = this[0].indexOfFirst { it == '.' } to 0
    var remainingInstructions = instructions

    while (true) {
        val steps = remainingInstructions.takeWhile { it.isDigit() }.toInt()
        remainingInstructions = remainingInstructions.dropWhile { it.isDigit() }

        repeat(steps) {
            position = this.move(position, facing)
        }
        if (remainingInstructions.isEmpty()) break
        val turn = remainingInstructions.first()
        remainingInstructions = remainingInstructions.drop(1)
        facing = facing.turn(turn)
    }
    return Triple(position.first + 1, position.second + 1, facing)
}

private fun Pair<Int, Int>.applyWrapping(maze: Array<CharArray>, facing: Facing): Pair<Int, Int> {
    var (newX, newY) = this
    if (newX >= maze[0].size) newX %= maze[0].size
    if (newY >= maze.size) newY %= maze.size
    if (newX < 0) newX = maze[newY].size - 1
    if (newY < 0) newY = maze.size - 1
    val newChar = maze[newY][newX]
    if (newChar == ' ') {
        when (facing) {
            LEFT -> newX = maze[newY].indexOfLast { it == '.' || it == '#' }
            RIGHT -> newX = maze[newY].indexOfFirst { it == '.' || it == '#' }
            UP -> newY = maze.indices.reversed().first { maze[it][newX] == '.' || maze[it][newX] == '#' }
            DOWN -> newY = maze.indices.first { maze[it][newX] == '.' || maze[it][newX] == '#' }
        }
    }
    return newX to newY
}

private fun Array<CharArray>.move(position: Pair<Int, Int>, facing: Facing): Pair<Int, Int> {
    val newPosition = facing.applyOn(position).applyWrapping(this, facing)
    return when (this[newPosition.second][newPosition.first]) {
        '#' -> position
        '.' -> newPosition
        else -> position
    }
}

private enum class Facing(val x: Int, val y: Int, val value: Int) {
    UP(0, -1, 3),
    RIGHT(1, 0, 0),
    DOWN(0, 1, 1),
    LEFT(-1, 0, 2);

    fun applyOn(old: Pair<Int, Int>) = old.first + x to old.second + y

    fun turn(dir: Char): Facing {
        val position = ordinal + if (dir == 'R') 1 else -1
        return entries[position.mod(entries.size)]
    }
}

private class CubeFace(var content: Array<CharArray>, var translocations: Map<Facing, Translocation>)

private class Cube(val faces: List<CubeFace>)

private class Translocation(val number: Int, val newFacing: Facing)

private data class CubePosition(val x: Int, val y: Int, val cube: Int, val facing: Facing) {
    fun turn(dir: Char): CubePosition = copy(facing = facing.turn(dir))

    fun translocateTo(translocation: Translocation): CubePosition {

        val newFacing = translocation.newFacing
        val targetFace = translocation.number
        return when (this.facing) {
            UP -> when (newFacing) {
                UP -> CubePosition(this.x, 49, targetFace, newFacing)
                DOWN -> throw IllegalStateException("UP->DOWN")
                LEFT -> CubePosition(49, 49 - this.x, targetFace, newFacing)
                RIGHT -> CubePosition(0, this.x, targetFace, newFacing)
            }

            DOWN -> when (newFacing) {
                UP -> throw IllegalStateException("DOWN->UP")
                DOWN -> CubePosition(this.x, 0, targetFace, newFacing)
                LEFT -> CubePosition(49, this.x, targetFace, newFacing)
                RIGHT -> CubePosition(0, 49 - this.x, targetFace, newFacing)
            }

            LEFT -> when (newFacing) {
                UP -> CubePosition(49 - this.y, 49, targetFace, newFacing)
                DOWN -> CubePosition(this.y, 0, targetFace, newFacing)
                LEFT -> CubePosition(49, this.y, targetFace, newFacing)
                RIGHT -> CubePosition(0, 49 - this.y, targetFace, newFacing)
            }

            RIGHT -> when (newFacing) {
                UP -> CubePosition(this.y, 49, targetFace, newFacing)
                DOWN -> CubePosition(49 - this.y, 0, targetFace, newFacing)
                LEFT -> CubePosition(49, 49 - this.y, targetFace, newFacing)
                RIGHT -> CubePosition(0, this.y, targetFace, newFacing)
            }
        }
    }
}
