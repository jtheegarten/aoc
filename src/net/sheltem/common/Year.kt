package net.sheltem.common

import java.io.File
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.writeText
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime


open class Year(
    private val year: Int = 2024,
    private val days: List<Pair<String, Day<out Any>>>
) {

    suspend fun run() {

        val results = days
            .map { Triple(it.first, it.second, File("src/net/sheltem/aoc/y$year/data/Day${it.first}.txt").readLines()) }
            .map { (day, dayClass, input) ->
                Triple(day, measureTime { dayClass.part1(input) }, measureTime { dayClass.part2(input) })
            }

        val formattedResults = results.map { (day, resultA, resultB) ->
            listOf(day, resultA.formatted(), resultB.formatted(), (resultA + resultB).formatted())
        }

        val markdownTable = buildString {

            val maxWidths = formattedResults.fold(listOf(0, 0, 0, 0)) { acc, row ->
                acc.zip(row.map { it.length }) { a, b -> maxOf(a, b) }
            }

            val headersLine =
                listOf("Day", "Part A", "Part B", "Total")
                    .zip(maxWidths) { header, width -> header.padStart(width) }
                    .joinToString(" | ", prefix = "| ", postfix = " |")
            val separatorLine =
                maxWidths.joinToString("|", prefix = "|", postfix = "|") { "-".repeat(it + 1) + ":" }

            appendLine(headersLine)
            appendLine(separatorLine)

            formattedResults.forEach { (day, timeA, timeB, total) ->
                val dayPadded = day.padEnd(maxWidths[0])
                val timeAPadded = timeA.padStart(maxWidths[1])
                val timeBPadded = timeB.padStart(maxWidths[2])
                val totalPadded = total.padStart(maxWidths[3])

                appendLine("| $dayPadded | $timeAPadded | $timeBPadded | $totalPadded |")
            }
        }

        val totalTime = results.fold(0.seconds) { acc, (_, timeA, timeB) ->
            acc + timeA + timeB
        }

        println("Total time: $totalTime")

        val outputPath = Path("TIMES$year.MD")

        outputPath.writeText("$markdownTable\n\nTotal time: $totalTime\n")
    }
}

private fun Duration.formatted(): String {
    val seconds = inWholeMicroseconds.toDouble() / 1000_000.0
    return "%.4fs".format(Locale.US, seconds)
}
