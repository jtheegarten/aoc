package net.sheltem.aoc.y2023

import net.sheltem.common.Direction
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.SOUTH
import net.sheltem.common.PositionInt
import net.sheltem.common.move
import net.sheltem.common.within
import net.sheltem.aoc.y2023.Day17.CacheEntry
import net.sheltem.aoc.y2023.Day17.QueueEntry
import java.util.*

suspend fun main() {
    Day17().run()
}

class Day17 : Day<Int>(102, 94) {

    override suspend fun part1(input: List<String>): Int = input.bfs(0 to 0, input[0].indices.last to input.indices.last, 3)

    override suspend fun part2(input: List<String>): Int = input.bfs(0 to 0, input[0].indices.last to input.indices.last, 10, 4)

    data class QueueEntry(val position: PositionInt, val direction: Direction, val loss: Int, val straight: Int) : Comparable<QueueEntry> {
        override fun compareTo(other: QueueEntry) = this.loss - other.loss
    }

    data class CacheEntry(val position: PositionInt, val direction: Direction, val straight: Int)
}

private fun List<String>.bfs(start: PositionInt, target: PositionInt, maxStraight: Int, minStraight: Int = 0): Int {
    val queue = PriorityQueue<QueueEntry>()
    val visited = mutableMapOf<CacheEntry, Int>()
    queue.offer(QueueEntry(start.move(EAST), EAST, 0, 1))
    queue.offer(QueueEntry(start.move(SOUTH), SOUTH, 0, 1))

    while (queue.isNotEmpty()) {
        val (position, direction, loss, straight) = queue.poll()!!
        if (!position.within(this)) {
            continue
        }
        val totalLoss = this[position.second][position.first].digitToInt() + loss

        if (position == target && straight >= minStraight) {
            return totalLoss
        }

        val cacheEntry = CacheEntry(position, direction, straight)

        if ((visited[cacheEntry] ?: Int.MAX_VALUE) <= totalLoss) {
            continue
        }

        visited[cacheEntry] = totalLoss

        if (straight >= minStraight) {
            queue.offer(QueueEntry(position.move(direction.turnLeft()), direction.turnLeft(), totalLoss, 1))
            queue.offer(QueueEntry(position.move(direction.turnRight()), direction.turnRight(), totalLoss, 1))
        }
        if (straight < maxStraight) {
            queue.offer(QueueEntry(position.move(direction), direction, totalLoss, straight + 1))
        }
    }

    return 0
}
