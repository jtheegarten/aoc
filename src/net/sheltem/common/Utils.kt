package net.sheltem.common

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/*
The crossinline marker is used to mark lambdas that mustn’t allow non-local returns,
especially when such lambda is passed to another execution context such as a higher order function that is not inlined,
a local object or a nested function. In other words, you won’t be able to do a return in such lambdas.
 */
suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R): List<R> = coroutineScope {
    map { async { transform(it) } }.map { it.await() }
}
suspend fun <T> Iterable<T>.filterParallel(predicate: suspend (T) -> Boolean): List<T> = coroutineScope {
    map { async { it.takeIf { predicate(it) } } }.awaitAll().filterNotNull()
}
suspend inline fun <T> Iterable<T>.countParallel(crossinline predicate: (T) -> Boolean): Int = coroutineScope {
    map { async { predicate(it) } }.count { it.await() }
}
suspend inline fun <T> Iterable<T>.sumOfParallel(crossinline transform: (T) -> Long): Long = coroutineScope {
    map { async { transform(it) } }.sumOf { it.await() }
}
suspend inline fun <T> Iterable<T>.forEachParallel(crossinline action: (T) -> Unit): Unit = coroutineScope {
    map { async { action(it) } }.awaitAll()
}
