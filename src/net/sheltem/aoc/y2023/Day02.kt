package net.sheltem.aoc.y2023

import kotlin.math.max

suspend fun main() {
    Day02().run()
}

private val maxCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)

class Day02 : Day<Long>(8, 2286) {

    override suspend fun part1(input: List<String>): Long = input
        .mapIndexed { index, s -> (index + 1) to s.toMinCubeMap() }
        .filterNot { setToTest -> maxCubes.any { setToTest.second[it.key]!! > it.value } }
        .sumOf { it.first }.toLong()

    override suspend fun part2(input: List<String>): Long = input
        .map{ it.toMinCubeMap() }
        .sumOf {
            it.values.reduce { accumulator, element ->
                accumulator * element
            }
        }
}

private fun String.toMinCubeMap(): Map<String, Long> =
    this.split(": ")
        .last()
        .split("; ")
        .flatMap { cubeSet ->
            cubeSet
                .split(", ")
                .map { cube ->
                    cube
                        .split(" ")
                        .let { it.component2() to it.component1().toLong() }
                }
        }//.also { println(it) }
        .groupingBy { it.first }
        .aggregate { _, accumulator: Long?, element, first ->
            if (first) {
                element.second
            } else {
                max(accumulator!!, element.second)
            }
        }//.also { println(it) }
