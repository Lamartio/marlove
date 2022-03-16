package io.lamart.rijksart.logic

/**
 * The 'suspending' means that it spies suspend functions. This is necessary for overloading
 * The '1' in the name refers to 1 argument. This is necessary for overloading
 *
 * @return a lambda and a list with the results of the lambda
 */

fun < R> suspendingLambdaSpy(block: suspend () -> R): Pair<List< Result<R>>, suspend () -> R> {
    val list = mutableListOf<Result<R>>()
    val lambda: suspend () -> R = {
        runCatching { block() }
            .also(list::add)
            .getOrThrow()
    }

    return list to lambda
}

fun <A, R> suspendingLambdaSpy1(block: suspend (A) -> R): Pair<List<Pair<A, Result<R>>>, suspend (A) -> R> {
    val list = mutableListOf<Pair<A, Result<R>>>()
    val lambda: suspend (A) -> R = { a ->
        runCatching { block(a) }
            .also { list.add(a to it) }
            .getOrThrow()
    }

    return list to lambda
}

fun <A, B, R> suspendingLambdaSpy2(block: suspend (A, B) -> R): Pair<List<Triple<A, B, Result<R>>>, suspend (A, B) -> R> {
    val list = mutableListOf<Triple<A, B, Result<R>>>()
    val lambda: suspend (A, B) -> R = { a, b ->
        runCatching { block(a, b) }
            .also { list.add(Triple(a, b, it)) }
            .getOrThrow()
    }

    return list to lambda
}