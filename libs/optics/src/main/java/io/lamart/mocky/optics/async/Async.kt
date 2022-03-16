package io.lamart.mocky.optics.async

sealed class Async<T>(
    open val reason: Throwable?,
    open val result: T?,
) {

    class Initial<T> : Async<T>(null, null)

    class Executing<T> : Async<T>(null, null)

    data class Failure<T>(override val reason: Throwable) : Async<T>(reason, null)

    data class Success<T>(override val result: T) : Async<T>(null, result)

}

fun <T> initial(): Async<T> =
    Async.Initial()

fun <T> executing(): Async<T> =
    Async.Executing()

fun <T> failureOf(reason: Throwable): Async<T> =
    Async.Failure(reason)

fun <T> successOf(result: T): Async<T> =
    Async.Success(result)