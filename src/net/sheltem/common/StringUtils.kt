package net.sheltem.common

import java.io.File

val numericRegex = Regex("-?\\d+")

fun String.count(s: String) = this.count { it == s[0] }
fun String.lastAsInt(delimiter: String) = this.split(delimiter).last().toInt()

const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"

fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun Collection<MatchResult>.toListLong(): List<Long> = mapNotNull { number -> number.value.toLong() }
fun List<String>.rotateCockwise(): List<String> = this.let { list ->
    (0 until list[0].length)
        .map { i ->
            list.map { it[i] }.joinToString("")
        }
}

fun Sequence<MatchResult>.toListLong(): List<Long> = toList().toListLong()

fun String.regex(r: Regex) = r.findAll(this).map { it.value }.toList()
fun String.regex(r: String) = Regex(r).findAll(this).map { it.value }.toList()
fun String.regexNumbers() = numericRegex.findAll(this).map { it.value }.mapNotNull { it.toLongOrNull() }.toList()
fun String.splitInHalf(): List<String> = (length / 2).let { mid -> listOf(this.take(mid), this.drop(mid)) }

const val RESET = "\u001B[0m"
const val BLACK = "\u001B[30m"
const val GREEN_BACKGROUND = "\u001B[42m"
const val BLUE_BACKGROUND = "\u001B[44m"
const val RED_BACKGROUND = "\u001B[41m"

val Any.inGreen get() = BLACK + GREEN_BACKGROUND + this + RESET
val Any.inBlue get() = BLACK + BLUE_BACKGROUND + this + RESET
val Any.inRed get() = BLACK + RED_BACKGROUND + this + RESET
