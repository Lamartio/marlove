package io.lamart.mocky.logic

import MarloveItem
import com.google.common.truth.Truth.assertThat
import io.lamart.mocky.optics.sourceOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import org.junit.Test

class Logic {

    @Test
    fun getAndFetchItems() = runBlocking {
        val job = Job(coroutineContext[Job])
        val states = mutableListOf(State())
        val source = sourceOf(states::last, states::add)
        val getItems = getAndFetchItemsOf(
            scope = this + job,
            source = source,
            { null },
            setItems = { a, b -> },
            fetchItems = { listOf(MarloveItem("id", 1.0, "image", "text")) }
        )

        getItems(0)
        joinAll(job)

        assertThat(states).hasSize(0)
    }
}