fun main() {
    Day14().run()
}

class Day14 : Day<Int>(24, 93) {

    override fun part1(input: List<String>): Int = input.toCave().countMaxSandDrops()

    override fun part2(input: List<String>): Int = input.toCave().addFloor().countMaxSandDrops()
}

private fun Array<CharArray>.addFloor(): Array<CharArray> = apply { this[this.size - 1] = "#".repeat(1000).toCharArray() }

private fun Array<CharArray>.countMaxSandDrops() = generateSequence { this.dropSand(500, 0) }.takeWhile { it }.count()

private tailrec fun Array<CharArray>.dropSand(right: Int, down: Int): Boolean {
    return when {
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
}

private fun List<String>.toCave(): Array<CharArray> {
    val wallInstructions = this.map { it.toWallInstructions() }
    return Array(wallInstructions.flatten().maxOf { it.second } + 3) { CharArray(1000) { '.' } }.apply { wallInstructions.forEach(this::addWall) }
}

private fun Array<CharArray>.addWall(instructions: List<Pair<Int, Int>>) = instructions.zipWithNext().forEach { this.drawBetween(it.first, it.second) }

private fun Array<CharArray>.drawBetween(start: Pair<Int, Int>, end: Pair<Int, Int>) {
    if (start.first == end.first) {
        (start.second to end.second).toRange().forEach { this[it][start.first] = '#' }
    } else {
        (start.first to end.first).toRange().forEach{ this[start.second][it] = '#' }
    }
}

private fun Pair<Int, Int>.toRange() = if (first <= second) first..second else second..first

private fun String.toWallInstructions() = split(" -> ").map { it.split(",").let { position -> position[0].toInt() to position[1].toInt() } }
