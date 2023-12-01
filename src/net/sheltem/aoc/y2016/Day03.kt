package net.sheltem.aoc.y2016

suspend fun main() {
    Day03().run()
}

class Day03 : Day<Int>(3, 4) {

    override suspend fun part1(input: List<String>): Int {
        return input.map { it.toTriangleStringList() }.map { Triangle.from(it) }.count { it.isValid() }
    }

    override suspend fun part2(input: List<String>): Int {
        return input.map { it.toTriangleStringList() }.windowed(3, 3).zipColumns().flatten().map { Triangle.from(it) }.count { it.isValid() }
    }

    class Triangle(val a: Int, val b: Int, val c: Int) {

        fun isValid() = listOf(a, b, c).sorted().take(2).sum() > maxOf(a, b, c)

        override fun toString() = "$a $b $c"

        companion object {
            fun from(sidesList: List<String>) = Triangle(sidesList[0].toInt(), sidesList[1].toInt(), sidesList[2].toInt())
        }
    }

    private fun List<List<List<String>>>.zipColumns() = map {
        listOf(
            listOf(
                it[0][0],
                it[1][0],
                it[2][0],
            ),
            listOf(
                it[0][1],
                it[1][1],
                it[2][1],
            ),
            listOf(
                it[0][2],
                it[1][2],
                it[2][2],
            )
        )
    }

    private fun String.toTriangleStringList() = replace("\\s+".toRegex(), " ").trim().split(" ").map { it.trim() }
}
