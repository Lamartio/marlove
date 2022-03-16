package io.lamart.mocky.details

import MarloveItem
import androidx.lifecycle.ViewModel
import io.lamart.mocky.logic.Details
import io.lamart.mocky.logic.Logic
import io.lamart.mocky.logic.State
import io.lamart.mocky.toStateDelegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailsViewModel internal constructor(details: Flow<MarloveItem?>) : ViewModel() {

    constructor(logic: Logic) : this(details = logic.state.getDetails())

    val details by details.toStateDelegate(null)

}

fun Flow<State>.getDetails(): Flow<MarloveItem?> =
    map {
        when (val details = it.details) {
            Details.None -> null
            is Details.Some -> details.item
        }
    }
