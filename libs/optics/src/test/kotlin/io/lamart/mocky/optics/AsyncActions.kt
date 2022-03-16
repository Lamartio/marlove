package io.lamart.mocky.optics

import com.google.common.truth.Truth.assertThat
import io.lamart.mocky.optics.async.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test


class AsyncActions {

    private val initial = State()

    @Test
    fun executeAsyncActionSuccessfully() = runBlocking {
        // Arrange
        val job = Job(coroutineContext[Job])
        val result = mutableListOf<State>(initial)
        val source = sourceOf({ result.last() }, result::add)
        val download = source
            .compose(lensOf({ downloading }, { copy(downloading = it) }))
            .toAsyncAction<Int, String>(
                strategy = latest { flowOf(it.toString()) },
                collectOn = finiteCollectorOf(job, 2)
            )
        // Act
        download(123)
        job.join()
        // Assert
        assertThat(result).hasSize(3)
        assertThat(result[0].downloading).isInstanceOf(Async.Initial::class.java)
        assertThat(result[1].downloading).isInstanceOf(Async.Executing::class.java)
        assertThat(result[2].downloading).isInstanceOf(Async.Success::class.java)
        assertThat(result[2].downloading.result).isEqualTo("123")
    }

    @Test
    fun executeAsyncActionFailing() = runBlocking {
        // Arrange
        val job = Job(coroutineContext[Job])

        val result = mutableListOf<State>(initial)
        val source = sourceOf({ result.last() }, result::add)
        val download = source
            .compose(lensOf({ downloading }, { copy(downloading = it) }))
            .toAsyncAction<Int, String>(
                strategy = latest(flowOfSuspending { throw Throwable("Whoops!") }),
                collectOn = finiteCollectorOf(job, 2)
            )
        // Act
        download(123)
        job.join()
        // Assert
        assertThat(result).hasSize(3)
        assertThat(result[0].downloading).isInstanceOf(Async.Initial::class.java)
        assertThat(result[1].downloading).isInstanceOf(Async.Executing::class.java)
        assertThat(result[2].downloading).isInstanceOf(Async.Failure::class.java)
        assertThat(result[2].downloading.reason).isNotNull()
    }

}
