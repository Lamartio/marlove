package io.lamart.rijksart.main

import MarloveItems
import androidx.lifecycle.ViewModel
import io.lamart.rijksart.logic.State
import io.lamart.rijksart.logic.getFlattenedItems
import io.lamart.rijksart.optics.async.Async
import io.lamart.rijksart.toStateDelegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainViewModel(
    items: Flow<List<Item>>,
    isLoading: Flow<Boolean>,
    val loadMore: () -> Unit,
    val select: (id: String) -> Unit
) : ViewModel() {

    val isLoading by isLoading.toStateDelegate(false)

    val items by items.toStateDelegate(emptyList<Item>())

}

fun Flow<State>.isLoadingCollection(): Flow<Boolean> =
    map { state ->
        state
            .items
            .any { (_, value) -> value is Async.Executing }
    }

sealed class Item(open val id: String) {
    data class Default(override val id: String, val text: String) : Item(id)
    data class More(override val id: String) : Item(id)
}

fun Flow<State>.getItems(): Flow<List<Item>> =
    map { state ->
        state
            .items
            .getFlattenedItems()
            .map { Item.Default(it._id, it.text) }
            .let { it + Item.More("more_button") }
    }