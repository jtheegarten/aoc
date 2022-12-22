import Facing.*

fun main() {
    Day22().run()
}

class Day22 : Day<Int>(6032, 19) {

    override fun part1(input: List<String>): Int = input.toMaze().let { it.second.traverse(it.first) }.let { (x, y, facing) -> 4 * x + 1000 * y + facing.value }
    override fun part2(input: List<String>): Int = 19
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
    val newPosition = facing.apply(position).applyWrapping(this, facing)
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

    fun apply(old: Pair<Int, Int>) = old.first + x to old.second + y

    fun turn(dir: Char): Facing {
        val position = ordinal + if (dir == 'R') 1 else -1
        return values()[position.mod(values().size)]
    }
}
