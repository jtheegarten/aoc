package net.sheltem.common

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/*
The crossinline marker is used to mark lambdas that mustn’t allow non-local returns,
especially when such lambda is passed to another execution context such as a higher order function that is not inlined,
a local object or a nested function. In other words, you won’t be able to do a return in such lambdas.
 */
suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R): List<R> = coroutineScope {
    map { async { transform(it) } }.map { it.await() }
}
