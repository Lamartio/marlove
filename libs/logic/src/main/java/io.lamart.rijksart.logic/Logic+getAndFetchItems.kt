package io.lamart.rijksart.logic

import MarloveItems
import io.lamart.rijksart.logic.utils.getAndFetch
import io.lamart.rijksart.optics.Source
import io.lamart.rijksart.optics.async.initial
import io.lamart.rijksart.optics.async.latest
import io.lamart.rijksart.optics.async.toAsyncAction
import io.lamart.rijksart.optics.compose
import io.lamart.rijksart.optics.lensOf
import kotlinx.coroutines.CoroutineScope

internal fun getAndFetchItemsOf(
    scope: CoroutineScope,
    source: Source<State>,
    getItems: suspend (page: Int) -> MarloveItems?,
    setItems: suspend (page: Int, items: MarloveItems) -> Unit,
    fetchItems: suspend (page: Int) -> MarloveItems,
): (page: Int) -> Unit {
    return { page ->
        val items = source
            .compose(lensOf({ items }, { copy(items = it) }))
            .compose(lensOf(
                select = { get(page) ?: initial() },
                copy = { collection -> plus(page to collection) }
            ))
        val action = items.toAsyncAction(
            strategy = latest(getAndFetch(
                get = getItems,
                set = setItems,
                fetch = fetchItems
            )),
            scope
        )

        action(page)
    }
}