package io.lamart.mocky.rijksmuseum

import io.lamart.mocky.domain.ArtCollection
import io.lamart.mocky.domain.ArtDetails
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RijksMuseum {

    @GET("collection")
    suspend fun getCollection(
        @Query("p") page: Int,
        @Query("ps") pageSize: Int = 30,
        @Query("s") sort: String = "artist",
    ): ArtCollection

    @GET("collection/{objectNumber}")
    suspend fun getDetails(@Path("objectNumber") objectNumber: String): ArtDetails

}

fun rijksMuseumOf(key: String, url: String = "https://www.rijksmuseum.nl/api/en/"): RijksMuseum {
    val client = OkHttpClient.Builder()
        .addInterceptor(keyInterceptorOf(key))
        .build()

    return Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RijksMuseum::class.java)
}