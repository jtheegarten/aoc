package net.sheltem.aoc

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.FILE
import platform.posix.fclose
import platform.posix.fgets

fun CValuesRef<FILE>.readLines(): List<String> {
    var returnBuffer = listOf<String>()
    try {
        memScoped {
            val readBufferLength = 64 * 10240
            val buffer = allocArray<ByteVar>(readBufferLength)

            returnBuffer = generateSequence { fgets(buffer, readBufferLength, this@readLines)?.toKString() }.takeWhile { it != null }.toList()
        }

    } finally {
        fclose(this)
    }
    return returnBuffer
}

fun String.lastAsInt(delimiter: String) = this.split(delimiter).last().toInt()

fun <K, V> MutableMap<K, V>.putIfAbsent(key: K, value: V) = apply { if (this[key] == null) put(key, value) }
