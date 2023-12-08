package net.sheltem.aoc.common

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R): List<R> = coroutineScope {
    map { async { transform(it) } }.map { it.await() }
}
