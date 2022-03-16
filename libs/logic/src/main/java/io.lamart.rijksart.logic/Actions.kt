package io.lamart.rijksart.logic

import MarloveItem
import io.lamart.rijksart.optics.Source
import io.lamart.rijksart.optics.async.Async
import io.lamart.rijksart.optics.compose
import io.lamart.rijksart.optics.lensOf
import kotlinx.coroutines.flow.Flow

interface Actions {
    fun appendCollection()
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

        override fun appendCollection() {
            val items = source
                .compose(lensOf({ items }, { copy(items = it) }))
                .get()
            val isNotExecuting = items.values.none { it is Async.Executing }

            if (isNotExecuting) {
                val id = items
                    .getFlattenedItems()
                    .lastOrNull()
                    ?._id

                getAndFetchItems(id)
            }
        }
    }

private fun Source<State>.findItem(id: String): MarloveItem? =
    this
        .compose(lensOf({ items }, { copy(items = it) }))
        .get()
        .getFlattenedItems()
        .firstOrNull { it._id == id }