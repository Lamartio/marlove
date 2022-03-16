package io.lamart.mocky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.lamart.mocky.details.DetailsViewModel
import io.lamart.mocky.logic.Logic
import io.lamart.mocky.main.MainViewModel

class MockyViewModelProviderFactory(
    private val defaultFactory: ViewModelProvider.Factory,
    private val getLogic: () -> Logic,
) : ViewModelProvider.Factory {

    private val logic: Logic
        get() = getLogic()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(logic)
            DetailsViewModel::class.java -> DetailsViewModel(logic)
            else -> defaultFactory.create(modelClass)
        } as T
}