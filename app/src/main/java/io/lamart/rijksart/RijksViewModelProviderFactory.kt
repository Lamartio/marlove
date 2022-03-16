package io.lamart.rijksart

import MarloveItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.lamart.rijksart.details.DetailsViewModel
import io.lamart.rijksart.details.getDetails
import io.lamart.rijksart.logic.Details
import io.lamart.rijksart.logic.Logic
import io.lamart.rijksart.logic.State
import io.lamart.rijksart.main.MainViewModel
import io.lamart.rijksart.main.getItems
import io.lamart.rijksart.main.isLoadingCollection
import io.lamart.rijksart.optics.async.Async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class RijksViewModelProviderFactory(
    private val defaultFactory: ViewModelProvider.Factory,
    private val getLogic: () -> Logic,
) : ViewModelProvider.Factory {

    private val logic: Logic
        get() = getLogic()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(
                items = logic.state.getItems(),
                isLoading = logic.state.isLoadingCollection(),
                loadMore = logic.actions::appendCollection,
                select = logic.actions.select
            )
            DetailsViewModel::class.java -> DetailsViewModel(logic.state.getDetails())
            else -> defaultFactory.create(modelClass)
        } as T
}