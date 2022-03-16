package io.lamart.rijksart.logic

import io.lamart.rijksart.optics.async.Async
import io.lamart.rijksart.optics.compose
import io.lamart.rijksart.optics.lensOf

interface Actions {
    fun appendCollection()
    val select: (id: String) -> Unit
    val getAndFetchItems: (page: Int) -> Unit
}

internal fun Dependencies.toActions(): Actions =
    object : Actions {

        override val getAndFetchItems: (page: Int) -> Unit =
            getAndFetchItemsOf(
                scope,
                source,
                storage::getItems,
                storage::setItems,
                { marlove.getItems() }
            )

        override val select: (id: String) -> Unit =
            { id ->
                val item = source
                    .compose(lensOf({ items }, { copy(items = it) }))
                    .get()
                    .values
                    .flatMap {
                        when (it) {
                            is Async.Success -> it.result
                            else -> emptyList()
                        }
                    }
                    .firstOrNull { it._id == id }

                source
                    .compose(lensOf({ details }, { copy(details = it)}))
                    .set(detailsOfNullable(item))
            }

        override fun appendCollection() {
            val items = source
                .compose(lensOf({ items }, { copy(items = it) }))
                .get()
            val isNotExecuting = items.values.none { it is Async.Executing }

            if (isNotExecuting) {
                val lastPage = items
                    .filterValues { it is Async.Success }
                    .keys
                    .maxOrNull()
                    ?: -1

                getAndFetchItems(lastPage + 1)
            }
        }
    }