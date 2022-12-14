class Day14 : Day<Int>(24, 93) {
    override fun part1(input: List<String>): Int = input.toMaze().countMaxSandDrops()

    override fun part2(input: List<String>): Int = input.toMaze(true).countMaxSandDrops()

}

fun main() {
    Day14().run()
}

private fun Array<CharArray>.countMaxSandDrops(): Int {
    var result = 0
    while (this.dropSand(500, 0)) {
        result++
    }
    return result
}

private fun Array<CharArray>.dropSand(right: Int, down: Int): Boolean =
    when {
        this[down][right] == 'o' -> false
        this.size == down + 1 -> false
        this[down + 1][right] == '.' -> dropSand(right, down + 1)
        this[down + 1][right - 1] == '.' -> dropSand(right - 1, down + 1)
        this[down + 1][right + 1] == '.' -> dropSand(right + 1, down + 1)
        else -> {
            this[down][right] = 'o'
            true
        }
    }

private fun List<String>.toMaze(withFloor: Boolean = false): Array<CharArray> {
    val wallInstructions = this.map { it.toWallInstructions() }
    val cave = Array(wallInstructions.flatten().maxOf { it.second } + 3) { CharArray(1000) { '.' } }

    wallInstructions.forEach { cave.addWall(it) }

    if (withFloor) {
        cave[cave.size - 1] = "#".repeat(1000).toCharArray()
    }

    return cave
}

private fun Array<CharArray>.addWall(instructions: List<Pair<Int, Int>>) {
    val instIterator = instructions.iterator()
    var (prevRight, prevDown) = instIterator.next()
    while (instIterator.hasNext()) {
        val (right, down) = instIterator.next()
        if (down != prevDown) {
            (prevDown to down).toRange().forEach { this[it][right] = '#' }
        } else {
            (prevRight to right).toRange().forEach { this[down][it] = '#' }
        }
        prevRight = right
        prevDown = down
    }
}

private fun Pair<Int, Int>.toRange() = if (first <= second) first..second else second..first

private fun String.toWallInstructions() = split(" -> ").map { it.split(",").let { position -> position[0].toInt() to position[1].toInt() } }
