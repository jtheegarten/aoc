import D3Direction.*

fun main() {
    Day18().run()
}

class Day18 : Day<Long>(64, 58) {

    override fun part1(input: List<String>): Long = input.toMatrix().countEmptyEdges()

    override fun part2(input: List<String>): Long = input.toMatrix().countSurfaceEdges()
}

private fun List<String>.toMatrix() = map { Point(it.split(",").map(String::toInt)) }.toSet()

private fun Set<Point>.countEmptyEdges(): Long = sumOf { it.neighbours().filter { neighbour -> neighbour !in this }.size }.toLong()

private fun Set<Point>.countSurfaceEdges(): Long {
    val minx = minOf { it.x } - 1
    val miny = minOf { it.y } - 1
    val minz = minOf { it.z } - 1
    val maxx = maxOf { it.x } + 1
    val maxy = maxOf { it.y } + 1
    val maxz = maxOf { it.z } + 1

    val surface = mutableSetOf<Point>()
    val pointsToCheck = mutableListOf(Point(minx, miny, minz))
    while (pointsToCheck.isNotEmpty()) {
        val pointToCheck = pointsToCheck.removeLast()
        if (pointToCheck in this) continue

        if (pointToCheck.x !in minx..maxx || pointToCheck.y !in miny..maxy || pointToCheck.z !in minz..maxz) continue

        if (surface.add(pointToCheck)) pointsToCheck.addAll(pointToCheck.neighbours())
    }

    return sumOf { it.neighbours().filter { neighbour -> neighbour in surface }.size }.toLong()
}

private data class Point(var x: Int, var y: Int, var z: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])

    fun neighbours() = listOf(move(UP), move(DOWN), move(FORWARD), move(BACKWARD), move(RIGHT), move(LEFT))

    fun move(dir: D3Direction) = Point(x + dir.x, y + dir.y, z + dir.z)
}

private enum class D3Direction(val x: Int, val y: Int, val z: Int) {
    UP(0, 0, 1),
    DOWN(0, 0, -1),
    FORWARD(0, 1, 0),
    BACKWARD(0, -1, 0),
    RIGHT(1, 0, 0),
    LEFT(-1, 0,0);

}
