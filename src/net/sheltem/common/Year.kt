package net.sheltem.common

import java.io.File
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.writeText
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration


open class Year(
    private val year: Int = 2024,
    private val days: List<Pair<String, Day<out Any>>>
) {

    suspend fun run() {

        val results = days
            .map { Triple(it.first, it.second, File("src/net/sheltem/aoc/y$year/data/Day${it.first}.txt").readLines()) }
            .map { (day, dayClass, input) ->
                Triple(day, List(10) { measureTime { dayClass.part1(input) } }, List(10) { measureTime { dayClass.part2(input) } })
            }

        val formattedResults = results.map { (day, resultA, resultB) ->
            listOf(day, resultA.min().formatted(), resultA.avg().formatted(), resultA.max().formatted(), resultB.min().formatted(), resultB.avg().formatted(), resultB.max().formatted(), (resultA.min() + resultB.min()).formatted(), (resultA.avg() + resultB.avg()).formatted(), (resultA.max() + resultB.max()).formatted())
        }

        val markdownTable = buildString {

            val maxWidths = formattedResults.fold(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)) { acc, row ->
                acc.zip(row.map { it.length }) { a, b -> maxOf(a, b) }
            }

            val headersLine =
                listOf("Day", "Part A min", "avg", "max", "Part B min", "avg", "max", "Total min", "avg", "max")
                    .zip(maxWidths) { header, width -> header.padStart(width) }
                    .joinToString(" | ", prefix = "| ", postfix = " |")
            val separatorLine =
                maxWidths.joinToString("|", prefix = "|", postfix = "|") { "-".repeat(it + 1) + ":" }

            appendLine(headersLine)
            appendLine(separatorLine)

            formattedResults.forEach {
                val dayPadded = it[0].padEnd(maxWidths[0])
                val timeAMinPadded = it[1].padStart(maxWidths[1])
                val timeAAvgPadded = it[2].padStart(maxWidths[2])
                val timeAMaxPadded = it[3].padStart(maxWidths[3])
                val timeBMinPadded = it[4].padStart(maxWidths[4])
                val timeBAvgPadded = it[5].padStart(maxWidths[5])
                val timeBMaxPadded = it[6].padStart(maxWidths[6])
                val totalMinPadded = it[7].padStart(maxWidths[7])
                val totalAvgPadded = it[8].padStart(maxWidths[8])
                val totalMaxPadded = it[9].padStart(maxWidths[9])

                appendLine("| $dayPadded | $timeAMinPadded | $timeAAvgPadded | $timeAMaxPadded | $timeBMinPadded | $timeBAvgPadded | $timeBMaxPadded | $totalMinPadded | $totalAvgPadded | $totalMaxPadded |")
            }
        }



        val totalTimeMin = results.fold(0.seconds) { acc, (_, timeA, timeB) ->
            acc + timeA.min() + timeB.min()
        }

        val totalTimeAvg = results.fold(0.seconds) { acc, (_, timeA, timeB) ->
            acc + timeA.avg() + timeB.avg()
        }

        val totalTimeMax = results.fold(0.seconds) { acc, (_, timeA, timeB) ->
            acc + timeA.max() + timeB.max()
        }

        println("Total time: $totalTimeMin (min), $totalTimeAvg (avg), $totalTimeMax (max)")

        val outputPath = Path("TIMES$year.MD")

        outputPath.writeText("$markdownTable\n\n\"Total time: $totalTimeMin (min), $totalTimeAvg (avg), $totalTimeMax (max)\"\n")
    }
}

private fun List<Duration>.avg(): Duration = (sumOf { it.inWholeMicroseconds } / size).toDuration(DurationUnit.MICROSECONDS)

private fun Duration.formatted(): String {
    val seconds = inWholeMicroseconds.toDouble() / 1000_000.0
    return "%.4fs".format(Locale.US, seconds)
}
