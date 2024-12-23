package net.sheltem.aoc.y2024

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
    "12" to Day12(),
    "13" to Day13(),
    "14" to Day14(),
    "15" to Day15(),
    "16" to Day16(),
    "17" to Day17(),
    "18" to Day18(),
    "19" to Day19(),
    "20" to Day20(),
    "21" to Day21(),
    "22" to Day22(),
    "23" to Day23(),
//    "24" to Day24(),
//    "25" to Day25(),
)

suspend fun main() {
    Year2024().run()
}

class Year2024: Year(2024, days)
