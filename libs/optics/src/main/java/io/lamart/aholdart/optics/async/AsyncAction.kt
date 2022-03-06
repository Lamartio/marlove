package io.lamart.aholdart.optics.async

import io.lamart.aholdart.optics.OptionalSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.consumeAsFlow


fun <P, T : Any> OptionalSource<Async<T>>.bla(): Unit {

}

fun <P, T : Any> OptionalSource<Async<T>>.toAsyncAction(
    strategy: AsyncStrategy<P, T>,
    scope: CoroutineScope,
): (payload: P) -> Unit =
    toAsyncAction(strategy, collectorOf(scope))

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