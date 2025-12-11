package net.sheltem.aoc.y2025

import net.sheltem.common.Year

private val days = listOf(
    "01" to Day01(),
    "02" to Day02(),
    "03" to Day03(),
    "04" to Day04(),
    "05" to Day05(),
    "06" to Day06(),
    "07" to Day07(),
    "08" to Day08(),
    "09" to Day09(),
    "10" to Day10(),
    "11" to Day11(),
//    "12" to Day12(),
)

suspend fun main() {
    Year2025().run()
}

class Year2025 : Year(2025, days)
