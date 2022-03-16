package io.lamart.rijksart.logic

import MarloveItem
import MarloveItems
import io.lamart.rijksart.domain.ArtCollection
import io.lamart.rijksart.domain.ArtDetails
import io.lamart.rijksart.optics.async.Async
import io.lamart.rijksart.optics.async.initial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class State(
    val items: Map<String?, Async<MarloveItems>> = emptyMap(),
    val details: Details = Details.None,
)

sealed class Details {
    data class Some(val item: MarloveItem) : Details()
    object None : Details()

}

fun detailsOfNullable(item: MarloveItem?): Details {
    return when (item) {
        null -> Details.None
        else -> Details.Some(item)
    }
}


fun Map<String?, Async<MarloveItems>>.getFlattenedItems(): List<MarloveItem> =
    this
        .values
        .flatMap { async ->
            when (async) {
                is Async.Success<MarloveItems> -> async.result
                else -> emptyList()
            }
        }
        .distinctBy { it._id }
        .sortedBy { it._id }
        .reversed()