package net.sheltem.aoc.y2024

import net.sheltem.common.*
import net.sheltem.common.Direction.EAST
import java.util.*
import kotlin.collections.HashMap

suspend fun main() {
    Day16().run()
}

class Day16 : Day<Long>(11048, 64) {

    override suspend fun part1(input: List<String>): Long = input.toGrid().labyrinth()

    override suspend fun part2(input: List<String>): Long = input.toGrid().seats()


    private fun Grid<Char>.labyrinth(): Long {
        val start = this.find('S')
        val end = this.find('E')

        val queue = PriorityQueue(compareBy<Triple<PositionInt, Direction, Long>> { it.third }).apply { add(Triple(start, EAST, 0L)) }
        val visited = mutableMapOf<Pair<PositionInt, Direction>, Long>()

        while (queue.isNotEmpty()) {
            val (pos, dir, score) = queue.poll()

            when {
                pos == end -> return score
                visited.getOrDefault(pos to dir, Long.MAX_VALUE) < score -> continue
                else -> {
                    visited[pos to dir] = score
                    if (this[pos move dir] != '#') queue.add(Triple(pos move dir, dir, score + 1))
                    queue.add(Triple(pos, dir.turnRight(), score + 1000))
                    queue.add(Triple(pos, dir.turnLeft(), score + 1000))
                }
            }
        }
        return 0L
    }

    private fun Grid<Char>.seats(): Long {
        val start = this.find('S')
        val end = this.find('E')

        val queue = PriorityQueue<Triple<List<PositionInt>, Direction, Long>>(compareBy { it.third }).apply { add(Triple(listOf(start), EAST, 0L)) }
        val visited = HashMap<Pair<PositionInt, Direction>, Long>()
        var min = Long.MAX_VALUE
        val best = HashSet<PositionInt>()


        while(queue.isNotEmpty()) {
            val (path, dir, score) = queue.poll()
            val pathEnd = path.last()

            if (pathEnd == end) {
                if (score <= min) min = score else return best.size.toLong()
                best.addAll(path)
            }

            if (visited.getOrDefault(pathEnd to dir, Long.MAX_VALUE) < score) continue
            visited[pathEnd to dir] = score
            if (this[pathEnd move dir] != '#') {
                queue.add(Triple(path + (pathEnd move dir), dir, score + 1))
            }
            queue.add(Triple(path, dir.turnRight(), score + 1000))
            queue.add(Triple(path, dir.turnLeft(), score + 1000))
        }
        return 0L
    }

}
