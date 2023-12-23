package net.sheltem.aoc.y2016

import net.sheltem.common.ALPHABET

suspend fun main() {
    Day04().run(true)
}

class Day04 : Day<Int>(1514, 4) {

    override suspend fun part1(input: List<String>): Int {
        return input.map { Room.from(it) }.filter { it.isValid() }.sumOf { it.id }
    }

    override suspend fun part2(input: List<String>): Int {
        return input.map { Room.from(it, true) }.first { it.name.contains("northpole") }.id
    }

    class Room(val name: String, val id: Int, val checksum: String) {

        override fun toString() = "$name: $id - $checksum"

        fun isValid(): Boolean = name
            .groupingBy { it }
            .eachCount()
            .entries
            .asSequence()
            .sortedBy { it.key }
            .sortedByDescending { it.value }
            .map { it.key }
            .take(5)
            .joinToString("", "") == checksum

        companion object {
            fun from(roomString: String, shift: Boolean = false): Room {
                val name = roomString.substring(0, roomString.indexOfFirst { it.isDigit() }).replace("-", "")
                val id = roomString.substring(roomString.indexOfFirst { it.isDigit() }, roomString.indexOfFirst { it == '[' }).toInt()
                val checksum = roomString.substring(roomString.indexOfFirst { it == '[' } + 1).dropLast(1)

                return Room(if (shift) name.shift(id) else name, id, checksum)
            }
        }
    }
}

private fun String.shift(id: Int) = map { ALPHABET[(ALPHABET.indexOf(it) + id).mod(26)] }.joinToString("")
