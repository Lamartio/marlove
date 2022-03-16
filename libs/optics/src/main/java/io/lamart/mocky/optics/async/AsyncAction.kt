package io.lamart.mocky.optics.async

import io.lamart.mocky.optics.OptionalSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.consumeAsFlow

fun <P, T : Any> OptionalSource<Async<T>>.toAsyncAction(
    strategy: AsyncStrategy<P, T>,
    scope: CoroutineScope,
): (payload: P) -> Unit =
    toAsyncAction(strategy, collectorOf(scope))

/**
 * Maps a source into a action (a function that returns Unit) that will mutate according to the behavior given to the strategy.
 */

fun <P, T : Any> OptionalSource<Async<T>>.toAsyncAction(
    strategy: AsyncStrategy<P, T>,
    collectOn: Collector<T>,
): (payload: P) -> Unit {
    val flow = Channel<P>(UNLIMITED)

    flow
        .consumeAsFlow()
        .strategy()
        .let { collectOn(it, this@toAsyncAction::set) }

    return { payload ->
        flow.trySend(payload)
    }
}
