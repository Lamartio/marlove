package io.lamart.mocky.rijksmuseum

import okhttp3.Interceptor

internal fun keyInterceptorOf(key: String): Interceptor =
    Interceptor { chain ->
        chain
            .request()
            .newBuilder()
            .url(chain.addQueryParameter("key", key))
            .build()
            .let(chain::proceed)
    }

internal fun Interceptor.Chain.addQueryParameter(key: String, value: String) =
    this
        .request()
        .url
        .newBuilder()
        .addQueryParameter(key, value)
        .build()
