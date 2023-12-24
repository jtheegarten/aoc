package net.sheltem.aoc.y2023

suspend fun main() {
    Day24().run()
}

class Day24 : Day<Long>(0, 47) {

    override suspend fun part1(input: List<String>) = input.map { it.toHailstone() }.intersections()

    override suspend fun part2(input: List<String>) = input.take(4).map { it.toHailstone() }.findStone().let { println(it); it.pos.x + it.pos.y + it.pos.z }
}

// We would have to solve
// x + dx * t = other.x + other.dx * t
// y + dy * t = other.y + other.dy * t
// z + dz * t = other.z + other.dz * t
// for 3 different hailstones
private fun List<Hailstone>.findStone(): Hailstone {
    val testRange = 0..50
    val testSpeeds = 20 downTo -20
    testSpeeds.forEach { dx ->
        testSpeeds.forEach { dy ->
            testSpeeds.forEach { dz ->
                testRange.forEach { x ->
                    testRange.forEach { y ->
                        testRange.forEach { z ->
                            if (dx == 0 || dy == 0 || dz == 0) {

                            } else {
                                val current = Hailstone(Position3D(x, y, z), Vector(dx, dy, dz))
//                            println("$x $y $z | $dx $dy $dz")
                                if (this.all { it.fullIntersects(current, 0, Long.MAX_VALUE) }) {
                                    return current
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    TODO()
}

private fun List<Hailstone>.intersections(minTime: Long = 200000000000000, maxTime: Long = 400000000000000): Long {
    val checked = mutableSetOf<Hailstone>()
    return this.sumOf { stone ->
        checked.add(stone)
        (this - checked).count { other ->
            stone.intersects(other, minTime, maxTime)
        }.toLong()
    }
}

private fun String.toHailstone(): Hailstone = split(" @ ").let { (position, speed) ->
    val pos = position.replace(" ", "").split(",").map { it.toLong() }.let { (x, y, z) -> Position3D(x, y, z) }
    val vec = speed.replace(" ", "").split(",").map { it.toInt() }.let { (dx, dy, dz) -> Vector(dx, dy, dz) }
    Hailstone(pos, vec)
}

data class Hailstone(val pos: Position3D, val speed: Vector) {
    private val slope = speed.dy.toDouble() / speed.dx.toDouble()

    fun intersects(other: Hailstone, minTime: Long, maxTime: Long): Boolean {
        val stoneSlope = this.slope
        val otherSlope = other.slope
        val commonX = ((otherSlope * other.pos.x) - (stoneSlope * this.pos.x) + this.pos.y - other.pos.y) / (otherSlope - stoneSlope)
        val commonY = (stoneSlope * (commonX - this.pos.x)) + this.pos.y

        return commonX > minTime && commonX < maxTime && commonY > minTime && commonY < maxTime && this.project(commonX, commonY) && other.project(
            commonX,
            commonY
        )
    }

    fun fullIntersects(other: Hailstone, minTime: Long, maxTime: Long): Boolean =
        this.intersects(other, minTime, maxTime)
                && this.rotate().intersects(other.rotate(), minTime, maxTime)
                && this.rotate().rotate().intersects(other.rotate().rotate(), minTime, maxTime)

    private fun rotate(): Hailstone = Hailstone(pos.rotate(), speed.rotate())

    private fun project(futureX: Double, futureY: Double) =
        !(speed.dx < 0 && pos.x < futureX
                || speed.dx > 0 && pos.x > futureX
                || speed.dy < 0 && pos.y < futureY
                || speed.dy > 0 && pos.y > futureY)

}

data class Position3D(val x: Long, val y: Long, val z: Long) {
    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    fun rotate(): Position3D = Position3D(z, x, y)
}

data class Vector(val dx: Int, val dy: Int, val dz: Int) {
    fun rotate(): Vector = Vector(dz, dx, dy)
}
