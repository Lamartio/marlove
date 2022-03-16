package io.lamart.rijksart.optics.async

import kotlinx.coroutines.flow.*

/**
 * The strategy is best understood through example: When the user is repeatedly clicking the download button and the download is slow and verifying in speed, what should happen with downloading happen with the download?
 * - Cancel current operation and start with the new one (latest)
 * - Allow all, but ensure that the result is in order with the clicks (concat)
 * - Allow all, but do not care about the order in which the results come in (merge)
 */

typealias AsyncStrategy<P, T> = Flow<P>.() -> Flow<Async<T>>

fun <P, T> latest(transform: suspend (payload: P) -> Flow<T>): AsyncStrategy<P, T> =
    { flatMapLatest(asyncFlowOf(transform)) }

fun <P, T> concat(transform: suspend (payload: P) -> Flow<T>): AsyncStrategy<P, T> =
    { flatMapConcat(asyncFlowOf(transform)) }

fun <P, T> merge(transform: suspend (payload: P) -> Flow<T>): AsyncStrategy<P, T> =
    { flatMapMerge(DEFAULT_CONCURRENCY, asyncFlowOf(transform)) }

private fun <P, T> asyncFlowOf(transform: suspend (payload: P) -> Flow<T>): suspend (payload: P) -> Flow<Async<T>> =
    { payload ->
        transform(payload)
            .map { result -> successOf(result) }
            .catch { reason -> emit(failureOf(reason)) }
            .onStart { emit(executing()) }
    }