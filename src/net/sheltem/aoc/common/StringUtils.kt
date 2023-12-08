package net.sheltem.aoc.common

import java.io.File

val numericRegex = Regex("\\d+")

fun String.count(s: String) = this.count { it == s[0] }
fun String.lastAsInt(delimiter: String) = this.split(delimiter).last().toInt()

const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"

fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun Collection<MatchResult>.toListLong(): List<Long> = mapNotNull { number -> number.value.toLong() }
fun Sequence<MatchResult>.toListLong(): List<Long> = toList().toListLong()
fun String.regex(r: String) = Regex(r).findAll(this).map { it.value }.toList()
fun String.regexNumbers() = numericRegex.findAll(this).map { it.value }.mapNotNull { it.toLongOrNull() }.toList()
