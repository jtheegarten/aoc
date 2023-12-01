package net.sheltem.aoc.y2022


import net.sheltem.aoc.y2022.Direction.*
import java.util.function.Predicate

suspend fun main() {
    Day17().run(true)
}

class Day17 : Day<Long>(3068, 1514285714288) {

    override suspend fun part1(input: List<String>): Long = input.first().toDirectionList().simulateRocks(2022) { _ -> false }.height.toLong()

    override suspend fun part2(input: List<String>): Long = input.first().toDirectionList().part2()
}

private fun List<Direction>.part2(): Long {
    val goal = 1000000000000L
    val startOfCycle = simulateRocks(goal) { c -> c.reset && (c.numberOfRocks != 0L) }
    val endOfCycle = simulateRocks(goal) { c -> c.reset && (c.numberOfRocks > startOfCycle.numberOfRocks) }

    val rocksPerCycle = endOfCycle.numberOfRocks - startOfCycle.numberOfRocks
    val numberOfCycles = goal / rocksPerCycle
    val totalRocks = rocksPerCycle * numberOfCycles + startOfCycle.numberOfRocks

    val heightPerCycle = endOfCycle.height - startOfCycle.height
    val totalHeight = heightPerCycle * numberOfCycles + startOfCycle.height
    val overshoot = totalRocks - goal

    val atOvershoot = simulateRocks(goal) { c -> c.numberOfRocks == startOfCycle.numberOfRocks - overshoot }

    return totalHeight - (startOfCycle.height - atOvershoot.height)
}

private fun List<Direction>.simulateRocks(
    maxRocks: Long,
    condition: Predicate<State>,
): State {
    val caveMap = mutableMapOf<Int, MutableSet<Int>>()
    var instructionIndex = 0
    var rocksDropped = 0L
    while (rocksDropped < maxRocks) {

        val currentCycle = State(caveMap.size, rocksDropped, instructionIndex == 0)
        if (condition.test(currentCycle)) {
            return currentCycle
        }

        val rockToDrop = Shape.values()[(rocksDropped % 5).toInt()]
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
        rocksDropped++
    }

    return State(caveMap.size, maxRocks, false)
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

private data class State(val height: Int, val numberOfRocks: Long, val reset: Boolean)
