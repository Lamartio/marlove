package io.lamart.rijksart.logic

import MarloveItem
import io.lamart.rijksart.optics.Source
import io.lamart.rijksart.optics.async.Async
import io.lamart.rijksart.optics.compose
import io.lamart.rijksart.optics.lensOf

interface Actions {
    fun appendItems()
    fun refresh()

    val select: (id: String) -> Unit
    val getAndFetchItems: (page: String?) -> Unit
}

internal fun Dependencies.toActions(): Actions =
    object : Actions {

        override val getAndFetchItems: (lastId: String?) -> Unit =
            getAndFetchItemsOf(
                scope,
                source,
                storage::getItems,
                storage::setItems,
                marlove::getItems
            )

        override val select: (id: String) -> Unit =
            { id ->
                val item = source.findItem(id)
                val details = detailsOfNullable(item)

                source
                    .compose(lensOf({ details }, { copy(details = it) }))
                    .set(details)
            }

        override fun appendItems() {
            val items = source
                .compose(lensOf({ items }, { copy(items = it) }))
                .get()
            val isExecuting = items.values.any { it is Async.Executing }

            if (!isExecuting) {
                items
                    .getFlattenedItems()
                    .lastOrNull()
                    ?._id
                    .let(getAndFetchItems)
            }
        }

        override fun refresh() {
            val items = source
                .compose(lensOf({ items }, { copy(items = it) }))
                .get()
            val isExecuting = items.values.any { it is Async.Executing }

            if (!isExecuting) {
                items
                    .keys
                    .let { it + null }
                    .distinct()
                    .forEach(getAndFetchItems)
            }
        }
    }

private fun Source<State>.findItem(id: String): MarloveItem? =
    this
        .compose(lensOf({ items }, { copy(items = it) }))
        .get()
        .getFlattenedItems()
        .firstOrNull { it._id == id }