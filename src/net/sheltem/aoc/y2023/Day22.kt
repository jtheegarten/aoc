package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.mapParallel

suspend fun main() {
    Day22().run()
}

class Day22 : Day<Long>(5, 7) {

    override suspend fun part1(input: List<String>): Long = input.map { it.toBrick() }.settle().canDisintegrate().count().toLong()

    override suspend fun part2(input: List<String>): Long = input.map { it.toBrick() }.settle().disintegrate().sum()

}

private suspend fun List<Brick>.disintegrate(): List<Long> =
    mapParallel { disBrick ->
        val stack = this.toMutableList()
        val removedBricks = mutableListOf(disBrick)
        stack.remove(disBrick)

        do {
            val noSupport = stack.filter { it.allSupportsIn(removedBricks) }
            stack.removeAll(noSupport)
            removedBricks.addAll(noSupport)
        } while (noSupport.isNotEmpty())
        removedBricks.size - 1L
    }

private fun List<Brick>.settle(): List<Brick> {
    val movedBricks = mutableListOf<Brick>()
    sortedBy { it.z.first }
        .map { brick ->
            var currentBrick = brick
            var supported = false
            while (!supported) {
                val supportingBricks = movedBricks.filter { currentBrick.under(it) }
                supported = supportingBricks.isNotEmpty() || currentBrick.z.first == 0
                if (supported) {
                    supportingBricks.forEach { it.connect(currentBrick) }
                    movedBricks += currentBrick
                } else {
                    val nextZ = movedBricks.filter { it.z.last < currentBrick.z.first - 1 }.maxOfOrNull { it.z.last }?.let { it + 1 } ?: 0
                    val height = currentBrick.z.last - currentBrick.z.first
                    currentBrick = currentBrick.copy(z = nextZ..(nextZ + height))
                }
            }
        }
    return movedBricks
}


private fun List<Brick>.canDisintegrate() = filter { !it.supports.any { supportedBricks -> supportedBricks.supportedBy.size == 1 } }


private fun String.toBrick(): Brick =
    split("~")
        .map { it.split(",").map { coord -> coord.toInt() } }
        .let { (left, right) ->
            Brick(left[0]..right[0], left[1]..right[1], left[2]..right[2])
        }

private data class Brick(val x: IntRange, val y: IntRange, val z: IntRange) {
    val supports = mutableListOf<Brick>()
    val supportedBy = mutableListOf<Brick>()

    fun putOnTop(other: Brick) {
        supports.add(other)
    }

    fun supported(other: Brick) {
        supportedBy.add(other)
    }

    fun connect(other: Brick) {
        this.putOnTop(other)
        other.supported(this)
    }

    fun under(other: Brick) = other.x.any{ this.x.contains(it) } && other.y.any { this.y.contains(it) } && other.z.last == this.z.first - 1
    fun allSupportsIn(bricks: List<Brick>) = supportedBy.isNotEmpty() && supportedBy.all { support -> bricks.contains(support) }
}
