package io.lamart.mocky

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.properties.ReadOnlyProperty

fun <T> Flow<T>.toStateDelegate(initial: T): ReadOnlyProperty<ViewModel, State<T>> =
    toMutableStateDelegate(initial)

fun <T> Flow<T>.toMutableStateDelegate(initial: T): ReadOnlyProperty<ViewModel, MutableState<T>> =
    ReadOnlyProperty { viewModel, _ ->
        mutableStateOf(initial).apply {
            this@toMutableStateDelegate
                .onEach { value = it }
                .launchIn(viewModel.viewModelScope)
        }
    }

fun Fragment.navigate(directions: NavDirections) =
    this
        .requireActivity()
        .supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment)
        .let { it as NavHostFragment }
        .navController
        .navigate(directions)