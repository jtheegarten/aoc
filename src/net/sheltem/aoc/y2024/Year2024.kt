package net.sheltem.aoc.y2024

import net.sheltem.common.Year

private val days = listOf(
    "01" to Day01(),
    "02" to Day02(),
    "03" to Day03(),
    "04" to Day04(),
    "05" to Day05(),
)

suspend fun main() {
    Year2024().run()
}

class Year2024: Year(2024, days)
