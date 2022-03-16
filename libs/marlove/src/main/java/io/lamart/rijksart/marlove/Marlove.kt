package io.lamart.rijksart.marlove

import MarloveItem
import MarloveItems
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Marlove {

    @GET("items")
    suspend fun getItems(
        @Query("since_id") sinceId: String? = null,
        @Query("max_id") maxId: String? = null,
    ): MarloveItems

}

fun marloveOf(key: String, url: String = "https://marlove.net/e/mock/v1/"): Marlove {
    val client = OkHttpClient.Builder()
        .addInterceptor(appendHeaderInterceptorOf("Authorization", key))
        .build()

    return Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Marlove::class.java)
}

fun appendHeaderInterceptorOf(key: String, value: String): Interceptor =
    Interceptor { chain ->
        chain
            .request()
            .newBuilder()
            .header(key, value)
            .build()
            .let(chain::proceed)
    }
