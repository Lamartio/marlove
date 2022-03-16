package io.lamart.mocky.logic.utils

import kotlinx.coroutines.flow.*

/**
 * This is a generic way for for loading a datasource from persistence, then pull it from the network and last write it to persistence.
 */

fun <P, T : Any> getAndFetch(
    get: suspend (payload: P) -> T?,
    set: suspend (payload: P, value: T) -> Unit,
    fetch: suspend (payload: P) -> T,
    onError: suspend (error: Throwable) -> Unit = {},
): (payload: P) -> Flow<T> =
    { payload ->
        flowOf(Type.Get to payload, Type.Fetch to payload)
            .flatMapConcat { (type, payload) ->
                when (type) {
                    Type.Get -> resultFlowOf(type, payload, get)
                    Type.Fetch -> resultFlowOf(type, payload, fetch).onEachSuccess(payload, set)
                }
            }
            .throwWhenAllFailed()
            .onEachFailure(onError)
            .mapNotNull { (_, result) -> result.getOrNull() }
    }

private enum class Type {
    Get,
    Fetch;

    companion object {
        val values = values().toList()
    }
}

private typealias ResultFlow<T> = Flow<Pair<Type, Result<T>>>


private fun <P, T : Any> resultFlowOf(
    type: Type,
    payload: P,
    get: suspend (payload: P) -> T?,
): ResultFlow<T> =
    flowOf(payload)
        .map(get)
        .map { value ->
            when (value) {
                null -> type to Result.failure(NullPointerException("Failed to get value"))
                else -> type to Result.success(value)
            }
        }
        .catch { emit(type to Result.failure(it)) }

private fun <P, T> ResultFlow<T>.onEachSuccess(
    payload: P,
    set: suspend (payload: P, value: T) -> Unit,
): ResultFlow<T> =
    onEach { (_, result) -> result.onSuccess { runCatching { set(payload, it) } } }

private fun <T> ResultFlow<T>.throwWhenAllFailed(): ResultFlow<T> =
    this
        .scan(listOf<Pair<Type, Result<T>>>()) { previousList, value ->
            val list = previousList + value
            val containsGetAndFetch = list.map { (type) -> type }.containsAll(Type.values)
            val areAllFailure = list.all { (_, result) -> result.isFailure }

            if (containsGetAndFetch and areAllFailure) {
                throw list
                    .mapNotNull { (_, result) -> result.exceptionOrNull() }
                    .let(::GetAndFetchException)
            }

            list
        }
        .mapNotNull { list -> list.lastOrNull() }

private fun <T> ResultFlow<T>.onEachFailure(block: suspend (Throwable) -> Unit): ResultFlow<T> =
    onEach { (_, result) -> result.onFailure { runCatching { block(it) } } }

data class GetAndFetchException(val reasons: List<Throwable>) :
    Throwable(reasons.firstOrNull())
