package io.lamart.mocky.logic

import com.google.common.truth.Truth.assertThat
import io.lamart.mocky.logic.utils.getAndFetch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAndFetch {

    @Test
    fun `When getting and fetching fails, then it should throw`() = runBlocking {
        // Arrange
        val (getCalls, get) = suspendingLambdaSpy1<Int, String> { throw Throwable("Disk exploded") }
        val (setCalls, set) = suspendingLambdaSpy2<Int, String, Unit> { _, _ -> }
        val (fetchCalls, fetch) = suspendingLambdaSpy1<Int, String> { throw Throwable("Network exploded") }
        val getAndFetchString = getAndFetch(get, set, fetch)
        // Act
        val result = runCatching { getAndFetchString(1).toList() }
        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(getCalls).hasSize(1)
        assertThat(setCalls).hasSize(0)
        assertThat(fetchCalls).hasSize(1)
    }

    @Test
    fun `When getting succeeds with null and fetching fails, then it should throw`() = runBlocking {
        // Arrange
        val (getCalls, get) = suspendingLambdaSpy1<Int, String?> { null }
        val (setCalls, set) = suspendingLambdaSpy2<Int, String, Unit> { _, _ -> }
        val (fetchCalls, fetch) = suspendingLambdaSpy1<Int, String> { throw Throwable("Network exploded") }
        val getAndFetchString = getAndFetch(get, set, fetch)
        // Act
        val result = runCatching { getAndFetchString(1).toList() }
        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(getCalls).hasSize(1)
        assertThat(setCalls).hasSize(0)
        assertThat(fetchCalls).hasSize(1)
    }

    @Test
    fun `When getting succeeds, but fetching fails, then is should emit a single value`() =
        runBlocking {
            // Arrange
            val (getCalls, get) = suspendingLambdaSpy1<Int, String> { it.toString() }
            val (setCalls, set) = suspendingLambdaSpy2<Int, String, Unit> { _, _ -> }
            val (fetchCalls, fetch) = suspendingLambdaSpy1<Int, String> { throw Throwable("Network exploded") }
            val getAndFetchString = getAndFetch(get, set, fetch)
            // Act
            val result = getAndFetchString(1).toList()
            // Assert
            assertThat(result).hasSize(1)
            assertThat(getCalls).hasSize(1)
            assertThat(setCalls).hasSize(0)
            assertThat(fetchCalls).hasSize(1)
        }

    @Test
    fun `When getting fails and fetching succeeds, then is should emit a single value`() =
        runBlocking {
            // Arrange
            val (getCalls, get) = suspendingLambdaSpy1<Int, String> { throw Throwable("Disk exploded") }
            val (setCalls, set) = suspendingLambdaSpy2<Int, String, Unit> { _, _ -> }
            val (fetchCalls, fetch) = suspendingLambdaSpy1<Int, String> { it.toString() }
            val getAndFetchString = getAndFetch(get, set, fetch)
            // Act
            val result = getAndFetchString(1).toList()
            // Assert
            assertThat(result).hasSize(1)
            assertThat(getCalls).hasSize(1)
            assertThat(setCalls).hasSize(1)
            assertThat(fetchCalls).hasSize(1)
        }

    @Test
    fun `When getting and fetching succeeds, then is should emit both values`() = runBlocking {
        // Arrange
        val (getCalls, get) = suspendingLambdaSpy1<Int, String> { it.toString() }
        val (setCalls, set) = suspendingLambdaSpy2<Int, String, Unit> { _, _ -> }
        val (fetchCalls, fetch) = suspendingLambdaSpy1<Int, String> { it.toString() }
        val getAndFetchString = getAndFetch(get, set, fetch)
        // Act
        val result = getAndFetchString(1).toList()
        // Assert
        assertThat(result).hasSize(2)
        assertThat(getCalls).hasSize(1)
        assertThat(setCalls).hasSize(1)
        assertThat(fetchCalls).hasSize(1)
    }

}