package io.lamart.rijksart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.lamart.rijksart.details.DetailsViewModel
import io.lamart.rijksart.logic.Logic
import io.lamart.rijksart.main.MainViewModel

class RijksViewModelProviderFactory(
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