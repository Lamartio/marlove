package io.lamart.mocky.optics

import io.lamart.mocky.optics.async.Async
import io.lamart.mocky.optics.async.initial

data class State(val downloading: Async<String> = initial())
