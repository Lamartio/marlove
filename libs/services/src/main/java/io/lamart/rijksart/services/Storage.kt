package io.lamart.rijksart.services

import MarloveItems
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.lamart.rijksart.domain.ArtCollection
import io.lamart.rijksart.domain.ArtDetails

interface Storage: RijksmuseumStorage, MarloveStorage

interface MarloveStorage {

    suspend fun getItems(page: String?): MarloveItems?

    suspend fun setItems(page: String?, items: MarloveItems)

}

interface RijksmuseumStorage {

    suspend fun getCollection(page: Int): ArtCollection?

    suspend fun setCollection(page: Int, collection: ArtCollection)

    suspend fun getDetails(objectNumber: String): ArtDetails?

    suspend fun setDetails(objectNumber: String, details: ArtDetails)

}

internal fun storageOf(context: Context): Storage =
    object : Storage, StorageMixin by storageMixinOf(context, "data") {

        override suspend fun getItems(page: String?): MarloveItems? =
            get(itemsKeyOf(page), MarloveItem.listSerializer())

        override suspend fun setItems(page: String?, items: MarloveItems) =
            set(itemsKeyOf(page), MarloveItem.listSerializer(), items)

        override suspend fun getCollection(page: Int): ArtCollection? =
            get(collectionKeyOf(page), ArtCollection.serializer())

        override suspend fun setCollection(page: Int, collection: ArtCollection) =
            set(collectionKeyOf(page), ArtCollection.serializer(), collection)

        override suspend fun getDetails(objectNumber: String): ArtDetails? =
            get(detailsKeyOf(objectNumber), ArtDetails.serializer())

        override suspend fun setDetails(objectNumber: String, details: ArtDetails) =
            set(detailsKeyOf(objectNumber), ArtDetails.serializer(), details)

        private fun itemsKeyOf(page: String?): Preferences.Key<String> =
            stringPreferencesKey("items_$page")

        private fun collectionKeyOf(page: Int): Preferences.Key<String> =
            stringPreferencesKey("collection_$page")

        private fun detailsKeyOf(objectNumber: String): Preferences.Key<String> =
            stringPreferencesKey("details_$objectNumber")

    }
