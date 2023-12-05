package net.sheltem.aoc.y2023

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun main() {
    Day05().run()
}


class Day05 : Day<Long>(35, 46) {

    override suspend fun part1(input: List<String>): Long = input.toAlmanac().minLocationNumber(input[0].toSeedList())

    override suspend fun part2(input: List<String>): Long {
        val almanac = input.toAlmanac()
        val ranges = input[0].toSeedList().chunked(2).map { it.first()..(it.first() + it.last()) }

        return coroutineScope {
            generateSequence(0L) { it + 1 }
                .first { result ->
                    almanac.resultToSeed(result)
                        .let { seed ->
                            ranges.any { it.contains(seed) }
                        }
                }
        }
    }
}

private fun String.toSeedList() = split(": ").last().split(" ").mapNotNull { it.toLongOrNull() }

private fun List<String>.toAlmanac(): Almanac {
    val almanac = this.subList(2, this.size).joinToString("\n").split("\n\n").map { xToYMap ->
        xToYMap.split("\n").drop(1).map(AlmanacMapping::from)
    }
    return Almanac(almanac.map(::AlmanacSection))
}

private fun List<AlmanacSection>.findMapping(mappingNumber: Long) =
    fold(mappingNumber) { acc, almanacMapping -> almanacMapping.map(acc) }

private fun List<AlmanacSection>.findReverseMapping(result: Long) =
    reversed().fold(result) { acc, almanacMapping -> almanacMapping.reverseMap(acc) }

private data class Almanac(val mappings: List<AlmanacSection>) {
    fun minLocationNumber(seeds: List<Long>) =
        seeds.minOf { seed ->
            mappings.findMapping(seed)
        }

    fun resultToSeed(result: Long): Long =
        mappings.findReverseMapping(result)

}

private data class AlmanacSection(
    val mappings: List<AlmanacMapping>
) {
    fun map(number: Long): Long = mappings.find { it.canMap(number) }?.map(number) ?: number
    fun reverseMap(number: Long): Long = mappings.find { it.canReverseMap(number) }?.reverseMap(number) ?: number
}

private data class AlmanacMapping(val target: Long, val start: Long, val size: Long) {

    fun canMap(mappingNumber: Long) = (mappingNumber >= start && mappingNumber < (start + size))
    fun canReverseMap(mappingNumber: Long) = (mappingNumber >= target && mappingNumber < (target + size))
    fun map(mappingNumber: Long) = mappingNumber + (target - start)
    fun reverseMap(mappingNumber: Long) = mappingNumber - (target - start)

    companion object {
        fun from(input: String) = input.split(" ").mapNotNull { it.toLongOrNull() }.let { (target, start, size) -> AlmanacMapping(target, start, size) }
    }
}
