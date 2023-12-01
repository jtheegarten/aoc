package net.sheltem.aoc.y2015

import java.security.MessageDigest

suspend fun main() {
    Day04().run(true)
}

class Day04 : Day<Long>(609043, 3) {
    override suspend fun part1(input: List<String>): Long = input.first().findMD5()

    override suspend fun part2(input: List<String>): Long = input.first().findMD5(6)
}

private fun String.findMD5(leadingZeros: Int = 5): Long {
    val digest = MessageDigest.getInstance("MD5")
    var i = 0L
    while (true) {
        i++
        val numberString = i.toString().let { "0".repeat((leadingZeros + 1) - it.length) + it }
        val md5 = digest.digest((this + numberString).toByteArray()).toHex()
        if (md5.startsWith("0".repeat(leadingZeros))) break
        if (i == Long.MAX_VALUE) {
            println("WHAAAAAAAAAAAT")
            break
        }
    }
    return i
}

private fun ByteArray.toHex() = joinToString("") { byte -> "%02x".format(byte) }
