import kotlin.math.absoluteValue

class Day09 : Day<Int>(88, 36) {
    override fun part1(input: List<String>): Int = moveMatrix(input.toInstructions(), 2).flatMap { it.toList() }.filter { it != 0 }.size

    override fun part2(input: List<String>): Int = moveMatrix(input.toInstructions(), 10).flatMap { it.toList() }.filter { it != 0 }.size

    private fun moveMatrix(instructions: List<String>, pieces: Int): Array<IntArray> {
        val ropeList = List(pieces) { Rope(500, 500) }
        val moveMatrix = Array(1000) { IntArray(1000) }
        moveMatrix[500][500] = 1

        val iterator = instructions.iterator()
        while (iterator.hasNext()) {
            val start = ropeList[0]
            val direction = iterator.next()
            when (direction) {
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
//            println(direction)
//            printList(ropeList)
        }

        return moveMatrix
    }
}

fun main() {
    Day09().run()
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

private fun printList(list: List<Rope>) {
    val printArray = Array(20) { Array(20) { "." } }
    for (i in list.indices) {
        printArray[list[i].x % 10][list[i].y % 10] = "$i"
    }
    for (array in printArray) {
        println(array.joinToString("") { it })
    }
}
