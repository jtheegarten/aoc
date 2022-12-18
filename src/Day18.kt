import kotlin.math.absoluteValue

fun main() {
    Day18().run()
}

class Day18 : Day<Long>(64, 58) {

    override fun part1(input: List<String>): Long = input.toMatrix().countEmptyEdges()

    override fun part2(input: List<String>): Long = 0
}

private fun List<String>.toMatrix() = map { Point(it.split(",").map(String::toInt)) }

private fun List<Point>.countEmptyEdges(): Long =
    sumOf { comparePoint ->
        6 - count { it.distance(comparePoint) == 1 }
    }.toLong()

private data class Point(var x: Int, var y: Int, var z: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])

    fun distance(other: Point) = (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue
}
