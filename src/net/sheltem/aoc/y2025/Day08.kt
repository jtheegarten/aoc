package net.sheltem.aoc.y2025

import Point3D
import distance
import net.sheltem.common.multiply
import sum
import toPoint3D
import kotlin.math.roundToInt


suspend fun main() {
    Day08().run()
}

class Day08 : Day<Long>(40, 25272) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.toPoint3D() }
        .connect()

    override suspend fun part2(input: List<String>): Long = input
        .map { it.toPoint3D() }
        .connectAll()

    private fun List<Point3D>.connect(): Long {
        val connectionsToMake = if (this.size < 1000) 10 else 1000
        val circuits = this.map { setOf(it) }.toMutableList()
        val mesh = this.fullMesh().sortedBy { (a, b) -> a.distance(b).roundToInt() }

        var i = 0
        repeat (connectionsToMake) {
            var connection = mesh[i]
            if (circuits.any { it.contains(connection.first) && it.contains(connection.second) }) {
                connection = mesh[++i]
            }
            circuits.merge(connection.first, connection.second)
        }

        return circuits.map { it.size.toLong() }.sortedDescending().take(3).multiply()
    }

    private fun List<Point3D>.connectAll(): Long {
        val circuits = this.map { setOf(it) }.toMutableList()
        val mesh = this.fullMesh().sortedBy { (a, b) -> a.distance(b).roundToInt() }

        var i = 0
        while (true) {
            var connection = mesh[i]
            while (circuits.any { it.contains(connection.first) && it.contains(connection.second) }) {
                connection = mesh[++i]
            }
            circuits.merge(connection.first, connection.second)
            if (circuits.size == 1) {
                return connection.first.first * connection.second.first
            }
        }
    }

    private fun List<Point3D>.fullMesh() = flatMap { point ->
        (this - setOf(point)).map { if (point.sum() <= it.sum()) point to it else it to point }
    }.toSet()

    fun MutableList<Set<Point3D>>.merge(a: Point3D, b: Point3D) = apply {
        val aSet = this.single { it.contains(a) }
        val bSet = this.single { it.contains(b) }
        this.remove(aSet)
        this.remove(bSet)
        this.add(aSet + bSet)
    }
}
