package io.lamart.mocky.optics.async

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Creates a Flow of a suspending function
 */

fun <P, T> flowOfSuspending(transform: suspend (payload: P) -> T): suspend (payload: P) -> Flow<T> =
    { payload ->
        flow {
            emit(transform(payload))
        }
    }