package io.lamart.mocky.logic

import MarloveItems
import io.lamart.mocky.logic.utils.getAndFetch
import io.lamart.mocky.optics.Source
import io.lamart.mocky.optics.async.concat
import io.lamart.mocky.optics.async.initial
import io.lamart.mocky.optics.async.toAsyncAction
import io.lamart.mocky.optics.compose
import io.lamart.mocky.optics.lensOf
import kotlinx.coroutines.CoroutineScope

internal fun getAndFetchItemsOf(
    scope: CoroutineScope,
    source: Source<State>,
    getItems: suspend (page: String?) -> MarloveItems?,
    setItems: suspend (page: String?, items: MarloveItems) -> Unit,
    fetchItems: suspend (page: String?) -> MarloveItems,
): (page: String?) -> Unit =
    { page ->
        val items = source
            .compose(lensOf({ items }, { copy(items = it) }))
            .compose(lensOf(
                select = { get(page) ?: initial() },
                copy = { collection -> plus(page to collection) }
            ))
        val action = items.toAsyncAction(
            strategy = concat(getAndFetch(
                get = getItems,
                set = setItems,
                fetch = fetchItems
            )),
            scope
        )

        action(page)
    }