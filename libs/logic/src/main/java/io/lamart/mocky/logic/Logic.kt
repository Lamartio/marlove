package io.lamart.mocky.logic

import io.lamart.mocky.marlove.Marlove
import io.lamart.mocky.optics.sourceOf
import io.lamart.mocky.services.Services
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface Logic {

    val state: Flow<State>

    val actions: Actions

}

fun logicOf(marlove: Marlove, services: Services, scope: CoroutineScope): Logic {
    val state = MutableStateFlow(State())
    val source = sourceOf(
        get = { state.value },
        set = state::tryEmit
    )
    val dependencies = dependenciesOf(source, marlove, services, scope)

    return object : Logic {
        override val state: Flow<State> = state
        override val actions: Actions = dependencies.toActions()
    }
}