package io.lamart.mocky.logic

import io.lamart.mocky.marlove.Marlove
import io.lamart.mocky.optics.Source
import io.lamart.mocky.services.Services
import kotlinx.coroutines.CoroutineScope

internal interface Dependencies : Services {
    val source: Source<State>
    val marlove: Marlove
    val scope: CoroutineScope
}

internal fun dependenciesOf(
    source: Source<State>,
    marlove: Marlove,
    services: Services,
    scope: CoroutineScope,
): Dependencies =
    object : Dependencies, Services by services {
        override val source: Source<State> = source
        override val marlove: Marlove = marlove
        override val scope: CoroutineScope = scope
    }