package net.sheltem.ec.events.algorithmia

import kotlin.math.abs

suspend fun main() {
    Quest08().run()
}

class Quest08 : AlgorithmiaQuest<Long>(listOf(21, 19342085, 8)) {

    override suspend fun part1(input: List<String>): Long = input[0]
        .toLong()
        .buildPyramid()

    override suspend fun part2(input: List<String>): Long = input[0]
        .toLong()
        .buildShrine()

    override suspend fun part3(input: List<String>): Long = 23


    private fun Long.buildPyramid(): Long {
        var blocksRemaining = this - 1
        val pyramid = mutableListOf(1L)

        while (true) {
            pyramid.replaceAll { (it + 2).also { blocksRemaining -= 2} }
            pyramid += 1L
            blocksRemaining--
            if (blocksRemaining <= 0) {
                return pyramid[0] * abs(blocksRemaining)
            }
        }
    }

    private fun Long.buildShrine(): Long {
        val priests = this
        var lastThickness = 1L
        val moduloFactor = 1111
        var blocksRemaining = 20240000L

        val shrine = mutableListOf(1L)

        while(true) {
            lastThickness = (lastThickness * priests) % moduloFactor
            shrine.replaceAll{ (it + lastThickness).also { blocksRemaining -= lastThickness } }
            repeat (2) {
                shrine.add(lastThickness)
                blocksRemaining -= lastThickness
            }
            if (blocksRemaining <= 0) {
                return shrine.size * abs(blocksRemaining - 1)
            }
        }
    }
}
