package io.lamart.mocky.main

import androidx.lifecycle.ViewModel
import io.lamart.mocky.logic.Logic
import io.lamart.mocky.logic.State
import io.lamart.mocky.logic.getFlattenedItems
import io.lamart.mocky.optics.async.Async
import io.lamart.mocky.toStateDelegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainViewModel internal constructor(
    items: Flow<List<Item>>,
    isLoading: Flow<Boolean>,
    val loadMore: () -> Unit,
    val select: (id: String) -> Unit,
    val refresh: () -> Unit
) : ViewModel() {

    constructor(logic: Logic): this(
        items = logic.state.getItems(),
        isLoading = logic.state.isLoadingCollection(),
        loadMore = logic.actions::appendItems,
        select = logic.actions.select,
        refresh = logic.actions::refresh
    )

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