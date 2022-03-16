package io.lamart.mocky.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

/**
 * The mixin facilitates default logic that is used within Storage.kt
 */

internal interface StorageMixin {

    suspend fun <T : Any> get(
        key: Preferences.Key<String>,
        deserializer: DeserializationStrategy<T>,
    ): T?

    suspend fun <T : Any> set(
        key: Preferences.Key<String>,
        serializer: SerializationStrategy<T>,
        value: T,
    )

}

internal fun storageMixinOf(context: Context, name: String): StorageMixin =
    object : StorageMixin {

        private val Context.store: DataStore<Preferences> by preferencesDataStore(name)

        override suspend fun <T : Any> get(
            key: Preferences.Key<String>,
            deserializer: DeserializationStrategy<T>,
        ): T? =
            context
                .store
                .data
                .map { it[key] }
                .firstOrNull()
                ?.let { Json.decodeFromString(deserializer, it) }

        override suspend fun <T : Any> set(
            key: Preferences.Key<String>,
            serializer: SerializationStrategy<T>,
            value: T,
        ) {
            context
                .store
                .edit { settings -> settings[key] = Json.encodeToString(serializer, value) }
        }

    }