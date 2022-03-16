package io.lamart.mocky.domain

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class Serialization {

    @Test
    fun serializeArCollection() {
        val collection = ArtCollection(
            artObjects = listOf(
                ArtCollection.ArtObject(
                    hasImage = false,
                    headerImage = ArtCollection.HeaderImage("", 1, 1, 1, "", 1),
                    id = "",
                    links = ArtCollection.Links("", ""),
                    longTitle = "b",
                    objectNumber = "a",
                    permitDownload = false,
                    principalOrFirstMaker = "Danny",
                    productionPlaces = listOf("abc"),
                    showImage = false,
                    title = "hello",
                    webImage = ArtCollection.WebImage("", 1, 1, 1, "", 1)
                )
            )
        )
        val value = Json.encodeToString(ArtCollection.serializer(), collection)
        val decodedCollection = Json.decodeFromString(ArtCollection.serializer(), value)

        assertEquals(collection, decodedCollection)
    }
}