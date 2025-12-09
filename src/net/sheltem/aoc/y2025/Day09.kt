package net.sheltem.aoc.y2025

import net.sheltem.common.PositionInt
import net.sheltem.common.distX
import net.sheltem.common.distY
import net.sheltem.common.minX
import net.sheltem.common.minY
import net.sheltem.common.x
import net.sheltem.common.y
import java.awt.Polygon
import java.awt.Rectangle
import java.awt.geom.Area
import kotlin.math.abs


suspend fun main() {
    Day09().run()
}

class Day09 : Day<Long>(50, 24) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.toPositionInt() }
        .maxContainedArea(true)

    override suspend fun part2(input: List<String>): Long = input
        .map { it.toPositionInt() }
        .maxContainedArea()

    private fun String.toPositionInt() = split(",").let { (left, right) -> left.toInt() to right.toInt() }

    private fun List<PositionInt>.maxContainedArea(ignorePolygon: Boolean = false): Long =
        flatMapIndexed { i, start ->
            drop(i + 1).map { end ->
                Rectangle((start minX end), (start minY end), abs(start distX end), abs(start distY end))
            }
        }.let { list ->
            val polygon = Area(Polygon().also { polygon -> this.forEach { point -> polygon.addPoint(point.x, point.y) } })
            list.filter { ignorePolygon || polygon.contains(it) }
        }.maxOf { ((it.width + 1) * (it.height + 1)).toLong() }

//    private fun List<PositionInt>.maxArea(): Long =
//        map { start ->
//            (this - start).map { end ->
//                start to end
//            }.maxBy { it.area() }
//        }.maxBy { it.area() }
//            .area()

}

// Polygon > 1550582096
// Area    > 1624057680
// >:C
