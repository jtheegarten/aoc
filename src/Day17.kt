import Direction.*

fun main() {
    Day17().run()
}

class Day17 : Day<Long>(3068, 19) {

    override fun part1(input: List<String>): Long = input.first().toDirectionList().simulateRocks(2022)

    override fun part2(input: List<String>): Long = input.first().toDirectionList().part2()
}

private fun List<Direction>.part2(): Long {
    val goal = 1000000000000L
    return 0L
}

private fun List<Direction>.simulateRocks(
    maxRocks: Long
): Long {
    val caveMap = mutableMapOf<Int, MutableSet<Int>>()
    var instructionIndex = 0
    for (i in 0 until 2022) {
        val rockToDrop = Shape.values()[i % 5]
        var position = (if (rockToDrop == Shape.PLUS) 4 else 3) to (caveMap.keys.maxOrNull() ?: 0) + 4

        while (true) {
            if (caveMap.step(position, rockToDrop, this[instructionIndex])) {
                position = position.move(this[instructionIndex])
            }
            instructionIndex = (instructionIndex + 1) % this.size

            if (!caveMap.step(position, rockToDrop, DOWN)) break
            position = position.move(DOWN)
        }
        caveMap.addRock(position, rockToDrop)
    }

    return caveMap.size.toLong()
}

private fun MutableMap<Int, MutableSet<Int>>.addRockChunk(position: Pair<Int, Int>) {
    if (!this.containsKey(position.second)) this[position.second] = mutableSetOf()
    this[position.second]!!.add(position.first)
}

private fun MutableMap<Int, MutableSet<Int>>.addRock(position: Pair<Int, Int>, rockToAdd: Shape) {
    var positionToAdd = position
    addRockChunk(position)

    for (dir in rockToAdd.sequence) {
        positionToAdd = positionToAdd.move(dir)
        addRockChunk(positionToAdd)
    }
}

private fun Map<Int, Set<Int>>.step(position: Pair<Int, Int>, rockToDrop: Shape, direction: Direction): Boolean {
    var checkPosition: Pair<Int, Int> = position.move(direction)
    val partIterator = rockToDrop.sequence.iterator()

    while (true) {
        if (checkPosition.first < 1
            || checkPosition.first > 7
            || checkPosition.second < 1
            || this.getOrDefault(checkPosition.second, setOf()).contains(checkPosition.first)
        ) {
            return false
        }

        if (!partIterator.hasNext()) break
        checkPosition = checkPosition.move(partIterator.next())
    }
    return true
}

private fun String.toDirectionList() = trim().map { if (it == '<') LEFT else RIGHT }

private enum class Shape(val sequence: List<Direction>) {
    MINUS(listOf(RIGHT, RIGHT, RIGHT)),
    PLUS(listOf(UP, LEFT, RIGHT, RIGHT, LEFT, UP)),
    HOOK(listOf(RIGHT, RIGHT, UP, UP)),
    STICK(listOf(UP, UP, UP)),
    BLOCK(listOf(RIGHT, UP, LEFT));
}

private enum class Direction(val x: Int, val y: Int) {
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0);
}

private fun Pair<Int, Int>.move(direction: Direction) = first + direction.x to second + direction.y

private data class Cycle(val height: Long, val numberOfRocks: Long, val reset: Boolean)
