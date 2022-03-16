package io.lamart.mocky.optics.async

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.withIndex

typealias Collector<T> = (flow: Flow<Async<T>>, next: (Async<T>) -> Unit) -> Unit

fun <T> collectorOf(scope: CoroutineScope): Collector<T> =
    { flow, next -> scope.launch { flow.collect(next) } }

fun <T> CoroutineScope.finiteCollectorOf(job: Job, limit: Int): Collector<T> {
    return { flow, next ->
        val scope = this + job
        scope.launch {
            flow.cancellable().withIndex().collect { (index, value) ->
                next(value)
                // Usually the flow should signal the scope it is finished by limiting the Flow with Flow.take(), but it seems to be instable. Hence this workaround.
                if (index == limit - 1)
                    scope.cancel()
            }
        }
    }
}