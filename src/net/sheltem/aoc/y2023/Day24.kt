package net.sheltem.aoc.y2023


suspend fun main() {
    Day24().run()
}

class Day24 : Day<Long>(0, 47) {

    override suspend fun part1(input: List<String>) = input.map { it.toHailstone() }.intersections()

    override suspend fun part2(input: List<String>) = input.map { it.toHailstone() }.findStone()
}

private fun List<Hailstone>.findStone(): Long {
    val h1 = this@findStone[0]
    val h2 = this@findStone[1]

    val testSpeeds = 350 downTo -350L


    testSpeeds.forEach { dx ->
        testSpeeds.forEach { dy ->
            testSpeeds.forEach { dz ->
                if (dx != 0L && dy != 0L && dz != 0L) {

                    val A: Long = h1.pos.x
                    val a: Long = h1.speed.dx - dx
                    val B: Long = h1.pos.y
                    val b: Long = h1.speed.dy - dy
                    val C: Long = h2.pos.x
                    val c: Long = h2.speed.dx - dx
                    val D: Long = h2.pos.y
                    val d: Long = h2.speed.dy - dy

                    if (c != 0L && (a * d) - (b * c) != 0L) {
                        val t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c))

                        val x = h1.pos.x + h1.speed.dx * t - dx * t
                        val y = h1.pos.y + h1.speed.dy * t - dy * t
                        val z = h1.pos.z + h1.speed.dz * t - dz * t

                        val hitall = this@findStone.none { h ->
                            val u = when {
                                h.speed.dx.toLong() != dx -> (x - h.pos.x) / (h.speed.dx - dx)
                                h.speed.dy.toLong() != dy -> (y - h.pos.y) / (h.speed.dy - dy)
                                h.speed.dz.toLong() != dz -> (z - h.pos.z) / (h.speed.dz - dz)
                                else -> TODO()
                            }
                            (x + u * dx != h.pos.x + u * h.speed.dx)
                                    || (y + u * dy != h.pos.y + u * h.speed.dy)
                                    || (z + u * dz != h.pos.z + u * h.speed.dz)
                        }
                        if (hitall) return x + y + z
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
