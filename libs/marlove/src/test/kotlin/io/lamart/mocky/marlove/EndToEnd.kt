package io.lamart.mocky.marlove

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class EndToEnd {

    lateinit var marlove: Marlove

    @Before
    fun setup() {
        marlove = marloveOf(key = "6fff872af5a76800585c969ac8dff3d6")
    }


    @Test
    fun test() = runBlocking {
         marlove
             .getItems()
             .let(::assertThat)
             .isNotEmpty()
    }

}