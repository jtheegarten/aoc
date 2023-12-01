package net.sheltem.aoc.y2016

import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8


suspend fun main() {
    Day05().run()
}

class Day05 : Day<String>("18f47a30", "05ace8e3") {

    override suspend fun part1(input: List<String>): String {
        return input[0].toPassword()
    }

    override suspend fun part2(input: List<String>): String {
        return input[0].toAdvancedPassword()
    }
}

private fun String.toAdvancedPassword(): String {
    val result = mutableMapOf<Long, String>()
    for (index in 0..Long.MAX_VALUE) {
        hashIndex(this@toAdvancedPassword, index)?.let { hex ->
            val pwIndex = hex[5].toString().toLongOrNull()?.let { if (it in 0..7) it else null }
            if (pwIndex != null && !result.contains(pwIndex)) {
                result[pwIndex] = hex[6].toString()
                if (result.size == 8) {
                    return result.entries.sortedBy { it.key }.joinToString("") { it.value }
                }
            }
        }
    }
    return result.values.joinToString("")
}

private fun String.toPassword(): String =
    generateSequence(0L, Long::inc)
        .mapNotNull { hashIndex(this@toPassword, it) }
        .take(8)
        .map { it[5] }
        .joinToString("") { it.toString() }

private fun hashIndex(word: String, index: Long): String? {
    val hex = (word + index).md5().toHex()
    return if (hex.startsWith("00000")) {
        hex
    } else {
        null
    }
}

private fun String.md5() = MessageDigest.getInstance("MD5").digest(this.toByteArray(UTF_8))
private fun ByteArray.toHex() = joinToString("") { byte -> "%02x".format(byte) }
